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
import ru.skypro.homework.exception.UserWithEmailNotFoundException;
import ru.skypro.homework.mapper.UserMapper;
import ru.skypro.homework.repository.UserRepository;
import ru.skypro.homework.service.ImageService;
import ru.skypro.homework.service.UserService;

import java.io.IOException;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder encoder;
    private final ImageService imageService;
    private final UserMapper userMapper;


    @Override
    public boolean setPassword(NewPassword newPassword, String email) {
        Optional<UserEntity> optionalUser = userRepository.findByEmail(email);
        if (optionalUser.isPresent()) {
            UserEntity userEntity = optionalUser.get();
            if (encoder.matches(newPassword.getCurrentPassword(), userEntity.getPassword())) {
                userEntity.setPassword(encoder.encode(newPassword.getNewPassword()));
                userRepository.save(userEntity);
                log.info("Updated password");
                return true;
            }
        }
        log.info("Password not update");
        return false;
    }


    @Override
    public User getUser(String email) {
        UserEntity userEntity = userRepository.findByEmail(email)
                .orElseThrow(() -> new UserWithEmailNotFoundException(email));
        return userMapper.toUserDto(userEntity);
    }


    @Override
    public User updateUser(User user, String email) {
        UserEntity userEntity = userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException(email));
        userEntity.setFirstName(user.getFirstName());
        userEntity.setLastName(user.getLastName());
        userEntity.setPhone(user.getPhone());
        userRepository.save(userEntity);
        log.info("UserEntity updated");
        return userMapper.toUserDto(userEntity);
    }


    @Override
    public void updateAvatar(MultipartFile image, String email) {
        UserEntity userEntity = userRepository.findByEmail(email)
                .orElseThrow(() -> new UserWithEmailNotFoundException(email));
        imageService.deleteFileIfNotNull(userEntity.getImage());
        userEntity.setImage(imageService.saveImage(image, "/users"));
        userRepository.save(userEntity);
        log.info("Avatar updated");
    }


    @Override
    public byte[] getImage(String name) throws IOException {
        return imageService.getImage(name);
    }
}
