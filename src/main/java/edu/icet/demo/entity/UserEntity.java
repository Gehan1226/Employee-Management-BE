package edu.icet.demo.entity;

import edu.icet.demo.dto.enums.SecurityAuthorities;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@NoArgsConstructor
@Entity
@Table(name = "user")
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(unique = true, nullable = false)
    private String userName;
    private String password;

    @Column(unique = true, nullable = false)
    private String email;

    @ManyToMany
    @JoinTable(
            name = "user_role_association",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "user_role_id")
    )
    private List<UserRoleEntity> roleList;

    private boolean enabled;

    private LocalDate createdDate;

    public UserEntity(int id, String userName, String email, List<UserRoleEntity> roleList, boolean enabled) {
        this.id = id;
        this.userName = userName;
        this.email = email;
        this.roleList = roleList;
        this.enabled = enabled;
    }
}
