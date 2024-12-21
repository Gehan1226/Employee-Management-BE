package edu.icet.demo.dto;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;

@Data
public class UserDTO {
    private String userName;
    private String password;
    private String role;
}