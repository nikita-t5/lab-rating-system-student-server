package ru.labs.grading;

import io.grpc.stub.StreamObserver;
import org.lognet.springboot.grpc.GRpcService;
import org.springframework.beans.factory.annotation.Autowired;
import ru.labs.grading.dto.EvaluationDTO;
import ru.labs.grading.repositories.TaskRepository;

import java.util.ArrayList;
import java.util.List;

@GRpcService
public class EvaluationTaskServiceImpl extends EvaluationTaskServiceGrpc.EvaluationTaskServiceImplBase {
    private final TaskRepository taskRepository;

    @Autowired
    public EvaluationTaskServiceImpl(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    @Override
    public void getAllEvaluationTask(EvaluationTaskServiceOuterClass.EvaluationRequest request, StreamObserver<EvaluationTaskServiceOuterClass.EvaluationResponse> responseObserver) {
        final String taskIdFromRequest = request.getTaskId();
        final List<EvaluationDTO> evaluationDTOList = taskRepository.getEvaluationDTOListByTaskId(taskIdFromRequest);
        final List<EvaluationTaskServiceOuterClass.Evaluation> evaluationListResponse = new ArrayList<>();

        for (EvaluationDTO evaluationDTO : evaluationDTOList) {
            final String taskId = evaluationDTO.getTaskId();
            final String appraiserFullName = evaluationDTO.getAppraiserFullName();
            final Integer rating = evaluationDTO.getRating();
            EvaluationTaskServiceOuterClass.Evaluation evaluation = EvaluationTaskServiceOuterClass.Evaluation
                    .newBuilder()
                    .setTaskId(taskId)
                    .setAppraiserFullName(appraiserFullName)
                    .setRating(rating)
                    .build();
            evaluationListResponse.add(evaluation);
        }

        EvaluationTaskServiceOuterClass.EvaluationResponse response =
                EvaluationTaskServiceOuterClass.EvaluationResponse
                        .newBuilder()
                        .addAllEvaluation(evaluationListResponse)
                        .build();
        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }
}
