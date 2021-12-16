package ru.labs.grading;

import io.grpc.stub.StreamObserver;
import org.lognet.springboot.grpc.GRpcService;
import org.springframework.beans.factory.annotation.Autowired;
import ru.labs.grading.repositories.TaskRepository;

@GRpcService
public class PostRatingServiceImpl extends PostRatingServiceGrpc.PostRatingServiceImplBase {

    private final TaskRepository taskRepository;

    @Autowired
    public PostRatingServiceImpl(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    @Override
    public void postRatingByEvaluationDto(PostRatingServiceOuterClass.PostRatingRequest request, StreamObserver<PostRatingServiceOuterClass.PostRatingResponse> responseObserver) {
        System.out.println(request); //as log
        String taskId = request.getTaskId();
        String appraiserFullName = request.getAppraiserFullName();
        Integer rating = request.getRating();

        String taskIdResponse = taskRepository.setRatingByEvaluationDto(taskId, appraiserFullName,rating);

        PostRatingServiceOuterClass.PostRatingResponse response =
                PostRatingServiceOuterClass.PostRatingResponse
                        .newBuilder()
                        .setTaskIdResponse(taskIdResponse)
                        .build();
        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }
}
