package ru.skypro.homework.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.dto.NewPassword;
import ru.skypro.homework.dto.UserDto;
import ru.skypro.homework.entity.User;
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
        Optional<User> optionalUser = userRepository.findByEmail(email);
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            if (encoder.matches(newPassword.getCurrentPassword(), user.getPassword())) {
                user.setPassword(encoder.encode(newPassword.getNewPassword()));
                userRepository.save(user);
                log.trace("Updated password");
                return true;
            }
        }
        log.trace("Password not update");
        return false;
    }


    @Override
    public UserDto getUser(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UserWithEmailNotFoundException(email));
        return userMapper.toUserDto(user);
    }


    @Override
    public UserDto updateUser(UserDto userDto, String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException(email));
        user.setFirstName(userDto.getFirstName());
        user.setLastName(userDto.getLastName());
        user.setPhone(userDto.getPhone());
        userRepository.save(user);
        log.trace("User updated");
        return userMapper.toUserDto(user);
    }


    @Override
    public void updateAvatar(MultipartFile image, String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UserWithEmailNotFoundException(email));
        imageService.deleteFileIfNotNull(user.getImage());
        user.setImage(imageService.saveImage(image, "/users"));
        userRepository.save(user);
        log.trace("Avatar updated");
    }


    @Override
    public byte[] getImage(String name) throws IOException {
        return imageService.getImage(name);
    }
}
