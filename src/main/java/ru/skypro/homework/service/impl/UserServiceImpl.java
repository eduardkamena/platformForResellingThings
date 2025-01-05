package ru.skypro.homework.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.dto.NewPassword;
import ru.skypro.homework.dto.User;
import ru.skypro.homework.entity.UserEntity;
import ru.skypro.homework.exception.UserNotFoundException;
import ru.skypro.homework.mapper.UserMapper;
import ru.skypro.homework.repository.UserRepository;
import ru.skypro.homework.service.ImageService;
import ru.skypro.homework.service.UserService;

import java.io.IOException;
import java.util.Optional;

/**
 * Сервис для выполнения операций, связанных с пользователями.
 * <p>
 * Этот класс реализует интерфейс {@link UserService} и предоставляет методы для работы с данными пользователей,
 * таких как изменение пароля, получение информации о пользователе, обновление данных пользователя
 * и управление аватаром пользователя.
 * </p>
 *
 * @see Service
 * @see Slf4j
 * @see RequiredArgsConstructor
 * @see UserService
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder encoder;
    private final ImageService imageService;
    private final UserMapper userMapper;

    /**
     * Устанавливает новый пароль для пользователя.
     *
     * @param newPassword объект, содержащий текущий и новый пароль.
     * @param email       email пользователя.
     * @throws UserNotFoundException если пользователь с указанным email не найден.
     */
    @Override
    public void setPassword(NewPassword newPassword, String email) {
        log.info("setPassword method from UserService was invoked");

        Optional<UserEntity> optionalUser = userRepository.findByEmail(email);
        if (optionalUser.isPresent()) {
            UserEntity userEntity = optionalUser.get();
            if (encoder.matches(newPassword.getCurrentPassword(), userEntity.getPassword())) {
                userEntity.setPassword(encoder.encode(newPassword.getNewPassword()));
                userRepository.save(userEntity);

                log.info("Successfully updated password for user: {}", email);
            }
        }
    }

    /**
     * Получает информацию о пользователе по его email.
     *
     * @param email email пользователя.
     * @return объект {@link User}, содержащий информацию о пользователе.
     * @throws UserNotFoundException если пользователь с указанным email не найден.
     */
    @Override
    public User getUser(String email) {
        log.info("getUser method from UserService was invoked");

        UserEntity userEntity = userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("User not found with email: " + email));

        log.info("Successfully got User with email: {}", email);
        return userMapper.toUserDTOFromUserEntity(userEntity);
    }

    /**
     * Обновляет данные пользователя.
     *
     * @param user  объект, содержащий новые данные пользователя.
     * @param email email пользователя.
     * @return объект {@link User}, содержащий обновленные данные пользователя.
     * @throws UserNotFoundException если пользователь с указанным email не найден.
     */
    @Override
    public User updateUser(User user, String email) {
        log.info("updateUser method from UserService was invoked");

        UserEntity userEntity = userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("User not found with email: " + email));
        userEntity.setFirstName(user.getFirstName());
        userEntity.setLastName(user.getLastName());
        userEntity.setPhone(user.getPhone());
        userRepository.save(userEntity);

        log.info("Successfully updated User data with email: {}", email);
        return userMapper.toUserDTOFromUserEntity(userEntity);
    }

    /**
     * Обновляет аватар пользователя.
     *
     * @param image файл изображения для нового аватара.
     * @param email email пользователя.
     * @throws IOException           если возникает ошибка при обработке изображения.
     * @throws UserNotFoundException если пользователь с указанным email не найден.
     */
    @Override
    public void updateAvatar(MultipartFile image, String email) throws IOException {
        log.info("updateAvatar method from UserService was invoked");

        UserEntity userEntity = userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("User not found with email: " + email));
        imageService.deleteImage(userEntity.getImage());
        log.info("Successfully deleted old avatar with name: {} for User with email: {}", userEntity.getImage(), email);

        userEntity.setImage(imageService.saveImage(image, "/users"));
        userRepository.save(userEntity);

        log.info("Successfully updated User avatar with name: {}", userEntity.getImage());
    }

    /**
     * Получает аватар пользователя по его имени.
     *
     * @param name имя файла аватара.
     * @return массив байтов, представляющий изображение.
     * @throws IOException если возникает ошибка при чтении изображения.
     */
    @Override
    public byte[] getImage(String name) throws IOException {
        log.info("getImage method from UserService was invoked " +
                "and successfully got User avatar with name: {}", name);
        return imageService.getImage(name);
    }

}
