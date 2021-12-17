package ru.labs.grading.services;

import com.google.protobuf.ByteString;
import io.grpc.stub.StreamObserver;
import lombok.extern.slf4j.Slf4j;
import org.lognet.springboot.grpc.GRpcService;
import org.springframework.beans.factory.annotation.Autowired;
import ru.labs.grading.FileUploadRequest;
import ru.labs.grading.FileUploadResponse;
import ru.labs.grading.FileUploadServiceGrpc;
import ru.labs.grading.Status;
import ru.labs.grading.repositories.TaskRepository;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

@GRpcService
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
        log.info("Start uploading file");

        return new StreamObserver<FileUploadRequest>() {
            OutputStream writer;
            Status status = Status.IN_PROGRESS;

            @Override
            public void onNext(FileUploadRequest fileUploadRequest) {
                try {
                    if (fileUploadRequest.hasMetadata()) {
                        final String taskId = fileUploadRequest.getMetadata().getTaskId();
                        final String fileName = taskId + "-" + fileUploadRequest.getMetadata().getName() + "." + fileUploadRequest.getMetadata().getType();
                        final String developerFullName = fileUploadRequest.getMetadata().getDeveloperFullName();
                        writer = getFilePath(fileName);
                        saveFileMetadataToRepository(taskId, developerFullName, fileName);
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
                        .build();
                responseObserver.onNext(response);
                responseObserver.onCompleted();
            }
        };
    }

    private OutputStream getFilePath(String fileName) throws IOException {
        return Files.newOutputStream(SERVER_BASE_PATH.resolve(fileName), StandardOpenOption.CREATE, StandardOpenOption.APPEND);
    }

    private void saveFileMetadataToRepository(String taskId, String developerFullName, String fileName) {
        taskRepository.saveFileMetadata(taskId, developerFullName, fileName);
        log.info("Saved file: {}, developerFullName: {}, taskId: {}", fileName, developerFullName, taskId);
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
