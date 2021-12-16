package ru.labs.grading;

import io.grpc.stub.StreamObserver;
import org.lognet.springboot.grpc.GRpcService;
import org.springframework.beans.factory.annotation.Autowired;
import ru.labs.grading.repositories.TaskRepository;

import java.util.ArrayList;
import java.util.List;

@GRpcService
public class MinRatingServiceImpl extends MinRatingServiceGrpc.MinRatingServiceImplBase {

    private final TaskRepository taskRepository;

    @Autowired
    public MinRatingServiceImpl(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    @Override
    public void getAverageRatingByTaskId(MinRatingServiceOuterClass.Empty request, StreamObserver<MinRatingServiceOuterClass.MinRatingResponse> responseObserver) {
//        int a = 0;
//
//        List<String> arr = new ArrayList<>();
//        arr.add("aaaa");
//        arr.add("bbb");
//        arr.add("ccc");

        //получить List taskId с мин кол оценок

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
