package edu.icet.demo.repository;

import edu.icet.demo.entity.ManagerEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ManagerRepository extends JpaRepository<ManagerEntity, Long> {
    Optional<ManagerEntity> findByEmployeeId(Long employeeId);
}
