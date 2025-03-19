package edu.icet.demo.dto.employee;

import edu.icet.demo.dto.address.Address;
import edu.icet.demo.dto.role.RoleResponse;
import edu.icet.demo.dto.department.DepartmentResponse;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EmployeeResponse {
    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private LocalDate dob;
    private String phoneNumber;
    private String gender;
    private DepartmentResponse department;
    private RoleResponse role;
    private Address address;
}
