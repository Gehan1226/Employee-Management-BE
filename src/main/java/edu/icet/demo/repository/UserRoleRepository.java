package edu.icet.demo.repository;

import edu.icet.demo.dto.enums.SecurityAuthorities;
import edu.icet.demo.entity.UserRoleEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRoleRepository extends JpaRepository<UserRoleEntity, Long> {

    UserRoleEntity findByName(String name);

    List<UserRoleEntity> findByNameIn(List<SecurityAuthorities> names);
}
