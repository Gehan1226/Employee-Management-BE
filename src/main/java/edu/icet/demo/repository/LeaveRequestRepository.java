package edu.icet.demo.repository;

import edu.icet.demo.dto.enums.LeaveStatus;
import edu.icet.demo.entity.LeaveRequestEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface LeaveRequestRepository extends JpaRepository<LeaveRequestEntity, Long> {

    List<LeaveRequestEntity> findByEmployeeId(Long employeeId);

    List<LeaveRequestEntity> findByEmployeeIdAndStatus(Long employeeId, LeaveStatus status);

    @Query("""
                SELECT lr
                FROM LeaveRequestEntity lr
                WHERE lr.employee.department.id = :departmentId
                  AND lr.status = :status
            """)
    List<LeaveRequestEntity> findByDepartmentId(
            @Param("departmentId") Long departmentId,
            @Param("status") LeaveStatus status);

}
