package ru.labs.grading.dao;

import java.util.List;
import java.util.UUID;

public class TaskDAO {
    private String taskId;
    private String developerFullName;
    private String pathFile;
    private List<AppraiserDAO> appraiserDAOList;

    public TaskDAO(String developerFullName, String pathFile) {
        this.taskId = UUID.randomUUID().toString();
        this.developerFullName = developerFullName;
        this.pathFile = pathFile;
    }

    public String getTaskId() {
        return taskId;
    }

    //    public TaskDAO(String pathFile, String developerFullName) {
//
//    }
}
