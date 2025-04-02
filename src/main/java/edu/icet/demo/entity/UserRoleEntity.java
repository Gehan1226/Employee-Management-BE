package edu.icet.demo.entity;

import edu.icet.demo.dto.enums.SecurityAuthorities;
import jakarta.persistence.*;
import lombok.*;


@ToString
@Getter
@Setter
@Entity
@Table(name = "user_role")
@AllArgsConstructor
@NoArgsConstructor
public class UserRoleEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private  Long id;

    @Enumerated(EnumType.STRING)
    private SecurityAuthorities name;

    private String description;
}
