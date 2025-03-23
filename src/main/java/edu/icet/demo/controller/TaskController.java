package edu.icet.demo.controller;

import edu.icet.demo.dto.task.TaskCreateRequest;
import edu.icet.demo.dto.response.PaginatedResponse;
import edu.icet.demo.dto.response.SuccessResponse;
import edu.icet.demo.service.TaskService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/task")
@RequiredArgsConstructor
@CrossOrigin
@Validated
public class TaskController {

    private final TaskService taskService;

    @PostMapping()
    public SuccessResponse addTask(@RequestBody TaskCreateRequest taskCreateRequest) {
        taskService.addTask(taskCreateRequest);
        return SuccessResponse.builder().status(HttpStatus.OK.value()).message("Task added successfully!").build();
    }

    @PatchMapping("/{id}")
    public SuccessResponse updateById(@PathVariable Long id, @RequestBody TaskCreateRequest taskCreateRequest) {
        taskService.updateById(id, taskCreateRequest);
        return SuccessResponse.builder().status(HttpStatus.OK.value()).message("Task updated successfully!").build();
    }

    @DeleteMapping("/{id}")
    public SuccessResponse deleteById(@PathVariable Long id) {
        taskService.deleteById(id);
        return SuccessResponse.builder().status(HttpStatus.OK.value()).message("Task deleted successfully!").build();
    }

    @GetMapping("/get-all-paginated")
    public PaginatedResponse<TaskCreateRequest> getAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String searchTerm) {

        Pageable pageable = PageRequest.of(page, size);
        return taskService.getAllWithPagination(pageable, searchTerm);
    }

}
