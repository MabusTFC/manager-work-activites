package com.example.managerworkactivites.dto;

import com.example.managerworkactivites.domain.ActionType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ActionResponse {

    private Long id;

    private ActionType type;

    private Integer startTime;

    private Integer endTime;

    private String description;

    private LocalDateTime createdAt;


    public Integer getDurationSeconds() {
        if (startTime != null && endTime != null) {
            return endTime - startTime;
        }
        return 0;
    }

    public Integer getDurationMinutes() {
        Integer duration = getDurationSeconds();
        return duration / 60;
    }
}