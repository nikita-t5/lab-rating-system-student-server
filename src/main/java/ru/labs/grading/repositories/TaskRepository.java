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
    public void saveFile(String taskId, String developerFullName, String pathFile) {
        TaskDAO taskDAO = new TaskDAO(taskId, developerFullName, pathFile);
        taskDaoList.add(taskDAO);
    }

    public String getPathFileByTaskId(String taskId) {
        for (TaskDAO taskDAO : taskDaoList) {
            if (taskDAO.getTaskId().equals(taskId)) {
                return taskDAO.getPathFile();
            }
        }
        throw new RuntimeException("taskId does not exist on the server");
    }


}
