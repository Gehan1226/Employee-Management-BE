package edu.icet.demo.service;

import edu.icet.demo.dto.UserDTO;

public interface UserService {
    UserDTO register(UserDTO userDTO);

    String verify(UserDTO userDTO);
}
