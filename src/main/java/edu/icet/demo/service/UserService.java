package edu.icet.demo.service;

import edu.icet.demo.dto.auth.AccessToken;
import edu.icet.demo.dto.auth.UserDTO;
import edu.icet.demo.dto.auth.UserLoginRequest;

import java.util.List;

public interface UserService {
    UserDTO register(UserDTO userDTO);

    AccessToken verify(UserLoginRequest userLoginRequest);

    List<UserDTO> getDisableUsers();

    void updateRoleAndEnabled(UserDTO userDTO);
}
