package ru.labs.grading.services;

import com.google.protobuf.Empty;
import io.grpc.stub.StreamObserver;
import lombok.extern.slf4j.Slf4j;
import org.lognet.springboot.grpc.GRpcService;
import org.springframework.beans.factory.annotation.Autowired;
import ru.labs.grading.MinRatingServiceGrpc;
import ru.labs.grading.MinRatingServiceOuterClass;
import ru.labs.grading.repositories.TaskRepository;

import java.util.List;

@Slf4j
@GRpcService
public class MinRatingServiceImpl extends MinRatingServiceGrpc.MinRatingServiceImplBase {

    private final TaskRepository taskRepository;

    @Autowired
    public MinRatingServiceImpl(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    @Override
    public void getMinRatingList(Empty request, StreamObserver<MinRatingServiceOuterClass.MinRatingResponse> responseObserver) {
        log.info("Start getting min rating list");

        final List<String> minRatingList = taskRepository.getMinRatingList();

        MinRatingServiceOuterClass.MinRatingResponse response =
                MinRatingServiceOuterClass.MinRatingResponse
                        .newBuilder()
                        .addAllTaskId(minRatingList)
                        .build();

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }
}
