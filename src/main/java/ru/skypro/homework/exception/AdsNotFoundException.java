package ru.skypro.homework.exception;

/**
 * Исключение, выбрасываемое при попытке доступа к несуществующему объявлению.
 * <p>
 * Это исключение используется, когда запрашиваемое объявление не найдено в системе.
 * </p>
 *
 * @see RuntimeException
 */
public class AdsNotFoundException extends RuntimeException {

    public AdsNotFoundException(String message) {
        super(message);
    }

}
