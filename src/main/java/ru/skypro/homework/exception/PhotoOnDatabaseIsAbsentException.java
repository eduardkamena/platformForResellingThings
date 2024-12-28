package ru.skypro.homework.exception;

public class PhotoOnDatabaseIsAbsentException extends RuntimeException {

    public PhotoOnDatabaseIsAbsentException(String message) {
        super(message);
    }

}
