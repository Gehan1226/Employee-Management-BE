package edu.icet.demo.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.icet.demo.dto.Task;
import edu.icet.demo.dto.response.PaginatedResponse;
import edu.icet.demo.entity.EmployeeEntity;
import edu.icet.demo.entity.TaskEntity;
import edu.icet.demo.exception.DataIntegrityException;
import edu.icet.demo.exception.DataNotFoundException;
import edu.icet.demo.exception.UnexpectedException;
import edu.icet.demo.repository.TaskRepository;
import edu.icet.demo.service.TaskService;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TaskServiceImpl implements TaskService {

    private final TaskRepository taskRepository;
    private final ObjectMapper mapper;

    @Override
    public void addTask(Task task) {
        try {
            taskRepository.save(mapper.convertValue(task, TaskEntity.class));
        }catch (DataIntegrityViolationException ex) {
            throw new DataIntegrityException(
                    "Database constraint violation. Please check that all provided values are valid and unique.");
        } catch (Exception ex) {
            throw new UnexpectedException("An unexpected error occurred while saving the task");
        }
    }

    @Override
    public void updateById(Long id, Task task) {
         TaskEntity taskEntity = taskRepository.findById(id)
                .orElseThrow(() -> new DataNotFoundException(
                        String.format("Task with ID %d does not exist in the system.", id)));

        List<EmployeeEntity> employeeList = new ArrayList<>();

        task.getEmployeeCreateRequestList().forEach(employee ->
                employeeList.add(mapper.convertValue(employee, EmployeeEntity.class)));
        try {
            taskEntity.setTaskDescription(task.getTaskDescription());
            taskEntity.setAssignedTime(task.getAssignedTime());
            taskEntity.setAssignedDate(task.getDueDate());
            taskEntity.setEmployeeList(employeeList);
            taskRepository.save(taskEntity);
        } catch (DataIntegrityViolationException ex) {
            throw new DataIntegrityException(
                    "Database constraint violation. Please check that all provided values are valid and unique.");
        } catch (Exception ex) {
            throw new UnexpectedException("An unexpected error occurred while updating the task");
        }
    }

    @Override
    public void deleteById(Long id) {
        try {
            if (taskRepository.existsById(id)) {
                taskRepository.deleteById(id);
                return;
            }
        } catch (Exception e) {
            throw new UnexpectedException("An unexpected error occurred while deleting the task");
        }
        throw new DataIntegrityException(String.format("Task with ID %d does not exist in the system.", id));
    }

    @Override
    public PaginatedResponse<Task> getAllWithPagination(Pageable pageable, String searchTerm) {
        try {
            List<Task> taskList = new ArrayList<>();
            Page<TaskEntity> response = taskRepository.findAllWithSearch(searchTerm, pageable);
            response.forEach(taskEntity ->
                    taskList.add(mapper.convertValue(taskEntity, Task.class)));
            return new PaginatedResponse<>(
                    HttpStatus.OK.value(),
                    taskList.isEmpty() ? "No departments found!" : "Departments retrieved.",
                    taskList,
                    response.getTotalPages(),
                    response.getTotalElements(),
                    response.getNumber()
            );
        } catch (Exception e) {
            throw new UnexpectedException("An unexpected error occurred while retrieving tasks");
        }
    }
}
