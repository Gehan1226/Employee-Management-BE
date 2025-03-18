package edu.icet.demo.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Entity
@Getter
@Setter
@ToString
@Table(name = "department")
public class DepartmentEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String name;

    @Column(nullable = false)
    private String responsibility;

    private Integer employeeCount;

    @OneToOne
    @JoinColumn(name = "manager_id", referencedColumnName = "id")
    @JsonManagedReference
    private ManagerEntity manager;

    public DepartmentEntity(Long id) {
        this.id = id;
    }
}