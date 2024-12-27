package ru.skypro.homework.service;

import java.io.IOException;

public interface PhotoService {

    byte[] getPhoto(Integer pk) throws NoSuchFieldException;

}
