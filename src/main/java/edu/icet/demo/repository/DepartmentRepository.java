package edu.icet.demo.repository;

import edu.icet.demo.dto.department.DepartmentNameAndEmployeeCount;
import edu.icet.demo.entity.DepartmentEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DepartmentRepository extends JpaRepository<DepartmentEntity, Long> {

    boolean existsByName(String name);

    @Query("SELECT d FROM DepartmentEntity d WHERE " +
            "(:searchTerm IS NULL OR LOWER(d.name) LIKE LOWER(CONCAT('%', :searchTerm, '%')))")
    Page<DepartmentEntity> findAllWithSearch(@Param("searchTerm") String searchTerm, Pageable pageable);

    @Query("SELECT NEW edu.icet.demo.dto.department.DepartmentNameAndEmployeeCount(d.name, d.employeeCount) " +
            "FROM DepartmentEntity d")
    List<DepartmentNameAndEmployeeCount> findAllDepartmentNamesAndEmployeeCounts();

    @Modifying
    @Query("UPDATE DepartmentEntity d SET d.manager = NULL WHERE d.manager.id " +
            "IN (SELECT m.id FROM ManagerEntity m WHERE m.employee.id = :employeeId)")
    void removeManagerByEmployeeId(@Param("employeeId") Long employeeId);
}