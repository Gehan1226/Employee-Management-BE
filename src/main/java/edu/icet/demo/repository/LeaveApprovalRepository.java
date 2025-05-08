package edu.icet.demo.repository;

import edu.icet.demo.entity.LeaveBalanceEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LeaveApprovalRepository extends JpaRepository<LeaveBalanceEntity, Long> {
}
