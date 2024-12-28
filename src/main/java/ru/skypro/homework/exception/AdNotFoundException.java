package ru.skypro.homework.exception;

public class AdNotFoundException extends RuntimeException {
    public AdNotFoundException(Integer message) {
        super(String.valueOf(message));
    }
}
