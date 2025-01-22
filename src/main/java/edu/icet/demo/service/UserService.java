package edu.icet.demo.service;

import edu.icet.demo.dto.auth.AccessToken;
import edu.icet.demo.dto.auth.UserDTO;
import edu.icet.demo.dto.auth.UserLoginRequest;
import edu.icet.demo.dto.response.PaginatedResponse;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;

public interface UserService {
    UserDTO register(UserDTO userDTO);

    AccessToken verify(UserLoginRequest userLoginRequest);

    PaginatedResponse<UserDTO> getDisableUsersByOptionalDateRange(
            LocalDate startDate, LocalDate endDate, String searchTerm,Pageable pageable);

    void updateRoleAndEnabled(UserDTO userDTO);

    boolean deleteByEmail(String email);
}
