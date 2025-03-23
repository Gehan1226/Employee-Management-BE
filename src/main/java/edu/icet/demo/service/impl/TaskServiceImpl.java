package edu.icet.demo.service.impl;

import edu.icet.demo.dto.task.TaskCreateRequest;
import edu.icet.demo.dto.response.PaginatedResponse;
import edu.icet.demo.dto.task.TaskUpdateRequest;
import edu.icet.demo.entity.EmployeeEntity;
import edu.icet.demo.entity.ManagerEntity;
import edu.icet.demo.entity.TaskEntity;
import edu.icet.demo.exception.DataIntegrityException;
import edu.icet.demo.exception.DataNotFoundException;
import edu.icet.demo.exception.UnexpectedException;
import edu.icet.demo.repository.ManagerRepository;
import edu.icet.demo.repository.TaskRepository;
import edu.icet.demo.service.TaskService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class TaskServiceImpl implements TaskService {

    private final TaskRepository taskRepository;
    private final ManagerRepository managerRepository;
    private final ModelMapper mapper;

    @Override
    public void addTask(TaskCreateRequest taskCreateRequest) {
        if (!managerRepository.existsById(taskCreateRequest.getManagerId())) {
            throw new DataNotFoundException(
                    String.format("Manager with ID %d does not exist in the system.", taskCreateRequest.getManagerId()));
        }
        try {
            TaskEntity taskEntity = mapper.map(taskCreateRequest, TaskEntity.class);

            List<EmployeeEntity> employeeList = new ArrayList<>();
            taskCreateRequest.getEmployeeIdList().forEach(employeeId ->
                    employeeList.add(EmployeeEntity.builder().id(employeeId).build()));

            taskEntity.setEmployeeList(employeeList);
            taskRepository.save(taskEntity);
        } catch (DataIntegrityViolationException ex) {
            throw new DataIntegrityException(
                    "Database constraint violation. Please check that all provided values are valid and unique.");
        } catch (Exception ex) {
            log.error(ex.getMessage());
            throw new UnexpectedException(
                    "An unexpected error occurred while saving the task. Please check the provided values."
            );
        }
    }

    @Override
    public void updateById(Long id, TaskUpdateRequest taskUpdateRequest) {
        TaskEntity taskEntity = taskRepository.findById(id)
                .orElseThrow(() -> new DataNotFoundException(
                        String.format("Task with ID %d does not exist in the system.", id)));

        try {
            List<EmployeeEntity> employeeList = new ArrayList<>();
            if (taskUpdateRequest.getEmployeeIdList() != null) {
                taskUpdateRequest.getEmployeeIdList().forEach(employeeId ->
                        employeeList.add(EmployeeEntity.builder().id(employeeId).build()));

                taskEntity.setEmployeeList(employeeList);
            }
            taskUpdateRequest.setEmployeeIdList(null);
            mapper.map(taskUpdateRequest, taskEntity);
            taskRepository.save(taskEntity);
        } catch (DataIntegrityViolationException ex) {
            throw new DataIntegrityException(
                    "Database constraint violation. Please check that all provided values are valid and unique.");
        } catch (Exception ex) {
            log.error(ex.getMessage());
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
    public PaginatedResponse<TaskCreateRequest> getAllWithPagination(Pageable pageable, String searchTerm) {
        try {
            List<TaskCreateRequest> taskCreateRequestList = new ArrayList<>();
            Page<TaskEntity> response = taskRepository.findAllWithSearch(searchTerm, pageable);
            response.forEach(taskEntity ->
                    taskCreateRequestList.add(mapper.map(taskEntity, TaskCreateRequest.class)));
            return new PaginatedResponse<>(
                    HttpStatus.OK.value(),
                    taskCreateRequestList.isEmpty() ? "No departments found!" : "Departments retrieved.",
                    taskCreateRequestList,
                    response.getTotalPages(),
                    response.getTotalElements(),
                    response.getNumber()
            );
        } catch (Exception e) {
            throw new UnexpectedException("An unexpected error occurred while retrieving tasks");
        }
    }
}
