package edu.icet.demo.service;

import edu.icet.demo.dto.auth.AccessToken;
import edu.icet.demo.dto.auth.UserCreateRequest;
import edu.icet.demo.dto.auth.UserLoginRequest;
import edu.icet.demo.dto.auth.UserResponse;
import edu.icet.demo.dto.response.PaginatedResponse;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;

public interface UserService {
    void addUser(UserCreateRequest userCreateRequest);

    AccessToken authenticateAndGenerateToken(UserLoginRequest userLoginRequest);

    PaginatedResponse<UserCreateRequest> getDisableUsersByOptionalDateRange(
            LocalDate startDate, LocalDate endDate, String searchTerm,Pageable pageable);

    void updateRoleAndEnabled(UserCreateRequest userCreateRequest);

    boolean deleteByEmail(String email);

    UserResponse getUserByName(String name);
}
