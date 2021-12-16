package ru.labs.grading.dao;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class AppraiserDAO {
    private final String appraiserFullName;
    private final Integer markFromAppraiser;
}
