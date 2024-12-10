package ru.skypro.homework.controller;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import ru.skypro.homework.dto.announce.AdDTO;
import ru.skypro.homework.dto.announce.AdsDTO;
import ru.skypro.homework.service.AdImageService;
import ru.skypro.homework.service.AdService;

@RestController
@RequestMapping("/ads")
@RequiredArgsConstructor
public class AdController {
    private final AdService adService;
    private final AdImageService adImageService;

    @GetMapping(path = "/ads")
    @Operation(description = "Получение всех объявлений")
    public ResponseEntity<AdsDTO> getAllAds() {
        return null;
    }

    @PostMapping(path = "/ads")
    @Operation(description = "Добавление объявления")
    public ResponseEntity<AdDTO> createAd(@RequestBody AdDTO adDTO) {
        return null;
    }

    @GetMapping(path = "/ads/{id}")
    @Operation(description = "Получение информации об объявлении")
    public ResponseEntity<AdDTO> getAdById(@PathVariable Long id) {
        return null;
    }

    @DeleteMapping(path = "/ads/{id}")
    @Operation(description = "Удаление объявления")
    ResponseEntity<Void> deleteAdById(@PathVariable Long id) {
        return null;
    }

    @PatchMapping(path = "/ads/{id}")
    @Operation(description = "Обновление  информации об объявлении")
    ResponseEntity<AdDTO> updateAdById(@PathVariable Long id, @RequestBody AdDTO adDTO) {
        return null;
    }

    @GetMapping(path = "/ads/me")
    @Operation(description = "Получение объявлений авторизованного пользователя")
    ResponseEntity<AdsDTO> getCurrentUserAds(Authentication authentication) {
        return null;
    }

    @PatchMapping(path = "/ads/{id}/image")
    @Operation(description = "Обновление картинки объявления")
    ResponseEntity<String> updateImageOnAdById(@PathVariable Long id, @RequestBody AdDTO adDTO) {
        return null;
    }


}
