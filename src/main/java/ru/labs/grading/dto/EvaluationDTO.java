package ru.labs.grading.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class EvaluationDTO {
    private String taskId;
    private String appraiserFullName;
    private Integer rating;
}