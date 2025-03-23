package edu.icet.demo.service;

import edu.icet.demo.dto.task.TaskCreateRequest;
import edu.icet.demo.dto.response.PaginatedResponse;
import edu.icet.demo.dto.task.TaskResponse;
import edu.icet.demo.dto.task.TaskUpdateRequest;
import org.springframework.data.domain.Pageable;

public interface TaskService {

    void addTask(TaskCreateRequest taskCreateRequest);

    void updateById(Long id, TaskUpdateRequest taskCreateRequest);

    void deleteById(Long id);

    PaginatedResponse<TaskResponse> getAllByManagerIdWithPagination(
            Long managerId, Pageable pageable, String searchTerm);
}