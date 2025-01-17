package edu.icet.demo.dto.auth;


import edu.icet.demo.dto.enums.SecurityAuthorities;
import jakarta.validation.constraints.*;
import lombok.Data;

import java.time.LocalDate;

@Data
public class UserDTO {

    @NotBlank(message = "Username cannot be blank")
    @Size(min = 3, max = 50, message = "Username must be between 3 and 50 characters")
    private String userName;

    @NotBlank(message = "Password cannot be blank")
    @Size(min = 8, max = 20, message = "Password must be between 8 and 20 characters")
    @Pattern(
            regexp = "^(?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=]).*$",
            message = "Password must contain at" +
                    " least one digit, one lowercase, one uppercase letter, and one special character"
    )
    private String password;

    @NotBlank(message = "Email cannot be blank")
    @Email(message = "Email should be valid")
    private String email;

    @NotNull(message = "Role cannot be null")
    private SecurityAuthorities role;

    private boolean enabled;

    private LocalDate createdDate;
}