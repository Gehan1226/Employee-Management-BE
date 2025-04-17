package edu.icet.demo.repository;

import edu.icet.demo.entity.EmployeeEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EmployeeRepository extends JpaRepository<EmployeeEntity,Long> {

    EmployeeEntity findByFirstName(String firstName);

    boolean existsByEmail(String email);

    @Query("SELECT e FROM EmployeeEntity e WHERE " +
            "(:searchTerm IS NULL OR " +
            "LOWER(e.firstName) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
            "LOWER(e.lastName) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
            "LOWER(e.email) LIKE LOWER(CONCAT('%', :searchTerm, '%')))")
    Page<EmployeeEntity> findAllWithSearch(@Param("searchTerm") String searchTerm, Pageable pageable);

    @Query("SELECT e FROM EmployeeEntity e WHERE " +
            "e.department.id = :departmentId AND (" +
            "LOWER(e.firstName) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
            "LOWER(e.lastName) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
            "LOWER(e.email) LIKE LOWER(CONCAT('%', :searchTerm, '%')))")
    Page<EmployeeEntity> searchByDepartmentAndTerm(@Param("departmentId") Long departmentId,
                                                   @Param("searchTerm") String searchTerm,
                                                   Pageable pageable);


    @Query("SELECT e FROM EmployeeEntity e WHERE e.id NOT IN (SELECT m.employee.id FROM ManagerEntity m)")
    List<EmployeeEntity> findNonManagerEmployees();


}
