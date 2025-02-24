package edu.icet.demo.repository;

import edu.icet.demo.entity.TaskEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface TaskRepository extends JpaRepository<TaskEntity, Long> {

    @Query("SELECT t FROM TaskEntity t WHERE " +
            "(:searchTerm IS NULL OR " +
            "LOWER(t.taskDescription) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
            "LOWER(t.status) LIKE LOWER(CONCAT('%', :searchTerm, '%')))")
    Page<TaskEntity> findAllWithSearch(@Param("searchTerm") String searchTerm, Pageable pageable);
}
