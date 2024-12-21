package edu.icet.demo.dto;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;

@Data
@Entity
public class UserDTO {
    @Id
    private int id;
    private String userName;
    private String password;
    private String role;
}