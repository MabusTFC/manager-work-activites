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
public class ActionRequest {

    private ActionType type;

    private Integer startTime;

    private Integer endTime;

    private String description;
}