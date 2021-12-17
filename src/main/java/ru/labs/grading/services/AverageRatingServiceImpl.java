package ru.labs.grading.services;

import io.grpc.stub.StreamObserver;
import lombok.extern.slf4j.Slf4j;
import org.lognet.springboot.grpc.GRpcService;
import org.springframework.beans.factory.annotation.Autowired;
import ru.labs.grading.AverageRatingServiceGrpc;
import ru.labs.grading.AverageRatingServiceOuterClass;
import ru.labs.grading.repositories.TaskRepository;

@Slf4j
@GRpcService
public class AverageRatingServiceImpl extends AverageRatingServiceGrpc.AverageRatingServiceImplBase {

    private final TaskRepository taskRepository;

    @Autowired
    public AverageRatingServiceImpl(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    @Override
    public void getAverageRatingByTaskId(AverageRatingServiceOuterClass.AverageRatingRequest request, StreamObserver<AverageRatingServiceOuterClass.AverageRatingResponse> responseObserver) {
        log.info("Start getting average rating for taskID :: {}", request.getTaskId());
        final Double averageRating = taskRepository.getAverageRatingByTaskId(request.getTaskId());

        AverageRatingServiceOuterClass.AverageRatingResponse response =
                AverageRatingServiceOuterClass.AverageRatingResponse
                        .newBuilder()
                        .setAverageRating(averageRating)
                        .build();
        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }
}
