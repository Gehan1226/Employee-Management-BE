package edu.icet.demo.dto;

import jakarta.validation.constraints.*;
import lombok.*;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Employee {

    private Long id;

    @NotBlank(message = "First name is required.")
    private String firstName;

    @NotBlank(message = "Last name is required.")
    private String lastName;

    @Email(message = "Email should be valid.")
    private String email;

    @NotNull(message = "Date of birth is required.")
    @Past(message = "Date of birth must be in the past.")
    private Date dob;

    @Pattern(regexp = "^[0-9]{10}$", message = "Phone number must be 10 digits.")
    private String phoneNumber;

    @NotBlank(message = "Gender is required.")
    @Pattern(regexp = "^(Male|Female|Other)$", message = "Gender must be Male, Female, or Other.")
    private String gender;

    @NotNull(message = "Department ID is required.")
    private Long departmentId;

    @NotNull(message = "Role ID is required.")
    private Long roleId;

    @NotNull(message = "Address is required.")
    private Address address;
}