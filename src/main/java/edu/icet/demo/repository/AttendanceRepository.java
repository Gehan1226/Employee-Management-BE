package edu.icet.demo.repository;

import edu.icet.demo.entity.AttendanceEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;

@Repository
public interface AttendanceRepository extends JpaRepository<AttendanceEntity, Long> {

    boolean existsByEmployeeIdAndDate(Long employeeId, LocalDate date);
}
