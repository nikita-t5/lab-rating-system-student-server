package ru.labs.grading;

import com.google.protobuf.ByteString;
import io.grpc.Status;
import io.grpc.stub.StreamObserver;
import lombok.extern.slf4j.Slf4j;
import org.lognet.springboot.grpc.GRpcService;
import org.springframework.beans.factory.annotation.Autowired;
import ru.labs.grading.repositories.TaskRepository;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.InputStream;
import java.net.URL;

@Slf4j
@GRpcService
public class FileServiceImpl extends FileServiceGrpc.FileServiceImplBase {

    private final TaskRepository taskRepository;


    @Autowired
    public FileServiceImpl(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    //1 найти по id файл и передать его


    @Override
    public void downloadFile(DownloadFileRequest request, StreamObserver<DataChunk> responseObserver) {
        try {
            String fileName = "output/" + taskRepository.getPathFileByTaskId(request.getTaskId());

//            String fileName1 = "output/";
//            String fileName2 =  taskRepository.getPathFileByTaskId(request.getTaskId());

//            String fileName.txt = "/files/test2/txt";


//            String fileName.txt = "/output/" + request.getTaskId();

            // read the file and convert to a byte array
//            InputStream inputStream = getClass().getResourceAsStream(fileName.txt);
//            InputStream is = new ClassPathResource("/files/test2/txt").getInputStream();
//            InputStream is = ClassLoader.class.getResourceAsStream("/files/test2.txt");
//            URL url =  this.getClass().getResource("/files/test2/txt");
//            InputStream input = url.openStream();
//            ClassLoader classloader = Thread.currentThread().getContextClassLoader();
//            InputStream is = classloader.getResourceAsStream("test2.txt");

//            InputStream is = FileServiceImpl.class.getResourceAsStream("/test2.txt");


            //yes
//            ClassLoader classLoader = getClass().getClassLoader();
//                                                                              "files/fileName.txt"
//            String aaaa = "output/9038e53d-f720-406f-bc14-df1895cbe843-test2.txt";
//            java.io.File file =  new java.io.File(classLoader.getResource(fileName1 + fileName2).getFile());
//            InputStream inputStream = new FileInputStream(file);

            InputStream inputStream = getClass().getResourceAsStream(fileName);
            byte[] bytes = inputStream.readAllBytes();
            BufferedInputStream imageStream = new BufferedInputStream(new ByteArrayInputStream(bytes));

            int bufferSize = 1 * 1024;// 1K
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
