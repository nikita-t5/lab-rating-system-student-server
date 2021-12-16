package ru.labs.grading.repositories;

import org.springframework.stereotype.Repository;
import ru.labs.grading.dao.AppraiserDAO;
import ru.labs.grading.dao.TaskDAO;

import java.util.ArrayList;
import java.util.Comparator;
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
        return findTaskDaoByTaskId(taskId).getFileName();
    }


    public String setRatingByEvaluationDto(String taskId, String appraiserFullName, Integer rating) {
        TaskDAO taskDAO = findTaskDaoByTaskId(taskId);
        taskDAO.addAppraiserDAO(appraiserFullName, rating);
        return taskDAO.getTaskId();
    }


    public Double getAverageRatingByTaskId(String taskId) {
        TaskDAO taskDAO = findTaskDaoByTaskId(taskId);
        List<AppraiserDAO> appraiserDAOList = taskDAO.getAppraiserDAOList();
        if (appraiserDAOList.size() == 0) {
            throw new RuntimeException("There are no ratings for the takId");
        }
        Double sumRating = 0.0;
        for (AppraiserDAO appraiserDAO : appraiserDAOList) {
            sumRating += appraiserDAO.getMarkFromAppraiser();
        }
        return sumRating / appraiserDAOList.size();
    }


    private TaskDAO findTaskDaoByTaskId(String taskId) {
        for (TaskDAO taskDAO : taskDaoList) {
            if (taskDAO.getTaskId().equals(taskId)) {
                return taskDAO;
            }
        }
        throw new RuntimeException("taskId does not exist on the server");
    }

    public List<String> getMinRatingList() {
        List<String> minRatingList = new ArrayList<>();

        if (taskDaoList.size() < 3) {
            throw new RuntimeException("Not enough appraiser for implementation");
        }
        Comparator<TaskDAO> comparator = Comparator.comparing(obj -> obj.getAppraiserDAOList().size());
        taskDaoList.sort(comparator);
        for (int i = 0; i < 3; i++) {
            minRatingList.add(taskDaoList.get(i).getTaskId());
        }
        return minRatingList;
    }
}
