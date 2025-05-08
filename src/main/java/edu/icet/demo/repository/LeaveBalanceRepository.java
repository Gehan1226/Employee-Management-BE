package edu.icet.demo.repository;

import edu.icet.demo.entity.LeaveBalanceEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface LeaveBalanceRepository extends JpaRepository<LeaveBalanceEntity, Long> {

    Optional<LeaveBalanceEntity> findByEmployeeIdAndLeaveTypeId(Long employeeId, Long leaveTypeId);

}
