package ru.skypro.homework.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.skypro.homework.entity.UserEntity;
import ru.skypro.homework.repository.UserRepository;
import ru.skypro.homework.service.LoginService;

import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class LoginServiceImpl implements LoginService {

    private final UserRepository userRepository;
    private final PasswordEncoder encoder;

    @Override
    public boolean login(String userName, String password) {
        Optional<UserEntity> optionalUser = userRepository.findByEmail(userName);
        if (optionalUser.isEmpty()) {
            log.info("UserEntity not found");
            return false;
        }
        return encoder.matches(password, optionalUser.get().getPassword());
    }

}
