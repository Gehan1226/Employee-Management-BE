package edu.icet.demo.repository;

import edu.icet.demo.dto.UserDTO;

public interface UserRepository {

    UserDTO findByUserName(String username);
}