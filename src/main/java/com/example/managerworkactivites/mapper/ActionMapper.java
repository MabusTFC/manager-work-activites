package com.example.managerworkactivites.mapper;

import com.example.managerworkactivites.domain.Action;
import com.example.managerworkactivites.dto.ActionRequest;
import com.example.managerworkactivites.dto.ActionResponse;
import org.springframework.stereotype.Component;
import java.util.List;
import java.util.stream.Collectors;


@Component
public class ActionMapper {

    public Action toEntity(ActionRequest request) {
        if (request == null) {
            return null;
        }

        return Action.builder()
                .type(request.getType())
                .startTime(request.getStartTime())
                .endTime(request.getEndTime())
                .description(request.getDescription())
                .build();
    }

    public ActionResponse toResponse(Action action) {
        if (action == null) {
            return null;
        }

        return ActionResponse.builder()
                .id(action.getId())
                .type(action.getType())
                .startTime(action.getStartTime())
                .endTime(action.getEndTime())
                .description(action.getDescription())
                .createdAt(action.getCreatedAt())
                .build();
    }


    public List<ActionResponse> toResponseList(List<Action> actions) {
        return actions.stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }


    public Action updateFromRequest(ActionRequest request, Action action) {
        if (request == null) {
            return action;
        }

        if (request.getType() != null) {
            action.setType(request.getType());
        }
        if (request.getStartTime() != null) {
            action.setStartTime(request.getStartTime());
        }
        if (request.getEndTime() != null) {
            action.setEndTime(request.getEndTime());
        }
        if (request.getDescription() != null) {
            action.setDescription(request.getDescription());
        }

        return action;
    }
}