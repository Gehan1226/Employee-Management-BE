package edu.icet.demo.repository;

import edu.icet.demo.entity.ManagerEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ManagerRepository extends JpaRepository<ManagerEntity, Long> {
}
