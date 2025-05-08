package edu.icet.demo.repository;

import edu.icet.demo.entity.LeaveTypeEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LeaveTypeRepository extends JpaRepository<LeaveTypeEntity, Long> {
}
