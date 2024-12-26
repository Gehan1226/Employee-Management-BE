package edu.icet.demo.entity;

import edu.icet.demo.dto.enums.SecurityAuthorities;
import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(unique = true, nullable = false)
    private String userName;
    private String password;

    @Column(unique = true, nullable = false)
    private String email;

    @Enumerated(EnumType.STRING)
    private SecurityAuthorities role;

    private boolean enabled;
}
