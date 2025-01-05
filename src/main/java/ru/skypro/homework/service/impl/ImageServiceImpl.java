package ru.skypro.homework.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.entity.ImageEntity;
import ru.skypro.homework.exception.ImageNotFoundException;
import ru.skypro.homework.repository.ImageRepository;
import ru.skypro.homework.service.ImageService;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.UUID;

import static java.nio.file.StandardOpenOption.CREATE_NEW;

/**
 * Сервис для работы с изображениями.
 * <p>
 * Этот класс реализует интерфейс {@link ImageService} и предоставляет методы для выполнения операций
 * с изображениями, таких как сохранение, получение и удаление изображений.
 * </p>
 *
 * @see Service
 * @see Slf4j
 * @see RequiredArgsConstructor
 * @see ImageService
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class ImageServiceImpl implements ImageService {

    private final ImageRepository imageRepository;

    @Value("${image.dir.path}")
    private String imageDir;

    /**
     * Сохраняет изображение на диск и в базу данных.
     *
     * @param image файл изображения.
     * @param name  имя директории для сохранения изображения.
     * @return путь к сохраненному изображению.
     * @throws IOException если возникает ошибка при обработке изображения.
     */
    @Transactional
    @Override
    public String saveImage(MultipartFile image, String name) throws IOException {
        log.info("saveImage method from ImageService was invoked");

        String extension = StringUtils.getFilenameExtension(image.getOriginalFilename());
        String filename = UUID.randomUUID() + "." + extension;

        Path fullFilePath = Path.of(imageDir, filename);
        Files.createDirectories(fullFilePath.getParent());
        Files.deleteIfExists(fullFilePath);
        try (
                InputStream is = image.getInputStream();
                OutputStream os = Files.newOutputStream(fullFilePath, CREATE_NEW);
                BufferedInputStream bis = new BufferedInputStream(is, 1024);
                BufferedOutputStream bos = new BufferedOutputStream(os, 1024)
        ) {
            bis.transferTo(bos);
        } catch (IOException e) {
            log.error("Error writing file: {}", e.getMessage());
            throw new RuntimeException("Error writing file", e);
        }
        ImageEntity imageEntity = new ImageEntity();
        imageEntity.setFilePath(fullFilePath.toString());
        imageEntity.setFileSize(image.getSize());
        imageEntity.setMediaType(image.getContentType());
        imageEntity.setData(image.getBytes());

        log.info("Successfully loaded new image file: {}", filename);
        imageRepository.save(imageEntity);
        return name + "/image/" + filename;
    }

    /**
     * Получает изображение по его имени.
     *
     * @param name имя изображения.
     * @return массив байтов, представляющий изображение.
     * @throws IOException если возникает ошибка при чтении изображения.
     */
    @Transactional
    @Override
    public byte[] getImage(String name) throws IOException {
        log.info("getImage method from ImageService was invoked");

        String fullPath = imageDir + "/" + name;
        File file = new File(fullPath);
        if (file.exists()) {
            log.info("Successfully read image file with name: {}", name);
            return Files.readAllBytes(Path.of(fullPath));
        }
        log.debug("Image file with name: {} not exist at path: {}", name, fullPath);
        return null;
    }

    /**
     * Удаляет изображение с диска и из базы данных.
     *
     * @param path путь к изображению.
     * @throws ImageNotFoundException если изображение не найдено.
     */
    @Transactional
    @Override
    public void deleteImage(String path) {
        log.info("deleteImage method from ImageService was invoked");

        if (path == null) {
            log.debug("Path for Image file are null");
            return;
        }

        // Удаление файла изображения из директории на ПК
        String fileName = path.substring(path.lastIndexOf('/'));
        File fileToDelete = new File(imageDir + fileName);

        if (fileToDelete.exists()) {
            if (fileToDelete.delete()) {
                log.info("Successfully deleted Image file from imagePath: {}", fileToDelete);
            } else {
                log.error("Failed to delete Image file from imagePath: {}", fileToDelete);
                throw new ImageNotFoundException("Failed to delete Image file from imagePath" + fileToDelete);
            }
        } else {
            log.error("Image file not found from imagePath: {}", fileToDelete);
            throw new ImageNotFoundException("Image file not found from imagePath" + fileToDelete);
        }

        // Удаление записи изображения из базы данных
        String fullPath = imageDir + fileName;
        String fullPathDB = fullPath.replace("/", "\\");
        log.info("Try to delete Image record from database with path: {}", path);

        imageRepository.deleteByFilePath(fullPathDB);
        log.info("Successfully deleted Image record from database with path: {}", path);
    }

}
