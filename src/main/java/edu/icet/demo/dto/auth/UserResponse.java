package edu.icet.demo.dto.auth;

import edu.icet.demo.dto.employee.EmployeeResponse;
import edu.icet.demo.dto.enums.SecurityAuthorities;
import lombok.*;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserResponse {
    private String userName;
    private String email;
    private List<SecurityAuthorities> roleList;
    private boolean enabled;
    private LocalDate createdDate;
    private EmployeeResponse employee;
}