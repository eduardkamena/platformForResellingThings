package ru.skypro.homework.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.entity.ImageEntity;
import ru.skypro.homework.repository.ImageRepository;
import ru.skypro.homework.service.ImageService;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.UUID;

import static java.nio.file.StandardOpenOption.CREATE_NEW;

@Slf4j
@Service
@RequiredArgsConstructor
public class ImageServiceImpl implements ImageService {

    private final ImageRepository imageRepository;

    @Value("${image.dir.path}")
    private String imageDir;

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
        log.info("Loaded new image file: {} ", filename);

        imageRepository.save(imageEntity);
        return name + "/image/" + filename;

    }

    @Transactional
    @Override
    public byte[] getImage(String name) throws IOException {
        String fullPath = imageDir + "/" + name;
        File file = new File(fullPath);
        if (file.exists()) {
            return Files.readAllBytes(Path.of(fullPath));
        }
        return null;
    }

    @Transactional
    @Override
    public void deleteFileIfNotNull(String path) {
        if (path == null) {
            return;
        }

        // Удаление файла
        String fileName = path.substring(path.lastIndexOf('/'));
        File fileToDelete = new File(imageDir + fileName);

        if (fileToDelete.exists()) {
            if (fileToDelete.delete()) {
                log.info("Image file successfully deleted from imagePath {}", fileToDelete);
            } else {
                log.error("Failed to delete file from imagePath {}", fileToDelete);
                throw new RuntimeException("Failed to delete image file");
            }
        } else {
            log.error("File not found from imagePath {}", fileToDelete);
            throw new RuntimeException("File not found");
        }

        // Удаление записи из базы данных
        String fullPath = imageDir + fileName;
        String fullPathDB = fullPath.replace("/", "\\");
        log.info("Attempting to delete image record from database with path: {}", path);
        imageRepository.deleteByFilePath(fullPathDB);
        log.info("Image record successfully deleted from database with path: {}", path);
    }

}
