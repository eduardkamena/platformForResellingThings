package ru.skypro.homework.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
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

/**
 * Контроллер для управления объявлениями.
 * <p>
 * Этот контроллер предоставляет REST API для выполнения операций с объявлениями, таких как:
 * получение всех объявлений, добавление нового объявления, обновление и удаление существующих объявлений,
 * а также управление изображениями объявлений.
 * </p>
 *
 * @see RestController
 * @see RequestMapping
 * @see Tag
 * @see CrossOrigin
 * @see Slf4j
 */
@Slf4j
@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequiredArgsConstructor
@RequestMapping("/ads")
@Tag(name = "Объявления")
public class AdsController {

    private final AdsService adsService;

    /**
     * Получает все объявления.
     * <p>
     * Этот метод возвращает список всех объявлений, доступных в системе.
     * Ответ возвращается в формате JSON.
     * </p>
     *
     * @return {@link ResponseEntity} с объектом {@link Ads}, содержащим список всех объявлений, и статусом HTTP 200 (OK).
     * @see Ads
     * @see ResponseEntity
     * @see Operation
     * @see ApiResponse
     */
    @Operation(
            tags = "Объявления",
            summary = "Получение всех объявлений",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "OK",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = Ads.class)
                            )
                    )
            }
    )
    @GetMapping
    public ResponseEntity<Ads> getAllAds() {
        log.info("getAllAds method from AdsController was invoked");
        return ResponseEntity.ok(adsService.getAllAds());
    }

    /**
     * Добавляет новое объявление.
     * <p>
     * Этот метод принимает данные объявления и изображение, после чего сохраняет их в системе.
     * </p>
     *
     * @param authentication   объект аутентификации текущего пользователя.
     * @param createOrUpdateAd данные для создания или обновления объявления.
     * @param image            изображение объявления.
     * @return {@link ResponseEntity} с объектом {@link Ad} и статусом HTTP 201 (Created).
     * @throws IOException если возникает ошибка при обработке изображения.
     * @see Ad
     * @see CreateOrUpdateAd
     * @see MultipartFile
     * @see Operation
     * @see ApiResponse
     */
    @Operation(
            tags = "Объявления",
            summary = "Добавление объявления",
            responses = {
                    @ApiResponse(
                            responseCode = "201",
                            description = "Created",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = Ad.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "401",
                            description = "Unauthorized"
                    )
            }
    )
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Ad> addAd(Authentication authentication,
                                    @RequestPart("properties") CreateOrUpdateAd createOrUpdateAd,
                                    @RequestPart("image") MultipartFile image) throws IOException {
        log.info("addAd method from AdsController was invoked with adTitle: {} and imageSize: {}", createOrUpdateAd.getTitle(), image.getSize());
        return ResponseEntity.ok(adsService.addAd(createOrUpdateAd, authentication.getName(), image));
    }

    /**
     * Получает информацию об объявлении по его идентификатору.
     *
     * @param id идентификатор объявления.
     * @return {@link ResponseEntity} с объектом {@link ExtendedAd} и статусом HTTP 200 (OK).
     * @see ExtendedAd
     * @see Operation
     * @see ApiResponse
     */
    @Operation(
            tags = "Объявления",
            summary = "Получение информации об объявлении",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "OK",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = ExtendedAd.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "401",
                            description = "Unauthorized"
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Not found"
                    )
            }
    )
    @GetMapping("/{id}")
    public ResponseEntity<ExtendedAd> getAds(@PathVariable int id) {
        log.info("getAds method from AdsController was invoked for Ad id: {}", id);
        return ResponseEntity.ok(adsService.getAds(id));
    }

    /**
     * Удаляет объявление по его идентификатору.
     * <p>
     * Доступно только администраторам или владельцам объявления.
     * </p>
     *
     * @param id идентификатор объявления.
     * @return {@link ResponseEntity} с пустым телом и статусом HTTP 204 (No Content).
     * @see Operation
     * @see ApiResponse
     * @see PreAuthorize
     */
    @Operation(
            tags = "Объявления",
            summary = "Удаление объявления",
            responses = {
                    @ApiResponse(
                            responseCode = "204",
                            description = "No Content"
                    ),
                    @ApiResponse(
                            responseCode = "401",
                            description = "Unauthorized"
                    ),
                    @ApiResponse(
                            responseCode = "403",
                            description = "Forbidden"
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Not found"
                    )
            }
    )
    @PreAuthorize("hasRole('ADMIN') or @adsServiceImpl.getAds(#id).getEmail() == authentication.principal.username")
    @DeleteMapping("/{id}")
    public ResponseEntity<?> removeAd(@PathVariable int id) {
        log.info("removeAd method from AdsController was invoked for Ad id: {}", id);
        adsService.removeAd(id);
        return ResponseEntity.ok(HttpStatus.NO_CONTENT);
    }

    /**
     * Обновляет информацию об объявлении.
     * <p>
     * Доступно только администраторам или владельцам объявления.
     * </p>
     *
     * @param createOrUpdateAd новые данные для объявления.
     * @param id               идентификатор объявления.
     * @return {@link ResponseEntity} с объектом {@link Ad} и статусом HTTP 200 (OK).
     * @see Ad
     * @see CreateOrUpdateAd
     * @see Operation
     * @see ApiResponse
     * @see PreAuthorize
     */
    @Operation(
            tags = "Объявления",
            summary = "Обновление информации об объявлении",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "OK",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = Ad.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "403",
                            description = "Forbidden"
                    ),
                    @ApiResponse(
                            responseCode = "401",
                            description = "Unauthorized"
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Not found"
                    )
            }
    )
    @PreAuthorize("hasRole('ADMIN') or @adsServiceImpl.getAds(#id).getEmail()==authentication.principal.username")
    @PatchMapping("/{id}")
    public ResponseEntity<Ad> updateAds(@RequestBody CreateOrUpdateAd createOrUpdateAd,
                                        @PathVariable int id) {
        log.info("updateAds method from AdsController was invoked for Ad id: {}", id);
        return ResponseEntity.ok(adsService.updateAds(createOrUpdateAd, id));
    }

    /**
     * Получает объявления текущего авторизованного пользователя.
     *
     * @param authentication объект аутентификации текущего пользователя.
     * @return {@link ResponseEntity} с объектом {@link Ads} и статусом HTTP 200 (OK).
     * @see Ads
     * @see Operation
     * @see ApiResponse
     */
    @Operation(
            tags = "Объявления",
            summary = "Получение объявлений авторизованного пользователя",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "OK",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = Ads.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "401",
                            description = "Unauthorized"
                    )
            }
    )
    @GetMapping("/me")
    public ResponseEntity<Ads> getAdsMe(Authentication authentication) {
        log.info("getAdsMe method from AdsController was invoked for Username: {}", authentication.getName());
        return ResponseEntity.ok(adsService.getAdsMe(authentication.getName()));
    }

    /**
     * Обновляет изображение объявления.
     * <p>
     * Доступно только администраторам или владельцам объявления.
     * </p>
     *
     * @param id    идентификатор объявления.
     * @param image новое изображение.
     * @return {@link ResponseEntity} с пустым телом и статусом HTTP 200 (OK).
     * @throws IOException если возникает ошибка при обработке изображения.
     * @see Operation
     * @see ApiResponse
     */
    @Operation(
            tags = "Объявления",
            summary = "Обновление картинки объявления",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "OK",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_OCTET_STREAM_VALUE,
                                    schema = @Schema(implementation = String[].class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "403",
                            description = "Forbidden"
                    ),
                    @ApiResponse(
                            responseCode = "401",
                            description = "Unauthorized"
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Not found"
                    )
            }
    )
    @PatchMapping(value = "/{id}/image", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> updateImage(@PathVariable int id, @RequestParam MultipartFile image) throws IOException {
        log.info("updateImage method from AdsController was invoked for Ad id: {} and imageSize: {}", id, image.getSize());
        adsService.updateImage(id, image);
        return ResponseEntity.ok(HttpStatus.OK);
    }

    /**
     * Получает изображение объявления по его имени.
     *
     * @param name имя изображения.
     * @return массив байтов, представляющий изображение.
     * @throws IOException если возникает ошибка при чтении изображения.
     * @see Operation
     * @see ApiResponse
     */
    @Operation(
            tags = "Объявления",
            summary = "Получение картинки объявления",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "OK",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_OCTET_STREAM_VALUE,
                                    schema = @Schema(implementation = byte[].class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Not found"
                    )
            }
    )
    @GetMapping(value = "/image/{name}", produces = MediaType.IMAGE_PNG_VALUE)
    public byte[] getImages(@PathVariable String name) throws IOException {
        log.info("getImages method from AdsController was invoked for imageName: {}", name);
        return adsService.getImage(name);
    }

}
