package ru.skypro.homework.security;

import lombok.Data;
import ru.skypro.homework.dto.Role;

/**
 * Класс DTO, представляющий данные пользователя для аутентификации и авторизации.
 * <p>
 * Этот класс используется для передачи информации о пользователе, такой как идентификатор,
 * email, пароль и роль, между слоями приложения.
 * </p>
 *
 * @see Data
 */
@Data
public class MyUserDetailsDTO {

    /**
     * Идентификатор пользователя.
     */
    private Integer id;

    /**
     * Email пользователя.
     * <p>
     * Используется в качестве логина для аутентификации.
     * </p>
     */
    private String email;

    /**
     * Пароль пользователя.
     * <p>
     * Хранится в зашифрованном виде.
     * </p>
     */
    private String password;

    /**
     * Роль пользователя.
     * <p>
     * Определяет уровень доступа пользователя в системе.
     * </p>
     */
    private Role role;

}
