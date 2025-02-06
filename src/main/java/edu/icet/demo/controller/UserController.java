package edu.icet.demo.controller;


import edu.icet.demo.dto.auth.AccessToken;
import edu.icet.demo.dto.response.PaginatedResponse;
import edu.icet.demo.dto.response.SuccessResponseWithData;
import edu.icet.demo.dto.auth.UserDTO;
import edu.icet.demo.dto.auth.UserLoginRequest;
import edu.icet.demo.service.UserService;
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
@CrossOrigin
@Validated
@RequestMapping("/api/v1/auth")
public class UserController {

    private final UserService userService;

    @PostMapping("/register")
    public SuccessResponseWithData<UserDTO> register(@Valid @RequestBody UserDTO user, BindingResult result) {
        UserDTO registeredUser = userService.register(user);
        return SuccessResponseWithData.<UserDTO>builder()
                .status(HttpStatus.CREATED.value())
                .message("User register successfully !")
                .data(registeredUser)
                .build();
    }

    @PostMapping("/login")
    public SuccessResponseWithData<AccessToken> login(@Valid @RequestBody UserLoginRequest userLoginRequest,
                                                      BindingResult result) {
        AccessToken token = userService.verify(userLoginRequest);
        return SuccessResponseWithData.<AccessToken>builder()
                .status(HttpStatus.OK.value())
                .message("User login successfully !")
                .data(token)
                .build();
    }

    @GetMapping("/disable-users")
    public PaginatedResponse<UserDTO> getDisableUsersByOptionalDateRange(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
            @RequestParam(required = false) String searchTerm,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        Pageable pageable = PageRequest.of(page, size);
        return userService.getDisableUsersByOptionalDateRange(startDate, endDate, searchTerm, pageable);
    }


    @PatchMapping("/update-role-and-enabled")
    public SuccessResponseWithData<String> updateRoleAndEnabled(@RequestBody UserDTO userDTO) {
        userService.updateRoleAndEnabled(userDTO);
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
