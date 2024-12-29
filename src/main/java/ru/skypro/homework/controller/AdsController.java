package ru.skypro.homework.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.dto.Ad;
import ru.skypro.homework.dto.CreateOrUpdateAd;
import ru.skypro.homework.dto.ExtendedAd;
import ru.skypro.homework.dto.Ads;
import ru.skypro.homework.service.AdsService;

import java.io.IOException;

@Slf4j
@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequiredArgsConstructor
@RequestMapping("/ads")
public class AdsController {

    private final AdsService adsService;

    @GetMapping
    public ResponseEntity<Ads> getAllAds() {
        return ResponseEntity.ok(adsService.getAllAds());
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Ad> addAd(Authentication authentication,
                                    @RequestPart("properties") CreateOrUpdateAd createOrUpdateAd,
                                    @RequestPart("image") MultipartFile image) {
        return ResponseEntity.ok(adsService.addAd(createOrUpdateAd, authentication.getName(), image));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ExtendedAd> getAds(@PathVariable int id) {
        return ResponseEntity.ok(adsService.getAds(id));
    }

    @PreAuthorize("hasRole('ADMIN') or @adsServiceImpl.getAds(#id).getEmail() == authentication.principal.username")
    @DeleteMapping("/{id}")
    public ResponseEntity<?> removeAd(@PathVariable int id) {
        adsService.removeAd(id);
        return ResponseEntity.ok(HttpStatus.NO_CONTENT);
    }

    @PreAuthorize("hasRole('ADMIN') or @adsServiceImpl.getAds(#id).getEmail()==authentication.principal.username")
    @PatchMapping("/{id}")
    public ResponseEntity<Ad> updateAds(@RequestBody CreateOrUpdateAd createOrUpdateAd,
                                        @PathVariable int id) {
        return ResponseEntity.ok(adsService.updateAds(createOrUpdateAd, id));
    }

    @GetMapping("/me")
    public ResponseEntity<Ads> getAdsMe(Authentication authentication) {
        return ResponseEntity.ok(adsService.getAdsMe(authentication.getName()));
    }

    @PatchMapping(value = "/{id}/image", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> updateImage(@PathVariable int id, @RequestParam MultipartFile image) {
        adsService.updateAdsImage(id, image);
        return ResponseEntity.ok(HttpStatus.NO_CONTENT);
    }

    @GetMapping(value = "/image/{name}", produces = MediaType.IMAGE_PNG_VALUE)
    public byte[] getImages(@PathVariable String name) throws IOException {
        return adsService.getImage(name);
    }

}
