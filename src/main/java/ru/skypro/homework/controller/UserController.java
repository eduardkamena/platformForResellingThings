package ru.skypro.homework.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import ru.skypro.homework.dto.user.NewPasswordDTO;
import ru.skypro.homework.dto.user.UserDTO;
import ru.skypro.homework.service.UserService;

import javax.validation.Valid;

@RestController
@RequestMapping("/users")
@CrossOrigin(origins = "http://localhost:3000")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping(path = "/me")
    public UserDTO getCurrentUserInfo(Authentication authentication) {
        return userService.findUserByUsername(authentication.getName());
    }

    @PostMapping(path = "/set_password")
    public void setPassword(@RequestBody @Valid NewPasswordDTO newPasswordDTO,
                                         Authentication authentication) {
        userService.changePassword(newPasswordDTO, authentication.getName());
    }
}
