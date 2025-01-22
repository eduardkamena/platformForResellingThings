package ru.skypro.homework.service.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.entity.ImageEntity;
import ru.skypro.homework.exception.ImageNotFoundException;
import ru.skypro.homework.repository.ImageRepository;

import java.io.IOException;
import java.lang.reflect.Field;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ImageServiceImplMockMvcTest {

    @Mock
    private ImageRepository imageRepository;

    @InjectMocks
    private ImageServiceImpl imageService;

    private MultipartFile testFile;
    private ImageEntity testImageEntity;

    // Создаем путь к тестируемым файлам изображений
    private final String imageDir = "src/main/resources/images";

    @BeforeEach
    public void setUp() throws NoSuchFieldException, IllegalAccessException, IOException {
        testFile = new MockMultipartFile(
                "testImage",
                "testImage.png",
                "image/png",
                "test image content".getBytes()
        );

        testImageEntity = new ImageEntity();
        testImageEntity.setId(1);
        testImageEntity.setFilePath("testPath/testImage.png");
        testImageEntity.setFileSize(testFile.getSize());
        testImageEntity.setMediaType(testFile.getContentType());
        testImageEntity.setData(testFile.getBytes());

        // Устанавливаем значение imageDir через рефлексию
        Field imageDirField = ImageServiceImpl.class.getDeclaredField("imageDir");
        imageDirField.setAccessible(true); // Делаем поле доступным
        imageDirField.set(imageService, imageDir); // Устанавливаем значение
    }

    @Test
    public void shouldSaveImageAndReturnPath() throws IOException {
        // given & when
        when(imageRepository.save(any(ImageEntity.class))).thenReturn(testImageEntity);

        String resultPath = imageService.saveImage(testFile, "testDir");

        // then
        assertNotNull(resultPath);
        assertTrue(resultPath.contains("testDir/image/"));

        // Проверяем, что файл был создан на диске
        String fileName = resultPath.substring(resultPath.lastIndexOf('/') + 1);
        Path filePath = Paths.get(imageDir, fileName);
        assertTrue(Files.exists(filePath));

        // Проверяем, что метод save был вызван
        verify(imageRepository, times(1)).save(any(ImageEntity.class));

        // Удаляем файл после теста
        Files.deleteIfExists(filePath);
        assertFalse(Files.exists(filePath));

    }

    @Test
    public void shouldGetImageAndReturnImageBytes() throws IOException {
        // given
        String imageName = "testImage.png";
        Path imagePath = Path.of(imageDir, imageName);
        Files.createDirectories(imagePath.getParent());
        Files.write(imagePath, testFile.getBytes());

        // when
        byte[] resultBytes = imageService.getImage(imageName);

        // then
        assertNotNull(resultBytes);
        assertArrayEquals(testFile.getBytes(), resultBytes);

        Files.deleteIfExists(imagePath);
    }

    @Test
    public void shouldGetImageAndReturnNullWhenImageNotFound() throws IOException {
        // given
        String nonExistentImageName = "nonExistentImage.png";

        // when
        byte[] resultBytes = imageService.getImage(nonExistentImageName);

        // then
        assertNull(resultBytes);
    }

    @Test
    public void shouldThrowDeleteImageWhenImageNotFound() {
        // given & when
        String nonExistentImagePath = "nonExistentPath/testImage.png";

        // Удаляем файл, если он существует
        Path filePath = Path.of(imageDir, nonExistentImagePath);
        try {
            Files.deleteIfExists(filePath);
        } catch (IOException e) {
            fail("Failed to delete file: " + e.getMessage());
        }

        // then
        assertThrows(ImageNotFoundException.class, () -> imageService.deleteImage(nonExistentImagePath));
    }

}
