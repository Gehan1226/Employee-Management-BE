package edu.icet.demo.dto.department;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;

@AllArgsConstructor
@Data
public class ManagerEmployeeResponse {
    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private LocalDate dob;
    private String phoneNumber;
    private String gender;
}
