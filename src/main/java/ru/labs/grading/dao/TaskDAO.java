package ru.labs.grading.dao;

import lombok.Data;

import java.util.List;
import java.util.UUID;
@Data
public class TaskDAO {
    private String taskId;
    private String developerFullName;
//    private String pathFile;
    private byte[] file;
    private List<AppraiserDAO> appraiserDAOList;

    public TaskDAO(String taskId, String developerFullName, byte[] file) {
        this.taskId = taskId;
        this.developerFullName = developerFullName;
//        this.pathFile = pathFile;
        this.file = file;
    }

//    public String getTaskId() {
//        return taskId;
//    }

    //    public TaskDAO(String pathFile, String developerFullName) {
//
//    }
}
