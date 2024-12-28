package ru.skypro.homework.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.skypro.homework.config.CustomUserDetails;
import ru.skypro.homework.dto.Login;
import ru.skypro.homework.dto.Register;
import ru.skypro.homework.entity.User;
import ru.skypro.homework.mapper.UserMapper;
import ru.skypro.homework.repository.UserRepository;
import ru.skypro.homework.service.AuthService;
import ru.skypro.homework.service.LoggingMethod;
import ru.skypro.homework.service.UserService;


@Service
@Slf4j
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserDetailsServiceImpl userDetailsService;
    private final UserRepository userRepository;
    private final UserService userService;
    private final PasswordEncoder encoder;

    @Override
    public boolean login(String userName, String password) {
        return true;
    }
    @Override
    public boolean login(Login loginDto) {

        CustomUserDetails userDetails = userDetailsService.loadUserByUsername(loginDto.getUsername());
        return encoder.matches(loginDto.getPassword(), userDetails.getPassword());
    }

    @Override
    public boolean register(Register registerDto) {
        if(userRepository.findByUsername(registerDto.getUsername()).isPresent()) {
            return false;
        } else {
            userService.registerUser(registerDto);
            return true;
        }
    }

}
