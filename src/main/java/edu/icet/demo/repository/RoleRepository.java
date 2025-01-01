package edu.icet.demo.repository;

import edu.icet.demo.entity.RoleEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RoleRepository  extends JpaRepository<RoleEntity,Long> {
    List<RoleEntity> findByDepartmentId(Long id);
}