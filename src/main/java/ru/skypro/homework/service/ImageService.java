package ru.skypro.homework.service;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface ImageService {

    String saveImage(MultipartFile image, String name);

    byte[] getImage(String name) throws IOException;

    void deleteFileIfNotNull(String path);

}
