package edu.icet.demo.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalTime;

@AllArgsConstructor
@NoArgsConstructor
@Entity
@Getter
@Setter
@ToString
@Table(name = "attendance")
public class AttendanceEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDate date;
    private String status;
    private LocalTime checkInTime;
    private LocalTime checkOutTime;
    private Integer hoursWorked;
    private String shift;

    @OneToOne
    @JoinColumn(name = "employee_id", referencedColumnName = "id")
    @JsonManagedReference
    private EmployeeEntity employee;
}
