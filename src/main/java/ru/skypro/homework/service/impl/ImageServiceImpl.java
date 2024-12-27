package ru.skypro.homework.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.entity.Image;
import ru.skypro.homework.entity.ModelImage;
import ru.skypro.homework.mapper.UserMapper;
import ru.skypro.homework.repository.ImageRepository;
import ru.skypro.homework.service.ImageService;
import ru.skypro.homework.service.LoggingMethod;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Objects;

import static java.nio.file.StandardOpenOption.CREATE_NEW;

@Service
@Slf4j
@RequiredArgsConstructor
public class ImageServiceImpl implements ImageService {

    private final ImageRepository imageRepository;
    private final UserMapper userMapper;
    private final LoggingMethod loggingMethod;

    @Value("${path.to.photos.folder}")
    private String photoDir;

    @Override
    public ModelImage updateEntitiesPhoto(MultipartFile image, ModelImage entity) throws IOException {
        //если у сущности уже есть картинка, то нужно ее удалить
        if (entity.getImage() != null) {
            imageRepository.delete(entity.getImage());
        }

        //заполняем поля photo и сохраняем фото в БД
        Image photoEntity = userMapper.mapMultipartFileToImage(image);
        log.info("Создана сущность photoEntity - {}, {}, {}", photoEntity.getId(), photoEntity.getMediaType(), photoEntity.getFilePath());
        entity.setImage(photoEntity);
        imageRepository.save(photoEntity);

        //адрес до директории хранения фото на ПК
        Path filePath = Path.of(photoDir, entity.getImage().getId() + "."
                + this.getExtension(Objects.requireNonNull(image.getOriginalFilename())));
        log.info("путь к файлу для хранения фото на ПК: {}", filePath);

        //добавляем в сущность фото путь где оно хранится на ПК
        entity.getImage().setFilePath(filePath.toString());

        //добавляем в сущность путь на ПК
        entity.setFilePath(filePath.toString());

        //сохранение на ПК
        this.saveFileOnDisk(image, filePath);

        return entity;
    }


    /**
     * Метод сохраняет изображение на диск
     *
     * @param image    - изображение
     * @param filePath - путь, куда будет сохранено изображение
     * @return boolean
     * @throws IOException
     */
    @Override
    public boolean saveFileOnDisk(MultipartFile image, Path filePath) throws IOException {

        log.info("Запущен метод сервиса {}", loggingMethod.getMethodName());
        Files.createDirectories(filePath.getParent());
        Files.deleteIfExists(filePath);
        try (InputStream is = image.getInputStream();
             OutputStream os = Files.newOutputStream(filePath, CREATE_NEW);
             BufferedInputStream bis = new BufferedInputStream(is, 1024);
             BufferedOutputStream bos = new BufferedOutputStream(os, 1024);
        ) {
            bis.transferTo(bos);
        }
        return true;
    }

    @Override
    public byte[] getPhotoFromDisk(Image image) throws NoSuchFieldException {
        Path path1 = Path.of(image.getFilePath());
        try {
            return Files.readAllBytes(path1);
        } catch (IOException e) {
            throw new NoSuchFieldException("Искомый файл аватара или фото объявления, отсутствует на ПК\n" +
                    "Поиск файла перенаправлен в БД");
        }
    }

    /**
     * Метод получает расширение изображения
     *
     * @param fileName - полное название изображения
     * @return расширение изображения
     */
    @Override
    public String getExtension(String fileName) {
        log.info("Запущен метод сервиса {}", loggingMethod.getMethodName());
        return fileName.substring(fileName.lastIndexOf(".") + 1);
    }

}
