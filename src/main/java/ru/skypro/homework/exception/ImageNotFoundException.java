package ru.skypro.homework.exception;

/**
 * Исключение, выбрасываемое при попытке доступа к несуществующему изображению.
 * <p>
 * Это исключение используется, когда запрашиваемое изображение не найдено в системе.
 * </p>
 *
 * @see RuntimeException
 */
public class ImageNotFoundException extends RuntimeException {

    public ImageNotFoundException(String message) {
        super(message);
    }

}
