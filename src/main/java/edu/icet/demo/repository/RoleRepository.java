package edu.icet.demo.repository;

import edu.icet.demo.entity.RoleEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository  extends JpaRepository<RoleEntity,Long> {
}