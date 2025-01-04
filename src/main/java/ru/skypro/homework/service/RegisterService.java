package ru.skypro.homework.service;

import ru.skypro.homework.dto.Register;
import ru.skypro.homework.dto.Role;

/**
 * Интерфейс для выполнения операций, связанных с регистрацией пользователей.
 * <p>
 * Этот интерфейс предоставляет метод для регистрации нового пользователя в системе.
 * </p>
 */
public interface RegisterService {

    /**
     * Регистрирует нового пользователя в системе.
     *
     * @param register данные для регистрации пользователя.
     * @param role     роль пользователя.
     * @return {@code true}, если регистрация прошла успешно.
     */
    boolean register(Register register, Role role);

}
