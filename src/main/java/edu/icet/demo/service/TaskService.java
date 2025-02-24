package edu.icet.demo.service;

import edu.icet.demo.dto.Task;
import edu.icet.demo.dto.response.PaginatedResponse;
import org.springframework.data.domain.Pageable;

public interface TaskService {
    void addTask(Task task);
    void updateById(Long id, Task task);
    void deleteById(Long id);
    PaginatedResponse<Task> getAllWithPagination(Pageable pageable, String searchTerm);
}