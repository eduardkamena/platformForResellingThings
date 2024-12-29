package ru.skypro.homework.service;

import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.dto.NewPassword;
import ru.skypro.homework.dto.UserDto;

import java.io.IOException;

public interface UserService {

    boolean setPassword(NewPassword newPassword, String email);

    UserDto getUser(String email);

    UserDto updateUser(UserDto userDto, String email);

    void updateAvatar(MultipartFile image, String email);

    byte[] getImage(String name) throws IOException;
}
