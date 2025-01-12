package edu.icet.demo.repository;

import edu.icet.demo.dto.enums.SecurityAuthorities;
import edu.icet.demo.entity.UserEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;


@Repository
public interface UserRepository extends JpaRepository<UserEntity, Integer> {

    UserEntity findByUserName(String username);

    UserEntity findByEmail(String email);

    @Query("SELECT new edu.icet.demo.entity.UserEntity(u.id, u.userName, u.email, u.role, u.enabled) " +
            "FROM UserEntity u WHERE u.enabled = false")
    Page<UserEntity> findByEnabledFalse(Pageable pageable);

    @Modifying
    @Transactional
    @Query("UPDATE edu.icet.demo.entity.UserEntity u SET u.role = :role, u.enabled = :enabled WHERE u.email = :email")
    int updateUserRoleAndEnabled(@Param("email") String email,
                                 @Param("role") SecurityAuthorities role,
                                 @Param("enabled") boolean enabled);

    @Modifying
    @Transactional
    @Query("DELETE FROM edu.icet.demo.entity.UserEntity u WHERE u.email = :email")
    int deleteUserByEmail(@Param("email") String email);
}