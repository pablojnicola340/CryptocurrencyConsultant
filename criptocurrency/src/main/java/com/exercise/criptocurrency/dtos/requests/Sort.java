package com.exercise.criptocurrency.dtos.requests;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
public class Sort {
    private String field;
    private Direction direction;

    public Sort(String field, Direction direction) {
        this.field = field;
        this.direction = direction;
    }
    public enum Direction {
        ASC, DESC
    }
}
