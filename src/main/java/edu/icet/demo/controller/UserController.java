package edu.icet.demo.controller;


import edu.icet.demo.dto.UserDTO;
import edu.icet.demo.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/register")
    public UserDTO register(@RequestBody UserDTO user){
        return userService.register(user);
    }

    @PostMapping("/login")
    public String login(@RequestBody UserDTO user){
        return userService.verify(user);
    }
}
