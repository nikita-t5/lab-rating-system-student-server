package ru.labs.grading.services;

import io.grpc.stub.StreamObserver;
import lombok.extern.slf4j.Slf4j;
import org.lognet.springboot.grpc.GRpcService;
import org.springframework.beans.factory.annotation.Autowired;
import ru.labs.grading.PostRatingServiceGrpc;
import ru.labs.grading.PostRatingServiceOuterClass;
import ru.labs.grading.repositories.TaskRepository;

@Slf4j
@GRpcService
public class PostRatingServiceImpl extends PostRatingServiceGrpc.PostRatingServiceImplBase {

    private final TaskRepository taskRepository;

    @Autowired
    public PostRatingServiceImpl(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    @Override
    public void postRatingByEvaluationDto(PostRatingServiceOuterClass.PostRatingRequest request, StreamObserver<PostRatingServiceOuterClass.PostRatingResponse> responseObserver) {
        log.info("Start setting rating for taskID :: {}", request.getTaskId());
        final String taskId = request.getTaskId();
        final String appraiserFullName = request.getAppraiserFullName();
        final Integer rating = request.getRating();

        final String taskIdResponse = taskRepository.setRatingByEvaluationDto(taskId, appraiserFullName, rating);

        PostRatingServiceOuterClass.PostRatingResponse response =
                PostRatingServiceOuterClass.PostRatingResponse
                        .newBuilder()
                        .setTaskIdResponse(taskIdResponse)
                        .build();
        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }
}
