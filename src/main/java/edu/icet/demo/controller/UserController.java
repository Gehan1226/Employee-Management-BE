package edu.icet.demo.controller;

import edu.icet.demo.dto.auth.AccessToken;
import edu.icet.demo.dto.response.PaginatedResponse;
import edu.icet.demo.dto.response.SuccessResponse;
import edu.icet.demo.dto.response.SuccessResponseWithData;
import edu.icet.demo.dto.auth.UserCreateRequest;
import edu.icet.demo.dto.auth.UserLoginRequest;
import edu.icet.demo.service.UserService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
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

    @PostMapping()
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
        AccessToken token = userService.authenticateAndGenerateToken(userLoginRequest);
        ResponseCookie cookie = ResponseCookie.from("authToken", token.getToken())
                .httpOnly(true)
                .secure(true)
                .path("/")
                .maxAge(604800)
                .sameSite("Strict")
                .build();
        response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());
        return SuccessResponse.builder()
                .status(HttpStatus.OK.value())
                .message("User logged in successfully !")
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
}
