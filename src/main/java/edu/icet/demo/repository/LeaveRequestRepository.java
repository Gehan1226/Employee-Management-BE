package edu.icet.demo.repository;

import edu.icet.demo.entity.LeaveRequestEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LeaveRequestRepository extends JpaRepository<LeaveRequestEntity, Long> {

    List<LeaveRequestEntity> findByEmployeeId(Long employeeId);
}
