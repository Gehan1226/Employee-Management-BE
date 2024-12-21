package edu.icet.demo.service;

import edu.icet.demo.dto.AccessToken;
import edu.icet.demo.dto.UserDTO;

public interface UserService {
    UserDTO register(UserDTO userDTO);

    AccessToken verify(UserDTO userDTO);
}
