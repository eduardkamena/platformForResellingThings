package ru.skypro.homework.exception;

/**
 * Исключение, выбрасываемое при попытке доступа к несуществующему пользователю.
 * <p>
 * Это исключение используется, когда запрашиваемый пользователь не найден в системе.
 * </p>
 *
 * @see RuntimeException
 */
public class UserNotFoundException extends RuntimeException {

    public UserNotFoundException(String message) {
        super(message);
    }

}
