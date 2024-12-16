package ru.skypro.homework.service;

import ru.skypro.homework.dto.user.NewPasswordDTO;
import ru.skypro.homework.dto.user.UserDTO;

import java.util.Optional;

public interface UserService {

    UserDTO findUserByUsername(String username);

    void changePassword(NewPasswordDTO newPasswordDTO, String username);

}
