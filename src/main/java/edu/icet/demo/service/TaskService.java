package edu.icet.demo.service;

import edu.icet.demo.dto.Task;

public interface TaskService {
    void addTask(Task task);
    void updateById(Long id, Task task);
    void deleteById(Long id);
}