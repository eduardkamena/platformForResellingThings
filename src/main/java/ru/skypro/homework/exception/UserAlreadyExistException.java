package ru.skypro.homework.exception;

/**
 * Исключение, выбрасываемое при попытке регистрации пользователя, который уже существует в системе.
 * <p>
 * Это исключение используется, когда пользователь с указанным email или логином уже зарегистрирован.
 * </p>
 *
 * @see RuntimeException
 */
public class UserAlreadyExistException extends RuntimeException {

    public UserAlreadyExistException(String message) {
        super(message);
    }

}
