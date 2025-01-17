package edu.icet.demo.entity;

import edu.icet.demo.dto.enums.SecurityAuthorities;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
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

    private LocalDate createdDate;

    public UserEntity(int id, String userName, String email, SecurityAuthorities role, boolean enabled) {
        this.id = id;
        this.userName = userName;
        this.email = email;
        this.role = role;
        this.enabled = enabled;
    }
}
