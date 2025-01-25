package edu.icet.demo.repository;

import edu.icet.demo.entity.DepartmentEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface DepartmentRepository extends JpaRepository<DepartmentEntity, Long> {

    @Query("SELECT d FROM DepartmentEntity d WHERE " +
            "(:searchTerm IS NULL OR LOWER(d.name) LIKE LOWER(CONCAT('%', :searchTerm, '%')) " +
            "OR LOWER(d.manager) LIKE LOWER(CONCAT('%', :searchTerm, '%')))")
    Page<DepartmentEntity> findAllWithSearch(@Param("searchTerm") String searchTerm, Pageable pageable);

}