package edu.icet.demo.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Entity
@Getter
@Setter
@Table(name = "employee")
public class EmployeeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private LocalDate dob;
    private String phoneNumber;
    private String gender;

    @ManyToOne
    @JoinColumn(name = "department_id")
    private DepartmentEntity department;

    @ManyToOne
    @JoinColumn(name = "role_id")
    private RoleEntity role;

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "address_id", referencedColumnName = "id")
    private AddressEntity address;
}