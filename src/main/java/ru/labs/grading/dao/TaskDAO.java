package ru.labs.grading.dao;

import lombok.Data;

import java.util.ArrayList;
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
        this.appraiserDAOList = new ArrayList<>();
    }

    public void addAppraiserDAO(String appraiserFullName, Integer markFromAppraiser) {
        AppraiserDAO appraiserDAO = new AppraiserDAO(appraiserFullName, markFromAppraiser);
        appraiserDAOList.add(appraiserDAO);
    }
}
