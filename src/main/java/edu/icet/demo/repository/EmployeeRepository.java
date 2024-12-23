package edu.icet.demo.repository;

import edu.icet.demo.entity.EmployeeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EmployeeRepository extends JpaRepository<EmployeeEntity,Long> {
    EmployeeEntity findByFirstName(String firstName);
    EmployeeEntity findByEmail(String email);
    void deleteByEmail(String email);
    boolean existsByEmail(String email); // Optional for validation
}