package ru.skypro.homework.exception;

public class PasswordIsNotMatchException extends RuntimeException {

    public PasswordIsNotMatchException(String message) {
        super(message);
    }

}
