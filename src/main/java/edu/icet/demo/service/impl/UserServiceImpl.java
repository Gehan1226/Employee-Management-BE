package edu.icet.demo.service.impl;

import edu.icet.demo.dto.auth.*;
import edu.icet.demo.dto.enums.SecurityAuthorities;
import edu.icet.demo.dto.response.PaginatedResponse;
import edu.icet.demo.entity.RefreshTokenEntity;
import edu.icet.demo.entity.UserEntity;
import edu.icet.demo.entity.UserRoleEntity;
import edu.icet.demo.exception.*;
import edu.icet.demo.repository.EmployeeRepository;
import edu.icet.demo.repository.RefreshTokenRepository;
import edu.icet.demo.repository.UserRepository;
import edu.icet.demo.repository.UserRoleRepository;
import edu.icet.demo.security.JWTService;
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

import java.time.Instant;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

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
    private final RefreshTokenRepository refreshTokenRepo;

    @Override
    public void addUser(UserCreateRequest userCreateRequest) {

        if (!employeeRepository.existsByEmail(userCreateRequest.getEmail())) {
            throw new DataNotFoundException(
                    "Employee with email '%s' not found".formatted(userCreateRequest.getEmail()));
        }
        if (userRepository.existsByUserNameOrEmail(userCreateRequest.getUserName(), userCreateRequest.getEmail())) {
            throw new DataDuplicateException(
                    String.format(
                            "A user with the username '%s' or email '%s' already exists.",
                            userCreateRequest.getUserName(),
                            userCreateRequest.getEmail()
                    )
            );
        }
        List<UserRoleEntity> userRoleEntities = userRoleRepository.findByNameIn(userCreateRequest.getRoleList());
        if (userRoleEntities.isEmpty()) {
            throw new DataNotFoundException("user role's '%s' not found".formatted(userCreateRequest.getRoleList()));
        }
        userCreateRequest.setPassword(encoder.encode(userCreateRequest.getPassword()));
        userCreateRequest.setEnabled(true);
        userCreateRequest.setCreatedDate(LocalDate.now());

        try {
            UserEntity userEntity = modelMapper.map(userCreateRequest, UserEntity.class);
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
    public AuthResponse authenticateAndGenerateToken(UserLoginRequest userLoginRequest) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        userLoginRequest.getUserName(), userLoginRequest.getPassword()));
        RefreshTokenEntity refreshToken = createRefreshToken(userLoginRequest.getUserName());
        return new AuthResponse(
                jwtService.generateToken(userLoginRequest.getUserName()),
                refreshToken.getToken());
    }

    @Override
    public PaginatedResponse<UserCreateRequest> getDisableUsersByOptionalDateRange(
            LocalDate startDate, LocalDate endDate, String searchTerm, Pageable pageable) {

        List<UserCreateRequest> userCreateRequestList = new ArrayList<>();
        Page<UserEntity> userEntityPage =
                userRepository.findByOptionalDateRangeAndEnabledFalseAndSearchTerm(
                        startDate, endDate, searchTerm, pageable
                );

        userEntityPage.forEach(userEntity ->
                userCreateRequestList.add(modelMapper.map(userEntity, UserCreateRequest.class))
        );

        return new PaginatedResponse<>(
                HttpStatus.OK.value(),
                userCreateRequestList.isEmpty() ? "No disabled users found!" : "Disabled user list retrieved.",
                userCreateRequestList,
                userEntityPage.getTotalPages(),
                userEntityPage.getTotalElements(),
                userEntityPage.getNumber()
        );
    }


    @Override
    public void updateRoleAndEnabled(UserCreateRequest userCreateRequest) {
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

    @Override
    public UserResponse getUserByName(String name) {
        UserEntity userEntity = userRepository.findByUserName(name);
        if (userEntity == null) {
            throw new DataNotFoundException("User with name '%s' not found.".formatted(name));
        }
        UserResponse userResponse = modelMapper.map(userEntity, UserResponse.class);

        List<SecurityAuthorities> roleList = new ArrayList<>();
        userEntity.getRoleList().forEach(role -> roleList.add(role.getName()));
        userResponse.setRoleList(roleList);

        return userResponse;
    }

    public RefreshTokenEntity createRefreshToken(String userName) {
        try {
            UserEntity user = userRepository.findByUserName(userName);
            RefreshTokenEntity token = new RefreshTokenEntity();
            token.setUser(user);
            token.setToken(UUID.randomUUID().toString());
            token.setExpiryDate(Instant.now().plus(7, ChronoUnit.DAYS));
            return refreshTokenRepo.save(token);
        } catch (Exception exception) {
            throw new UnexpectedException("An unexpected error occurred while creating the refresh token");
        }
    }
}