package ru.skypro.homework.service;

import org.springframework.security.core.Authentication;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.dto.user.NewPasswordDTO;
import ru.skypro.homework.dto.user.UpdateUserDTO;
import ru.skypro.homework.dto.user.UserDTO;
import ru.skypro.homework.entity.User;

import java.io.IOException;
import java.util.Optional;

public interface UserService {

    /**
     * Метод обновляет пароль для авторизованного пользователя.
     * @param newPass        новый пароль
     * @param authentication
     */
    void setPassword(NewPasswordDTO newPass, Authentication authentication);

    /**
     * Метод возвращает информацию об авторизованном пользователе.
     * @param username
     * @return объект {@link User}, содержащий информацию о пользователе.
     */
    User getUser(String username);

    /**
     * Метод обновляет информацию об авторизованном пользователе.
     * @param updateUser объект содержащий поля с именем, фамилией и номером телефона.
     * @param authentication
     * @return объект {@link User}
     */
    User updateUser(UpdateUserDTO updateUser, Authentication authentication);

    /**
     * Метод обновляет аватар авторизованного пользователя.
     * @param image
     * @return true или false
     * @throws IOException
     */
    void updateUserImage(MultipartFile image, Authentication authentication) throws IOException;


    // Нужно пересмотреть (рефактор/соединить/удалить)
    UserDTO findUserByUsername(String username);

    // Нужно пересмотреть (рефактор/соединить/удалить)
    void changePassword(NewPasswordDTO newPasswordDTO, String username);

}
