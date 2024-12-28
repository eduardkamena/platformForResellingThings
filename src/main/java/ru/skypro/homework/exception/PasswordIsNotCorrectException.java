package ru.skypro.homework.exception;

public class PasswordIsNotCorrectException extends RuntimeException {
    public PasswordIsNotCorrectException(String message) {
        super(message);
    }
}
