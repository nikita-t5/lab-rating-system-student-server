package ru.labs.grading;

import io.grpc.stub.StreamObserver;
import org.lognet.springboot.grpc.GRpcService;
import org.springframework.beans.factory.annotation.Autowired;
import ru.labs.grading.repositories.TaskRepository;

@GRpcService
public class AverageRatingServiceImpl extends AverageRatingServiceGrpc.AverageRatingServiceImplBase {

    private final TaskRepository taskRepository;

    @Autowired
    public AverageRatingServiceImpl(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    @Override
    public void getAverageRatingByTaskId(AverageRatingServiceOuterClass.AverageRatingRequest request, StreamObserver<AverageRatingServiceOuterClass.AverageRatingResponse> responseObserver) {
        System.out.println(request); //as log

        Double averageRating = taskRepository.getAverageRatingByTaskId(request.getTaskId());

        AverageRatingServiceOuterClass.AverageRatingResponse response =
                AverageRatingServiceOuterClass.AverageRatingResponse
                .newBuilder()
                .setAverageRating(averageRating)
                .build();
        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }
}
