package edu.icet.demo.controller;

import edu.icet.demo.dto.Task;
import edu.icet.demo.dto.response.SuccessResponse;
import edu.icet.demo.service.TaskService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/task")
@RequiredArgsConstructor
@CrossOrigin
@Validated
public class TaskController {

    TaskService taskService;

    @PostMapping()
    public SuccessResponse addTask(Task task) {
        taskService.addTask(task);
        return SuccessResponse.builder().status(HttpStatus.OK.value()).message("Task added successfully!").build();
    }

    @PutMapping("/{id}")
    public SuccessResponse updateById(@PathVariable Long id, @RequestBody Task task) {
        taskService.updateById(id, task);
        return SuccessResponse.builder().status(HttpStatus.OK.value()).message("Task updated successfully!").build();
    }

    @DeleteMapping("/{id}")
    public SuccessResponse deleteById(@PathVariable Long id) {
        taskService.deleteById(id);
        return SuccessResponse.builder().status(HttpStatus.OK.value()).message("Task deleted successfully!").build();
    }

}
