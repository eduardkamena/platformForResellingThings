package ru.skypro.homework.service;

import liquibase.pro.packaged.F;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;


import java.io.File;
import java.io.IOException;

import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class ImageService {

    @Value("${image.dir.path}")
    private String imageDir;

    public String saveImage(MultipartFile image, String name) {

        String extension = StringUtils.getFilenameExtension(image.getOriginalFilename());
        String filename = UUID.randomUUID() + "." + extension;
        Path filePath = Path.of(imageDir, filename);
        try {
            Files.write(filePath, image.getBytes());
        } catch (IOException e) {
            log.error("Error writing file: {}", e.getMessage());
            throw new RuntimeException("Error writing file", e);
        }
        log.trace("Loaded file, name: ", filename);
        return name + "/image/" + filename;
    }

    public byte[] getImage(String name) throws IOException {
        String fullPath = imageDir + "/" + name;
        File file = new File(fullPath);
        if (file.exists()) {
            return Files.readAllBytes(Path.of(fullPath));
        }
        return null;
    }

    public void deleteFileIfNotNull(String path) {
        if (path == null) {
            return;
        }
        String fileName = path.substring(path.lastIndexOf('/'));
        File fileToDelete = new File(imageDir + fileName);
        if (fileToDelete.exists()) {
            if (fileToDelete.delete()) {
                log.trace("File successfully deleted");
            } else {
                log.trace("Failed to delete file");
            }
        } else {
            log.trace("File not found");
        }
    }
}
