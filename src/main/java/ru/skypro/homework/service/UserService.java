package ru.skypro.homework.service;

import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.dto.NewPassword;
import ru.skypro.homework.dto.User;

import java.io.IOException;

/**
 * Интерфейс для выполнения операций, связанных с пользователями.
 * <p>
 * Этот интерфейс предоставляет методы для работы с данными пользователей,
 * таких как изменение пароля, получение информации о пользователе, обновление данных пользователя
 * и управление аватаром пользователя.
 * </p>
 */
public interface UserService {

    /**
     * Устанавливает новый пароль для пользователя.
     *
     * @param newPassword объект, содержащий текущий и новый пароль.
     * @param email       email пользователя.
     */
    void setPassword(NewPassword newPassword, String email);

    /**
     * Получает информацию о пользователе по его email.
     *
     * @param email email пользователя.
     * @return объект {@link User}, содержащий информацию о пользователе.
     */
    User getUser(String email);

    /**
     * Обновляет данные пользователя.
     *
     * @param user  объект, содержащий новые данные пользователя.
     * @param email email пользователя.
     * @return объект {@link User}, содержащий обновленные данные пользователя.
     */
    User updateUser(User user, String email);

    /**
     * Обновляет аватар пользователя.
     *
     * @param image файл изображения для нового аватара.
     * @param email email пользователя.
     * @throws IOException если возникает ошибка при обработке изображения.
     */
    void updateAvatar(MultipartFile image, String email) throws IOException;

    /**
     * Получает аватар пользователя по его имени.
     *
     * @param name имя файла аватара.
     * @return массив байтов, представляющий изображение.
     * @throws IOException если возникает ошибка при чтении изображения.
     */
    byte[] getImage(String name) throws IOException;

}
