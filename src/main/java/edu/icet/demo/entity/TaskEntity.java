package edu.icet.demo.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Entity
@Table(name = "task")
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class TaskEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String taskDescription;
    private LocalDate assignedDate;
    private LocalTime assignedTime;
    private LocalDate dueDate;
    private LocalTime dueTime;
    private String status;

    @ManyToOne
    @JoinColumn(name = "manager_id")
    private ManagerEntity manager;

    @ManyToMany
    @JoinTable(
            name = "task_employee",
            joinColumns = @JoinColumn(name = "task_id"),
            inverseJoinColumns = @JoinColumn(name = "employee_id")
    )
    private List<EmployeeEntity> employeeList;
}
