package ru.skypro.homework.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.skypro.homework.dto.Register;
import ru.skypro.homework.dto.Role;
import ru.skypro.homework.entity.UserEntity;
import ru.skypro.homework.mapper.UserMapper;
import ru.skypro.homework.repository.UserRepository;
import ru.skypro.homework.service.RegisterService;

@Slf4j
@Service
@RequiredArgsConstructor
public class RegisterServiceImpl implements RegisterService {

    private final UserRepository userRepository;
    private final PasswordEncoder encoder;
    private final UserMapper userMapper;

    @Override
    public boolean register(Register register, Role role) {
        if (userRepository.findByEmail(register.getUsername()).isPresent()) {
            return false;
        }
        UserEntity userEntity = userMapper.toUser(register);
        userEntity.setEmail(userEntity.getEmail().toLowerCase());
        userEntity.setPassword(encoder.encode(userEntity.getPassword()));
        userEntity.setRole(role);
        userRepository.save(userEntity);
        log.info("Registered a new userEntity");
        return true;
    }

}
