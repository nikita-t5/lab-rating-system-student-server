package ru.labs.grading.repositories;

import org.springframework.stereotype.Repository;
import ru.labs.grading.dao.TaskDAO;

import java.util.List;

@Repository//????
public class TaskRepository {

    private final List<TaskDAO> taskDaoList;

    public TaskRepository(List<TaskDAO> taskDaoList) {
        this.taskDaoList = taskDaoList;
    }

    //методы по манипуляции

    //загр файл своей работы
    public void saveFileMetadata(String taskId, String developerFullName, String fileName) {
        TaskDAO taskDAO = new TaskDAO(taskId, developerFullName, fileName);
        taskDaoList.add(taskDAO);
    }

    public String getFileNameByTaskId(String taskId) {
        for (TaskDAO taskDAO : taskDaoList) {
            if (taskDAO.getTaskId().equals(taskId)) {
                return taskDAO.getFileName();
            }
        }
        throw new RuntimeException("taskId does not exist on the server");
    }


}
