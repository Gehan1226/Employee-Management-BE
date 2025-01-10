package edu.icet.demo.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.icet.demo.dto.auth.AccessToken;
import edu.icet.demo.dto.auth.UserDTO;
import edu.icet.demo.dto.auth.UserLoginRequest;
import edu.icet.demo.dto.enums.SecurityAuthorities;
import edu.icet.demo.entity.UserEntity;
import edu.icet.demo.exception.*;
import edu.icet.demo.repository.UserRepository;
import edu.icet.demo.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final AuthenticationManager authenticationManager;
    private final JWTService jwtService;
    private final ObjectMapper objectMapper;

    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(12);

    @Override
    public UserDTO register(UserDTO userDTO) {
        userDTO.setPassword(encoder.encode(userDTO.getPassword()));
        userDTO.setEnabled(true);
        try {
            UserEntity userEntity = userRepository.save(objectMapper.convertValue(userDTO, UserEntity.class));
            return objectMapper.convertValue(userEntity, UserDTO.class);
        } catch (DataIntegrityViolationException exception) {
            throw new DataDuplicateException("User name already exist.Please try another user name!");
        }
    }

    @Override
    public AccessToken verify(UserLoginRequest userLoginRequest) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        userLoginRequest.getUserName(), userLoginRequest.getPassword()));

        return new AccessToken(jwtService.genarateToken(userLoginRequest.getUserName()));
    }

    @Override
    public List<UserDTO> getDisableUsers() {
        List<UserDTO> userDTOList = new ArrayList<>();
        userRepository.findByEnabledFalse().forEach(userEntity ->
                userDTOList.add(objectMapper.convertValue(userEntity, UserDTO.class))
        );
        return userDTOList;
    }

    @Override
    public void updateRoleAndEnabled(UserDTO userDTO) {
        int updatedRows = userRepository.updateUserRoleAndEnabled(
                userDTO.getEmail(),
                userDTO.getRole(),
                userDTO.isEnabled()
        );
        if (updatedRows == 0) {
            throw new DataNotFoundException("User with email '%s' not found".formatted(userDTO.getEmail()));
        }
    }

    @Override
    public boolean deleteByEmail(String email) {
        if (email == null || email.isEmpty()) {
            throw new MissingAttributeException("Email cannot be null or empty");
        }
        int deletedRows = 0;
        try {
            deletedRows = userRepository.deleteUserByEmail(email);
        } catch (Exception e) {
            throw new DeletionException("User with email '%s' cannot be deleted.".formatted(email));
        }

        if (deletedRows == 0) {
            throw new DataNotFoundException("User with email '%s' not found.".formatted(email));
        }
        return true;
    }

}
