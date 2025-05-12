package edu.icet.demo.repository;

import edu.icet.demo.entity.LeaveApprovalEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LeaveApprovalRepository extends JpaRepository<LeaveApprovalEntity, Long> {
}
