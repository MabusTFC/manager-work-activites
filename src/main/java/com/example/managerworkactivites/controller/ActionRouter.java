package com.example.managerworkactivites.router;

import com.example.managerworkactivites.dto.ActionRequest;
import com.example.managerworkactivites.dto.ActionResponse;
import com.example.managerworkactivites.service.ActionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;


@RestController
@RequestMapping("/api/intervals")
@RequiredArgsConstructor
@Tag(name = "Intervals", description = "Управление интервалами активности (работа, перерывы)")
public class ActionRouter {

    private final ActionService actionService;

    /**
     * GET /api/intervals
     * Получить все сохранённые интервалы
     */
    @GetMapping
    @Operation(
            summary = "Получить все интервалы",
            description = "Возвращает список всех сохранённых интервалов активности"
    )
    @ApiResponse(
            responseCode = "200",
            description = "Список интервалов успешно получен",
            content = @Content(schema = @Schema(implementation = ActionResponse.class))
    )
    public ResponseEntity<List<ActionResponse>> getAllIntervals() {
        List<ActionResponse> intervals = actionService.getAllActions();
        return ResponseEntity.ok(intervals);
    }

    /**
     * GET /api/intervals/{id}
     * Получить интервал по ID
     */
    @GetMapping("/{id}")
    @Operation(
            summary = "Получить интервал по ID",
            description = "Возвращает конкретный интервал по его идентификатору"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Интервал найден",
                    content = @Content(schema = @Schema(implementation = ActionResponse.class))
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Интервал не найден"
            )
    })
    public ResponseEntity<ActionResponse> getIntervalById(
            @Parameter(description = "ID интервала", example = "1")
            @PathVariable Long id) {
        ActionResponse interval = actionService.getActionById(id);
        return ResponseEntity.ok(interval);
    }

    /**
     * POST /api/intervals
     * Добавить новый интервал
     */
    @PostMapping
    @Operation(
            summary = "Создать новый интервал",
            description = "Добавляет новый интервал активности в систему"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201",
                    description = "Интервал успешно создан",
                    content = @Content(schema = @Schema(implementation = ActionResponse.class))
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Невалидные данные"
            )
    })
    public ResponseEntity<ActionResponse> createInterval(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Данные нового интервала",
                    required = true,
                    content = @Content(schema = @Schema(implementation = ActionRequest.class))
            )
            @RequestBody ActionRequest request) {
        ActionResponse interval = actionService.createAction(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(interval);
    }

    /**
     * PUT /api/intervals/{id}
     * Обновить интервал
     */
    @PutMapping("/{id}")
    @Operation(
            summary = "Обновить интервал",
            description = "Обновляет данные существующего интервала"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Интервал успешно обновлён",
                    content = @Content(schema = @Schema(implementation = ActionResponse.class))
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Интервал не найден"
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Невалидные данные"
            )
    })
    public ResponseEntity<ActionResponse> updateInterval(
            @Parameter(description = "ID интервала", example = "1")
            @PathVariable Long id,
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Обновлённые данные интервала",
                    required = true
            )
            @RequestBody ActionRequest request) {
        ActionResponse interval = actionService.updateAction(id, request);
        return ResponseEntity.ok(interval);
    }

    /**
     * DELETE /api/intervals/{id}
     * Удалить интервал
     */
    @DeleteMapping("/{id}")
    @Operation(
            summary = "Удалить интервал",
            description = "Удаляет интервал из системы"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "204",
                    description = "Интервал успешно удалён"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Интервал не найден"
            )
    })
    public ResponseEntity<Void> deleteInterval(
            @Parameter(description = "ID интервала", example = "1")
            @PathVariable Long id) {
        actionService.deleteAction(id);
        return ResponseEntity.noContent().build();
    }
}