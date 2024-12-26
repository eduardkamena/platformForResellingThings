package ru.skypro.homework.service;

import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.entity.Image;
import ru.skypro.homework.entity.ModelImage;

import java.io.IOException;
import java.nio.file.Path;

public interface ImageService {

    ModelImage updateEntitiesPhoto(MultipartFile image, ModelImage entity) throws IOException;

    boolean saveFileOnDisk(MultipartFile image, Path filePath) throws IOException;

    byte[] getPhotoFromDisk(Image photo) throws NoSuchFieldException;

    String getExtension(String fileName);

}
