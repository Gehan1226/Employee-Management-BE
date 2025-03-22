package edu.icet.demo.repository;

import edu.icet.demo.entity.RoleEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RoleRepository  extends JpaRepository<RoleEntity,Long> {
    List<RoleEntity> findByDepartmentId(Long id);

    @Query("SELECT d FROM RoleEntity d WHERE " +
            "(:searchTerm IS NULL OR LOWER(d.name) LIKE LOWER(CONCAT('%', :searchTerm, '%')))")
    Page<RoleEntity> findAllWithSearch(@Param("searchTerm") String searchTerm, Pageable pageable);

    boolean existsByName(String name);
}