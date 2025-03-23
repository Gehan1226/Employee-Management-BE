package edu.icet.demo.service;

import edu.icet.demo.dto.task.TaskCreateRequest;
import edu.icet.demo.dto.response.PaginatedResponse;
import org.springframework.data.domain.Pageable;

public interface TaskService {

    void addTask(TaskCreateRequest taskCreateRequest);

    void updateById(Long id, TaskCreateRequest taskCreateRequest);

    void deleteById(Long id);

    PaginatedResponse<TaskCreateRequest> getAllWithPagination(Pageable pageable, String searchTerm);
}