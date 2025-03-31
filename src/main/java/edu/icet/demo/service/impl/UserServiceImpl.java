package edu.icet.demo.service.impl;

import edu.icet.demo.dto.auth.AccessToken;
import edu.icet.demo.dto.auth.UserDTO;
import edu.icet.demo.dto.auth.UserLoginRequest;
import edu.icet.demo.dto.response.PaginatedResponse;
import edu.icet.demo.entity.UserEntity;
import edu.icet.demo.entity.UserRoleEntity;
import edu.icet.demo.exception.*;
import edu.icet.demo.repository.EmployeeRepository;
import edu.icet.demo.repository.UserRepository;
import edu.icet.demo.repository.UserRoleRepository;
import edu.icet.demo.service.UserService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final AuthenticationManager authenticationManager;
    private final JWTService jwtService;
    private final ModelMapper modelMapper;
    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(12);
    private final UserRoleRepository userRoleRepository;
    private final EmployeeRepository employeeRepository;

    @Override
    public void addUser(UserDTO userDTO) {

        if (!employeeRepository.existsByEmail(userDTO.getEmail())) {
            throw new DataNotFoundException("Employee with email '%s' not found".formatted(userDTO.getEmail()));
        }
        if (userRepository.existsByUserNameOrEmail(userDTO.getUserName(), userDTO.getEmail())) {
            throw new DataDuplicateException(
                    String.format(
                            "A user with the username '%s' or email '%s' already exists.",
                            userDTO.getUserName(),
                            userDTO.getEmail()
                    )
            );
        }
        List<UserRoleEntity> userRoleEntities = userRoleRepository.findByNameIn(userDTO.getRoleList());
        if (userRoleEntities.isEmpty()) {
            throw new DataNotFoundException("user role's '%s' not found".formatted(userDTO.getRoleList()));
        }
        userDTO.setPassword(encoder.encode(userDTO.getPassword()));
        userDTO.setEnabled(true);
        userDTO.setCreatedDate(LocalDate.now());

        try {
            UserEntity userEntity = modelMapper.map(userDTO, UserEntity.class);
            userEntity.setRoleList(userRoleEntities);
            userRepository.save(userEntity);
        } catch (DataIntegrityViolationException exception) {
            throw new DataDuplicateException(
                    "Database constraint violation. Please check that all provided values are valid and unique!");
        } catch (Exception exception) {
            throw new UnexpectedException("An unexpected error occurred while saving the user");
        }
    }

    @Override
    public AccessToken authenticateAndGenerateToken(UserLoginRequest userLoginRequest) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        userLoginRequest.getUserName(), userLoginRequest.getPassword()));

        return new AccessToken(jwtService.genarateToken(userLoginRequest.getUserName()));
    }

    @Override
    public PaginatedResponse<UserDTO> getDisableUsersByOptionalDateRange(
            LocalDate startDate, LocalDate endDate, String searchTerm, Pageable pageable) {

        List<UserDTO> userDTOList = new ArrayList<>();
        Page<UserEntity> userEntityPage =
                userRepository.findByOptionalDateRangeAndEnabledFalseAndSearchTerm(
                        startDate, endDate, searchTerm, pageable
                );

        userEntityPage.forEach(userEntity ->
                userDTOList.add(modelMapper.map(userEntity, UserDTO.class))
        );

        return new PaginatedResponse<>(
                HttpStatus.OK.value(),
                userDTOList.isEmpty() ? "No disabled users found!" : "Disabled user list retrieved.",
                userDTOList,
                userEntityPage.getTotalPages(),
                userEntityPage.getTotalElements(),
                userEntityPage.getNumber()
        );
    }


    @Override
    public void updateRoleAndEnabled(UserDTO userDTO) {
//        int updatedRows = userRepository.updateUserRoleAndEnabled(
//                userDTO.getEmail(),
//                userDTO.getRole(),
//                userDTO.isEnabled()
//        );
//        if (updatedRows == 0) {
//            throw new DataNotFoundException("User with email '%s' not found".formatted(userDTO.getEmail()));
//        }
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
