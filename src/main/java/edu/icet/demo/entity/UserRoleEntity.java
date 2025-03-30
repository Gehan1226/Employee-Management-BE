package edu.icet.demo.entity;

import jakarta.persistence.*;
import lombok.*;


@ToString
@Entity
@Table(name = "user_role")
@AllArgsConstructor
@NoArgsConstructor
public class UserRoleEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private  Long id;
    private String name;
    private String description;
}
