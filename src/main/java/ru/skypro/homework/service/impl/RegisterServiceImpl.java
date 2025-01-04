package ru.skypro.homework.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.skypro.homework.dto.Register;
import ru.skypro.homework.dto.Role;
import ru.skypro.homework.entity.UserEntity;
import ru.skypro.homework.exception.UserAlreadyExistException;
import ru.skypro.homework.mapper.UserMapper;
import ru.skypro.homework.repository.UserRepository;
import ru.skypro.homework.service.RegisterService;

/**
 * Сервис для выполнения операций, связанных с регистрацией пользователей.
 * <p>
 * Этот класс реализует интерфейс {@link RegisterService} и предоставляет метод для регистрации
 * нового пользователя в системе.
 * </p>
 *
 * @see Service
 * @see Slf4j
 * @see RequiredArgsConstructor
 * @see RegisterService
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class RegisterServiceImpl implements RegisterService {

    private final UserRepository userRepository;
    private final PasswordEncoder encoder;
    private final UserMapper userMapper;

    /**
     * Регистрирует нового пользователя в системе.
     *
     * @param register данные для регистрации пользователя.
     * @param role     роль пользователя.
     * @return {@code true}, если регистрация прошла успешно.
     * @throws UserAlreadyExistException если пользователь с указанным email уже существует.
     */
    @Override
    public boolean register(Register register, Role role) {
        log.info("register method from RegisterService was invoked");

        if (userRepository.findByEmail(register.getUsername()).isPresent()) {
            log.error("User already exists with username: {}", register.getUsername());
            throw new UserAlreadyExistException("User already exists with username: " + register.getUsername());
        }
        UserEntity userEntity = userMapper.toUserEntityFromRegisterDTO(register);
        userEntity.setEmail(userEntity.getEmail().toLowerCase());
        userEntity.setPassword(encoder.encode(userEntity.getPassword()));
        userEntity.setRole(role);
        userRepository.save(userEntity);

        log.info("Successfully registered a new user with username: {}", register.getUsername());
        return true;
    }

}
