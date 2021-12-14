package ru.labs.grading;

import com.google.protobuf.ByteString;
import io.grpc.stub.StreamObserver;
import lombok.extern.log4j.Log4j;
import lombok.extern.slf4j.Slf4j;
import org.lognet.springboot.grpc.GRpcService;
import org.springframework.beans.factory.annotation.Autowired;
import ru.labs.grading.repositories.TaskRepository;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

@GRpcService //анатации нет в примере
@Slf4j
public class FileUploadServiceImpl extends FileUploadServiceGrpc.FileUploadServiceImplBase {

    private static final Path SERVER_BASE_PATH = Paths.get("src/main/resources/output");

    private final TaskRepository taskRepository;

    @Autowired
    public FileUploadServiceImpl(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    @Override
    public StreamObserver<FileUploadRequest> uploadFile(StreamObserver<FileUploadResponse> responseObserver) {
        return new StreamObserver<FileUploadRequest>() {
            // upload context variables
            OutputStream writer;
            Status status = Status.IN_PROGRESS;
            String taskId;
            @Override
            public void onNext(FileUploadRequest fileUploadRequest) {
                try {
                    if (fileUploadRequest.hasMetadata()) {
                        writer = getFilePath(fileUploadRequest);
                        taskId= saveFileToRepository(fileUploadRequest);
                    } else {
                        writeFile(writer, fileUploadRequest.getFile().getContent());
                    }
                } catch (IOException e) {
                    this.onError(e);
                }
            }

            @Override
            public void onError(Throwable throwable) {
                status = Status.FAILED;
                this.onCompleted();
            }

            @Override
            public void onCompleted() {
                closeFile(writer);
                status = Status.IN_PROGRESS.equals(status) ? Status.SUCCESS : status;
                FileUploadResponse response = FileUploadResponse.newBuilder()
                        .setStatus(status)
                        .setName(taskId)
                        .build();
                responseObserver.onNext(response);
                responseObserver.onCompleted();
            }
        };
    }

    private OutputStream getFilePath(FileUploadRequest request) throws IOException {
        var fileName = request.getMetadata().getName() + "." + request.getMetadata().getType();
        return Files.newOutputStream(SERVER_BASE_PATH.resolve(fileName), StandardOpenOption.CREATE, StandardOpenOption.APPEND);
    }

    private String saveFileToRepository(FileUploadRequest request) {
        String pathFile = SERVER_BASE_PATH + "/" + request.getMetadata().getName() + "." + request.getMetadata().getType();
        String developerFullName = request.getMetadata().getDeveloperFullName();
        String taskId = taskRepository.saveFile(pathFile, developerFullName);
        log.info("Save file: {}, developerFullName: {}, taskId: {}", pathFile, developerFullName,taskId);
        return taskId;
    }

    private void writeFile(OutputStream writer, ByteString content) throws IOException {
        writer.write(content.toByteArray());
        writer.flush();
    }

    private void closeFile(OutputStream writer) {
        try {
            writer.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
