package ru.skypro.homework.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.stereotype.Service;
import ru.skypro.homework.dto.Register;
import ru.skypro.homework.service.AuthService;


@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserDetailsServiceImpl userService;
    private final PasswordEncoder encoder = new BCryptPasswordEncoder();


    @Override
    public boolean login(String userName, String password) {
        if (!userService.userExists(userName)) {
            return false;
        }
        UserDetails userDetails = userService.loadUserByUsername(userName);
        return encoder.matches(password, userDetails.getPassword());
    }

//    @Override
//    public boolean register(Register register) {
//        if (manager.userExists(register.getUsername())) {
//            return false;
//        }
//        manager.createUser(
//                User.builder()
//                        .passwordEncoder(this.encoder::encode)
//                        .password(register.getPassword())
//                        .username(register.getUsername())
//                        .roles(register.getRole().name())
//                        .build());
//        return true;
//    }

    @Override
    public boolean register(Register register) {
        if (userService.userExists(register.getUsername())) {
            return false;
        }
        userService.createUser(register, encoder.encode(register.getPassword()));
        return true;
    }

}
