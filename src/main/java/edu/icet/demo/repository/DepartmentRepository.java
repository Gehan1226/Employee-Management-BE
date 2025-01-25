package edu.icet.demo.repository;

import edu.icet.demo.entity.DepartmentEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DepartmentRepository extends JpaRepository<DepartmentEntity, Long> {
    Page<DepartmentEntity> findAll(Pageable pageable);
}