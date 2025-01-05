package edu.icet.demo.controller;


import edu.icet.demo.dto.auth.AccessToken;
import edu.icet.demo.dto.response.SuccessResponse;
import edu.icet.demo.dto.auth.UserDTO;
import edu.icet.demo.dto.auth.UserLoginRequest;
import edu.icet.demo.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@CrossOrigin
@Validated
@RequestMapping("/api/v1/auth")
public class UserController {

    private final UserService userService;

    @PostMapping("/register")
    public SuccessResponse<UserDTO> register(@Valid @RequestBody UserDTO user, BindingResult result){
        UserDTO registeredUser = userService.register(user);
        return SuccessResponse.<UserDTO>builder()
                .status(HttpStatus.CREATED.value())
                .message("User register successfully !")
                .data(registeredUser)
                .build();
    }

    @PostMapping("/login")
    public SuccessResponse<AccessToken> login(@Valid @RequestBody UserLoginRequest userLoginRequest, BindingResult result){
        AccessToken token = userService.verify(userLoginRequest);
        return SuccessResponse.<AccessToken>builder()
                .status(HttpStatus.OK.value())
                .message("User login successfully !")
                .data(token)
                .build();
    }

    @GetMapping("/disable-users")
    public SuccessResponse<List<UserDTO>> getDisableUsers(){
        List<UserDTO> disableUsers = userService.getDisableUsers();
        String message = disableUsers.isEmpty() ? "Disable User List is empty!" : "Disable User list retrieved.";
        return SuccessResponse.<List<UserDTO>>builder()
                .status(HttpStatus.OK.value())
                .message(message)
                .data(disableUsers)
                .build();
    }
}
