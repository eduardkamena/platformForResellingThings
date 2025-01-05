package ru.skypro.homework.service;

/**
 * Интерфейс для выполнения операций, связанных с аутентификацией пользователей.
 * <p>
 * Этот интерфейс предоставляет метод для проверки учетных данных пользователя при входе в систему.
 * </p>
 */
public interface LoginService {

    /**
     * Проверяет учетные данные пользователя при входе в систему.
     *
     * @param userName имя пользователя (email).
     * @param password пароль пользователя.
     * @return {@code true}, если учетные данные верны, иначе {@code false}.
     */
    boolean login(String userName, String password);

}
