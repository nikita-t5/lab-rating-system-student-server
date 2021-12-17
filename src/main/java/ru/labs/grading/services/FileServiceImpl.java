package ru.labs.grading.services;

import com.google.protobuf.ByteString;
import io.grpc.Status;
import io.grpc.stub.StreamObserver;
import lombok.extern.slf4j.Slf4j;
import org.lognet.springboot.grpc.GRpcService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import ru.labs.grading.DataChunk;
import ru.labs.grading.DownloadFileRequest;
import ru.labs.grading.FileServiceGrpc;
import ru.labs.grading.repositories.TaskRepository;

import java.io.*;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Slf4j
@GRpcService
public class FileServiceImpl extends FileServiceGrpc.FileServiceImplBase {

    private final TaskRepository taskRepository;

    private final String OUTPUT_PATH;

    @Autowired
    public FileServiceImpl(TaskRepository taskRepository, @Value("${const.outputPath}") String OUTPUT_PATH) {
        this.taskRepository = taskRepository;
        this.OUTPUT_PATH = OUTPUT_PATH;
    }

    @Override
    public void downloadFile(DownloadFileRequest request, StreamObserver<DataChunk> responseObserver) {
        log.info("Start downloading file for taskID :: {}", request.getTaskId());
        try {
            final String fileName = taskRepository.getFileNameByTaskId(request.getTaskId());
            final URI uriResource = new URI(OUTPUT_PATH);

            final File fileFromResources = Files.walk(Paths.get(uriResource))
                    .filter(Files::isRegularFile)
                    .map(Path::toFile)
                    .filter(file -> file.getName().equals(fileName))
                    .findAny()
                    .orElseThrow();

            InputStream inputStream = new FileInputStream(fileFromResources);

            byte[] bytes = inputStream.readAllBytes();
            BufferedInputStream imageStream = new BufferedInputStream(new ByteArrayInputStream(bytes));
            int bufferSize = 1024;// 1K
            byte[] buffer = new byte[bufferSize];
            int length;
            while ((length = imageStream.read(buffer, 0, bufferSize)) != -1) {
                responseObserver.onNext(DataChunk.newBuilder()
                        .setData(ByteString.copyFrom(buffer, 0, length))
                        .setSize(bufferSize)
                        .build());
            }
            imageStream.close();
            responseObserver.onCompleted();
        } catch (Throwable e) {
            log.error("Unable to acquire the file with taskId :: {}", e.getMessage());
            responseObserver.onError(Status.ABORTED
                    .withDescription("Unable to acquire the file with taskId " + request.getTaskId())
                    .withCause(e)
                    .asException());
        }
    }
}
