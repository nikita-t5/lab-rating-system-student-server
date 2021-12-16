package ru.labs.grading.dao;

import lombok.Data;

import java.util.List;

@Data
public class TaskDAO {
    private String taskId;
    private String developerFullName;
    private String fileName;
    private List<AppraiserDAO> appraiserDAOList;

    public TaskDAO(String taskId, String developerFullName, String fileName) {
        this.taskId = taskId;
        this.developerFullName = developerFullName;
        this.fileName = fileName;
    }

//    public String getTaskId() {
//        return taskId;
//    }

    //    public TaskDAO(String pathFile, String developerFullName) {
//
//    }
}
