package ru.skypro.homework.service.impl;

import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.dto.Register;
import ru.skypro.homework.dto.user.NewPasswordDTO;
import ru.skypro.homework.dto.user.UpdateUserDTO;
import ru.skypro.homework.dto.user.UserDTO;
import ru.skypro.homework.entity.Image;
import ru.skypro.homework.entity.User;
import ru.skypro.homework.exception.PasswordIsNotCorrectException;
import ru.skypro.homework.exception.UserAlreadyRegisteredException;
import ru.skypro.homework.mapper.UserMapper;
import ru.skypro.homework.repository.ImageRepository;
import ru.skypro.homework.repository.UserRepository;
import ru.skypro.homework.service.LoggingMethod;
import ru.skypro.homework.service.UserService;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;

import static java.nio.file.StandardOpenOption.CREATE_NEW;

/**
 * Сервис хранящий логику для управления данными пользователей.
 */
@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder encoder;
    private final ImageRepository imageRepository;

    @Value("${path.to.user.images}")
    private String imagePath;
    @Value("${path.to.default.user.image}")
    private String pathToDefaultUserImage;


    @Override
    public void updatePassword(NewPasswordDTO dto, String username) throws PasswordIsNotCorrectException {
        User user = userRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException("User is not found"));
        if (encoder.matches(dto.getCurrentPassword(), user.getPassword())) {
            user.setPassword(encoder.encode(dto.getNewPassword()));
            userRepository.save(user);
        } else {
            throw new PasswordIsNotCorrectException("Password is not correct");
        }
    }
    @Override
    public byte[] getImage(String username) throws IOException {
        try {
            User user = userRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException("Username is not found"));
            if (user.getImage() == null) {
                // Создаем объект Image для изображения по умолчанию
                Image defaultImage = new Image();
                defaultImage.setFilePath(pathToDefaultUserImage);
                // Сохраняем объект Image в репозитории
                defaultImage = imageRepository.save(defaultImage);

                // Связываем сохраненный объект Image с User
                user.setImage(defaultImage);
                userRepository.save(user); // Сохраняем изменения пользователя
            }
            return Files.readAllBytes(Path.of(user.getImage().getFilePath())); // Используем путь из объекта Image
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    @Override
    public UserDTO getUser(String username) {
        User user = userRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException("User is not found"));
        return UserMapper.toDto(user);
    }
    @Override
    public UpdateUserDTO updateUser(String username, UpdateUserDTO dto) {
        User user = userRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException("User is not found"));
        user.setFirstName(dto.getFirstName());
        user.setLastName(dto.getLastName());
        user.setPhone(dto.getPhone());
        userRepository.save(user);
        return dto;
    }
    @Override
    public void updateUserImage(String username, MultipartFile file) throws IOException {
        User user = userRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException("User is not found"));
        uploadImage(user, file);
    }
    @Override
    public User registerUser(Register dto) {
        if (userRepository.findByUsername(dto.getUsername()).isPresent()) {
            throw new UserAlreadyRegisteredException(dto.getUsername());
        } else {
            User user = UserMapper.toEntity(dto);
            user.setPassword(encoder.encode(dto.getPassword()));
            return userRepository.save(user);
        }
    }
    public void uploadImage(User user, MultipartFile file) throws IOException {
        Path path = Path.of(imagePath, user.getUsername() + "." + StringUtils.getFilenameExtension(file.getOriginalFilename()));

        Files.createDirectories(path.getParent());
        Files.deleteIfExists(path);

        try (
                InputStream is = file.getInputStream();
                OutputStream os = Files.newOutputStream(path, CREATE_NEW);
                BufferedInputStream bis = new BufferedInputStream(is, 1024);
                BufferedOutputStream bos = new BufferedOutputStream(os, 1024)
        ) {
            bis.transferTo(bos);

            // Создаем объект Image
            Image userImage = new Image();
            userImage.setFilePath(path.toString());
            userImage.setFileSize(file.getSize());
            userImage.setMediaType(file.getContentType());
            userImage.setData(file.getBytes()); // Устанавливаем данные файла

            // Сохраняем объект Image в репозитории
            userImage = imageRepository.save(userImage);

            // Связываем сохраненный объект Image с пользователем
            user.setImage(userImage);

            // Сохраняем пользователя
            userRepository.save(user);
        }
    }

}
