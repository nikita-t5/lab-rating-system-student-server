package ru.labs.grading.services;

import com.google.protobuf.Empty;
import io.grpc.stub.StreamObserver;
import lombok.extern.slf4j.Slf4j;
import org.lognet.springboot.grpc.GRpcService;
import org.springframework.beans.factory.annotation.Autowired;
import ru.labs.grading.TaskServiceGrpc;
import ru.labs.grading.TaskServiceOuterClass;
import ru.labs.grading.dao.TaskDAO;
import ru.labs.grading.repositories.TaskRepository;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@GRpcService
public class TaskServiceImpl extends TaskServiceGrpc.TaskServiceImplBase {

    private final TaskRepository taskRepository;

    @Autowired
    public TaskServiceImpl(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    @Override
    public void getAllTask(Empty request, StreamObserver<TaskServiceOuterClass.TaskListResponse> responseObserver) {
        log.info("Start getting all task");

        final List<TaskDAO> allTask = taskRepository.getTaskDaoList();
        final List<TaskServiceOuterClass.Task> taskListResponse = new ArrayList<>();

        for (TaskDAO taskDAO : allTask) {
            final String taskId = taskDAO.getTaskId();
            final String developerFullName = taskDAO.getDeveloperFullName();
            TaskServiceOuterClass.Task task = TaskServiceOuterClass.Task
                    .newBuilder()
                    .setTaskId(taskId)
                    .setDeveloperFullName(developerFullName)
                    .build();
            taskListResponse.add(task);
        }

        TaskServiceOuterClass.TaskListResponse response =
                TaskServiceOuterClass.TaskListResponse
                        .newBuilder()
                        .addAllTask(taskListResponse)
                        .build();
        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }
}
