package edu.icet.demo.service;

import edu.icet.demo.dto.AccessToken;
import edu.icet.demo.dto.UserDTO;
import edu.icet.demo.dto.UserLoginRequest;

public interface UserService {
    UserDTO register(UserDTO userDTO);

    AccessToken verify(UserLoginRequest userLoginRequest);
}
