package ru.skypro.homework.controller;

import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.skypro.homework.service.*;

import java.io.FileNotFoundException;
import java.io.IOException;

@RestController
@Slf4j
@RequestMapping("/photo")
@CrossOrigin(origins = "http://localhost:3000")
@RequiredArgsConstructor
public class ImageController {

    @Value("${path.to.avatars.folder}")
    private String avatarPath;

    private final ImageService imageService;
    private final UserService userService;
    private final AdService adService;

    @GetMapping(value = "/{path}")
    public ResponseEntity<byte[]> getUrl(@PathVariable String path) {
        try {
            byte[] byteFromFile = imageService.getByteFromFile(path);
            return ResponseEntity.ok(byteFromFile);
        } catch (IOException e) {
            log.error("Error reading file from path: {}", path, e);
            return ResponseEntity.badRequest().build();
        }
    }





}
