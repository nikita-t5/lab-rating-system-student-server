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
    public String saveFile(String pathFile, String developerFullName) {
        TaskDAO taskDAO = new TaskDAO(developerFullName, pathFile);
        taskDaoList.add(taskDAO);
        return taskDAO.getTaskId();
    }

}
