package com.example.managerworkactivites.service;

import com.example.managerworkactivites.domain.Action;
import com.example.managerworkactivites.dto.ActionRequest;
import com.example.managerworkactivites.dto.ActionResponse;
import com.example.managerworkactivites.exception.ActionNotFoundException;
import com.example.managerworkactivites.exception.InvalidActionDataException;
import com.example.managerworkactivites.exception.IntervalOverlapException;
import com.example.managerworkactivites.mapper.ActionMapper;
import com.example.managerworkactivites.repository.ActionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;


@Service
@RequiredArgsConstructor
public class ActionServiceImpl implements ActionService {

    private final ActionRepository actionRepository;
    private final ActionMapper actionMapper;

    @Override
    public List<ActionResponse> getAllActions() {
        List<Action> actions = actionRepository.findAll();
        return actionMapper.toResponseList(actions);
    }

    @Override
    public ActionResponse getActionById(Long id) {
        if (id == null || id <= 0) {
            throw new InvalidActionDataException("Invalid action ID");
        }

        Action action = actionRepository.findById(id)
                .orElseThrow(() -> new ActionNotFoundException(
                        "Action with id " + id + " not found"
                ));
        return actionMapper.toResponse(action);
    }

    @Override
    public ActionResponse createAction(ActionRequest request) {
        validateActionRequest(request);

        // Проверяем пересечение с существующими интервалами
        checkForOverlaps(request.getStartTime(), request.getEndTime(), null);

        Action action = actionMapper.toEntity(request);
        Action savedAction = actionRepository.save(action);
        return actionMapper.toResponse(savedAction);
    }

    @Override
    public ActionResponse updateAction(Long id, ActionRequest request) {
        if (id == null || id <= 0) {
            throw new InvalidActionDataException("Invalid action ID");
        }

        validateActionRequest(request);

        Action action = actionRepository.findById(id)
                .orElseThrow(() -> new ActionNotFoundException(
                        "Action with id " + id + " not found"
                ));

        // Проверяем пересечение с существующими интервалами
        // но исключаем сам текущий интервал из проверки
        checkForOverlaps(request.getStartTime(), request.getEndTime(), id);

        actionMapper.updateFromRequest(request, action);
        Action updatedAction = actionRepository.save(action);
        return actionMapper.toResponse(updatedAction);
    }

    @Override
    public void deleteAction(Long id) {
        if (id == null || id <= 0) {
            throw new InvalidActionDataException("Invalid action ID");
        }

        if (!actionRepository.existsById(id)) {
            throw new ActionNotFoundException(
                    "Action with id " + id + " not found"
            );
        }

        actionRepository.deleteById(id);
    }

    /**
     * Проверка на пересечение интервалов
     */
    private void checkForOverlaps(Integer start, Integer end, Long excludeId) {
        List<Action> overlappingActions =
                actionRepository.findOverlappingIntervals(start, end);

        // Исключаем текущий интервал если это обновление
        if (excludeId != null) {
            overlappingActions = overlappingActions.stream()
                    .filter(a -> !a.getId().equals(excludeId))
                    .toList();
        }

        if (!overlappingActions.isEmpty()) {
            StringBuilder message = new StringBuilder();
            message.append("Интервал пересекается с существующим(и): ");

            overlappingActions.forEach(action -> {
                message.append(String.format(
                        "[%d - %d сек (ID: %d)] ",
                        action.getStartTime(),
                        action.getEndTime(),
                        action.getId()
                ));
            });

            throw new IntervalOverlapException(message.toString());
        }
    }

    private void validateActionRequest(ActionRequest request) {
        if (request == null) {
            throw new InvalidActionDataException("Action request cannot be null");
        }

        if (request.getType() == null) {
            throw new InvalidActionDataException("Action type cannot be empty");
        }

        if (request.getStartTime() == null) {
            throw new InvalidActionDataException("Start time cannot be empty");
        }

        if (request.getEndTime() == null) {
            throw new InvalidActionDataException("End time cannot be empty");
        }

        // Проверяем диапазон 0-86400
        if (request.getStartTime() < 0 || request.getStartTime() > 86400) {
            throw new InvalidActionDataException("Start time must be between 0 and 86400 seconds");
        }

        if (request.getEndTime() < 0 || request.getEndTime() > 86400) {
            throw new InvalidActionDataException("End time must be between 0 and 86400 seconds");
        }

        if (request.getStartTime() >= request.getEndTime()) {
            throw new InvalidActionDataException("Start time must be before end time");
        }

        if (request.getDescription() == null || request.getDescription().isBlank()) {
            throw new InvalidActionDataException("Description cannot be empty");
        }
    }
}