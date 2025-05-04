package edu.icet.demo.controller;

import edu.icet.demo.dto.auth.*;
import edu.icet.demo.dto.response.PaginatedResponse;
import edu.icet.demo.dto.response.SuccessResponse;
import edu.icet.demo.dto.response.SuccessResponseWithData;
import edu.icet.demo.service.UserService;
import edu.icet.demo.utils.CookieUtils;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequiredArgsConstructor
@Validated
@RequestMapping("/api/v1/auth")
public class UserController {

    private final UserService userService;

    private static final  String REFRESH_TOKEN_COOKIE_NAME = "refreshToken";
    private static final  String ACCESS_TOKEN_COOKIE_NAME = "accessToken";
    private static final  String REFRESH_TOKEN_PATH = "/";

    @PostMapping("/signup")
    public SuccessResponse addUser(@Valid @RequestBody UserCreateRequest user, BindingResult result) {
        userService.addUser(user);
        return SuccessResponse.builder()
                .status(HttpStatus.CREATED.value())
                .message("User register successfully !")
                .build();
    }

    @PostMapping("/login")
    public SuccessResponse login(@Valid @RequestBody UserLoginRequest userLoginRequest,
                                 BindingResult result, HttpServletResponse response) {
        AuthResponse authResponse = userService.authenticateAndGenerateToken(userLoginRequest);
        Cookie accessCookie = new Cookie(ACCESS_TOKEN_COOKIE_NAME, authResponse.getAccessToken());
        accessCookie.setHttpOnly(true);
        accessCookie.setSecure(true);
        accessCookie.setPath("/");
        accessCookie.setMaxAge(7 * 24 * 60 * 60);

        Cookie refreshCookie = new Cookie(REFRESH_TOKEN_COOKIE_NAME, authResponse.getRefreshToken());
        refreshCookie.setHttpOnly(true);
        refreshCookie.setSecure(true);
        refreshCookie.setPath(REFRESH_TOKEN_PATH);
        refreshCookie.setMaxAge(7 * 24 * 60 * 60);

        response.addCookie(accessCookie);
        response.addCookie(refreshCookie);

        return SuccessResponse.builder()
                .status(HttpStatus.OK.value())
                .message("User logged in successfully !")
                .build();
    }

    @GetMapping("/user")
    public SuccessResponseWithData<UserResponse> getUser() {
        UserResponse user = userService.getUser();
        return SuccessResponseWithData.<UserResponse>builder()
                .status(HttpStatus.OK.value())
                .message("User retrieved successfully !")
                .data(user)
                .build();
    }

    @GetMapping("/disable-users")
    public PaginatedResponse<UserCreateRequest> getDisableUsersByOptionalDateRange(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
            @RequestParam(required = false) String searchTerm,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        Pageable pageable = PageRequest.of(page, size);
        return userService.getDisableUsersByOptionalDateRange(startDate, endDate, searchTerm, pageable);
    }


    @PatchMapping("/update-role-and-enabled")
    public SuccessResponseWithData<String> updateRoleAndEnabled(@RequestBody UserCreateRequest userCreateRequest) {
        userService.updateRoleAndEnabled(userCreateRequest);
        return SuccessResponseWithData.<String>builder()
                .status(HttpStatus.OK.value())
                .message("User updated successfully !")
                .build();
    }

    @DeleteMapping("/delete-by-email/{email}")
    public SuccessResponseWithData<String> deleteByEmail(@PathVariable String email) {
        userService.deleteByEmail(email);
        return SuccessResponseWithData.<String>builder()
                .status(HttpStatus.OK.value())
                .message("User deleted successfully !")
                .build();
    }

    @PostMapping("/refresh")
    public SuccessResponse refresh(HttpServletRequest request, HttpServletResponse response) {
        String refreshToken = CookieUtils.extractTokenFromCookie(request, REFRESH_TOKEN_COOKIE_NAME);
        if (refreshToken == null || refreshToken.isEmpty()) {
            return SuccessResponse.builder()
                    .status(HttpStatus.UNAUTHORIZED.value())
                    .message("Refresh token is missing or invalid")
                    .build();
        }

        AuthResponse authResponse = userService.refresh(refreshToken);
        Cookie accessCookie = new Cookie(ACCESS_TOKEN_COOKIE_NAME, authResponse.getAccessToken());
        accessCookie.setHttpOnly(true);
        accessCookie.setSecure(true);
        accessCookie.setPath("/");
        accessCookie.setMaxAge(15 * 60);
        response.addCookie(accessCookie);

        Cookie refreshCookie = new Cookie(REFRESH_TOKEN_COOKIE_NAME, authResponse.getRefreshToken());
        refreshCookie.setHttpOnly(true);
        refreshCookie.setSecure(true);
        refreshCookie.setPath(REFRESH_TOKEN_PATH);
        refreshCookie.setMaxAge(7 * 24 * 60 * 60);
        response.addCookie(refreshCookie);

        return SuccessResponse.builder()
                .status(HttpStatus.OK.value())
                .message("Refresh token generated successfully !")
                .build();
    }
}