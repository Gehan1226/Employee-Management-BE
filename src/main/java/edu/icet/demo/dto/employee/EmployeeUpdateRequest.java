package edu.icet.demo.dto.employee;

import edu.icet.demo.dto.address.Address;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EmployeeUpdateRequest {
    private Long id;

    private String firstName;

    private String lastName;

    @Email(message = "Email should be valid.")
    private String email;

    @Past(message = "Date of birth must be in the past.")
    private LocalDate dob;

    @Pattern(regexp = "^\\d{10}$", message = "Phone number must be exactly 10 digits.")
    private String phoneNumber;

    @Pattern(regexp = "^(Male|Female|Other)$", message = "Gender must be Male, Female, or Other.")
    private String gender;

    private Long departmentId;

    private Long roleId;

    private Address address;
}
