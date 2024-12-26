package edu.icet.demo.controller;


import edu.icet.demo.dto.AccessToken;
import edu.icet.demo.dto.SuccessResponse;
import edu.icet.demo.dto.UserDTO;
import edu.icet.demo.dto.UserLoginRequest;
import edu.icet.demo.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@CrossOrigin
@Validated
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
}
