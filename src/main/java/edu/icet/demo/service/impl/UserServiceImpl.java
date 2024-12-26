package edu.icet.demo.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.icet.demo.dto.AccessToken;
import edu.icet.demo.dto.UserDTO;
import edu.icet.demo.dto.UserLoginRequest;
import edu.icet.demo.entity.UserEntity;
import edu.icet.demo.exception.DataDuplicateException;
import edu.icet.demo.repository.UserRepository;
import edu.icet.demo.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

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
        userDTO.setEnabled(false);
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
}
