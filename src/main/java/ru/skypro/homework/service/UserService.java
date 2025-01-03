package ru.skypro.homework.service;

import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.dto.NewPassword;
import ru.skypro.homework.dto.User;

import java.io.IOException;

public interface UserService {

    void setPassword(NewPassword newPassword, String email);

    User getUser(String email);

    User updateUser(User user, String email);

    void updateAvatar(MultipartFile image, String email) throws IOException;

    byte[] getImage(String name) throws IOException;

}
