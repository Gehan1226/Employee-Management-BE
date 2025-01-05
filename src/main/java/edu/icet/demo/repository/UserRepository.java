package edu.icet.demo.repository;

import edu.icet.demo.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Integer> {

    UserEntity findByUserName(String username);

    @Query("SELECT new edu.icet.demo.entity.UserEntity(u.id, u.userName, u.email, u.role, u.enabled) " +
            "FROM UserEntity u WHERE u.enabled = false")
    List<UserEntity> findByEnabledFalse();
}