package ru.labs.grading;

import com.google.protobuf.Empty;
import io.grpc.stub.StreamObserver;
import org.lognet.springboot.grpc.GRpcService;
import org.springframework.beans.factory.annotation.Autowired;
import ru.labs.grading.repositories.TaskRepository;

import java.util.List;

@GRpcService
public class MinRatingServiceImpl extends MinRatingServiceGrpc.MinRatingServiceImplBase {

    private final TaskRepository taskRepository;

    @Autowired
    public MinRatingServiceImpl(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    @Override
    public void getAverageRatingByTaskId(Empty request, StreamObserver<MinRatingServiceOuterClass.MinRatingResponse> responseObserver) {
        List<String> minRatingList = taskRepository.getMinRatingList();

        MinRatingServiceOuterClass.MinRatingResponse response =
                MinRatingServiceOuterClass.MinRatingResponse
                        .newBuilder()
                        .addAllTaskId(minRatingList)
                        .build();

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }
}
