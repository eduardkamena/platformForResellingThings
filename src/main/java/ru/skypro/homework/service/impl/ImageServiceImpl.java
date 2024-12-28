package ru.skypro.homework.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.entity.Image;
import ru.skypro.homework.mapper.UserMapper;
import ru.skypro.homework.repository.ImageRepository;
import ru.skypro.homework.service.ImageService;
import ru.skypro.homework.service.LoggingMethod;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.Objects;
import java.util.UUID;

import static java.nio.file.StandardOpenOption.CREATE_NEW;

@Service
@Slf4j
@RequiredArgsConstructor
public class ImageServiceImpl implements ImageService {

    private final ImageRepository imageRepository;
    private final UserMapper userMapper;
    private final LoggingMethod loggingMethod;

    @Value("${path.to.avatars.folder}")
    private String avatarPath;

    @Override
    public Image saveImage(MultipartFile imageFile) throws IOException {
        Image image = new Image();
        createNewPathAndSaveFile(imageFile, image);

        return getSave(image);
    }
    @Override
    public Image getImage(Integer imageId) {
        return imageRepository.findById(imageId).get();
    }
    @Override
    public Image updateImage(MultipartFile imageFile, Integer imageId) throws IOException {
        Image image = getImage(imageId);

        Path path = Path.of(image.getFilePath());
        Files.deleteIfExists(path);

        Image newPathAndSaveFile = createNewPathAndSaveFile(imageFile, image);
        return getSave(newPathAndSaveFile);
    }
    @Override
    public byte[] getByteFromFile(String path) throws IOException {
        return Files.readAllBytes(Path.of(avatarPath, path));
    }
    @Transactional(isolation = Isolation.READ_COMMITTED)
    public Image getSave(Image image) {
        return imageRepository.save(image);
    }
    private Image createNewPathAndSaveFile(MultipartFile imageFile, Image image) throws IOException {
        String originalFilename = imageFile.getOriginalFilename();

        String fileName = UUID.randomUUID() + "." + getExtension(Objects.requireNonNull(originalFilename));
        Path path = Path.of(avatarPath, fileName);

        Files.createDirectories(path.getParent());

        readAndWriteInTheDirectory(imageFile, path);

        image.setFilePath(path.toString());
        image.setMediaType(imageFile.getContentType());
        image.setFileSize(imageFile.getSize());

        return image;
    }
    private void readAndWriteInTheDirectory(MultipartFile fileImage, Path path) throws IOException {
        try (
                InputStream inputStream = fileImage.getInputStream();
                OutputStream outputStream = Files.newOutputStream(path, StandardOpenOption.CREATE_NEW);
                BufferedInputStream bufferedInputStream = new BufferedInputStream(inputStream, 4096);
                BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(outputStream, 4096);
        ) {
            bufferedInputStream.transferTo(bufferedOutputStream);
        }
    }
    private String getExtension(String fileName) {
        return fileName.substring(fileName.lastIndexOf(".") + 1);
    }

}
