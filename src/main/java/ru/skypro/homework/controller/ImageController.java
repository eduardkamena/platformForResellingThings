package ru.skypro.homework.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.skypro.homework.service.ImageService;
import ru.skypro.homework.service.LoggingMethod;
import ru.skypro.homework.service.PhotoService;

import java.io.IOException;

@RestController
@Slf4j
@RequestMapping("/photo")
@CrossOrigin(origins = "http://localhost:3000")
@AllArgsConstructor
public class ImageController {

    private final PhotoService photoService;
    private final LoggingMethod loggingMethod;


    @GetMapping("/image/{photoId}")
    public ResponseEntity<byte[]> getPhotoFromSource(@PathVariable Integer photoId) throws NoSuchFieldException {
        log.info("Запущен метод контроллера {}", loggingMethod.getMethodName());
        return ResponseEntity.ok(photoService.getPhoto(photoId));
    }
}
