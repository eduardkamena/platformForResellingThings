package ru.skypro.homework.exception;

/**
 * Исключение, выбрасываемое при попытке доступа к несуществующему комментарию.
 * <p>
 * Это исключение используется, когда запрашиваемый комментарий не найден в системе.
 * </p>
 *
 * @see RuntimeException
 */
public class CommentNotFoundException extends RuntimeException {

    public CommentNotFoundException(String message) {
        super(message);
    }

}
