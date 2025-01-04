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
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.dto.NewPassword;
import ru.skypro.homework.dto.UpdateUser;
import ru.skypro.homework.dto.User;
import ru.skypro.homework.service.UserService;

import java.io.IOException;

/**
 * Контроллер для управления пользователями.
 * <p>
 * Этот контроллер предоставляет REST API для выполнения операций, связанных с пользователями,
 * таких как обновление пароля, получение информации о пользователе, обновление данных пользователя
 * и управление аватаром пользователя.
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
@RequestMapping("/users")
@Tag(name = "Пользователи")
public class UsersController {

    private final UserService userService;

    /**
     * Обновляет пароль авторизованного пользователя.
     *
     * @param newPassword    объект {@link NewPassword}, содержащий новый пароль.
     * @param authentication объект аутентификации текущего пользователя.
     * @return {@link ResponseEntity} с объектом {@link NewPassword} и статусом HTTP 200 (OK).
     * @see NewPassword
     * @see Operation
     * @see ApiResponse
     */
    @Operation(
            tags = "Пользователи",
            summary = "Обновление пароля",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "OK"
                    ),
                    @ApiResponse(
                            responseCode = "401",
                            description = "Unauthorized"
                    ),
                    @ApiResponse(
                            responseCode = "403",
                            description = "Forbidden"
                    )
            }
    )
    @PostMapping("/set_password")
    public ResponseEntity<NewPassword> setPassword(@RequestBody NewPassword newPassword,
                                                   Authentication authentication) {
        log.info("setPassword method from UsersController was invoked for User: {}", authentication.getName());
        userService.setPassword(newPassword, authentication.getName());
        return ResponseEntity.ok(newPassword);
    }

    /**
     * Получает информацию об авторизованном пользователе.
     *
     * @param authentication объект аутентификации текущего пользователя.
     * @return {@link ResponseEntity} с объектом {@link User} и статусом HTTP 200 (OK).
     * @see User
     * @see Operation
     * @see ApiResponse
     */
    @Operation(
            tags = "Пользователи",
            summary = "Получение информации об авторизованном пользователе",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "OK",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = User.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "401",
                            description = "Unauthorized"
                    )
            }
    )
    @GetMapping("/me")
    public ResponseEntity<User> getUser(Authentication authentication) {
        log.info("getUser method from UsersController was invoked for User: {}", authentication.getName());
        return ResponseEntity.ok(userService.getUser(authentication.getName()));
    }

    /**
     * Обновляет информацию об авторизованном пользователе.
     *
     * @param user           объект {@link User}, содержащий новые данные пользователя.
     * @param authentication объект аутентификации текущего пользователя.
     * @return {@link ResponseEntity} с объектом {@link User} и статусом HTTP 200 (OK).
     * @see User
     * @see Operation
     * @see ApiResponse
     */
    @Operation(
            tags = "Пользователи",
            summary = "Обновление информации об авторизованном пользователе",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "OK",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = UpdateUser.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "401",
                            description = "Unauthorized"
                    )
            }
    )
    @PatchMapping("/me")
    public ResponseEntity<User> updateUser(@RequestBody User user,
                                           Authentication authentication) {
        log.info("updateUser method from UsersController was invoked for User: {}", authentication.getName());
        return ResponseEntity.ok(userService.updateUser(user, authentication.getName()));
    }

    /**
     * Обновляет аватар авторизованного пользователя.
     *
     * @param image          файл изображения для обновления аватара.
     * @param authentication объект аутентификации текущего пользователя.
     * @return {@link ResponseEntity} с пустым телом и статусом HTTP 200 (OK).
     * @throws IOException если возникает ошибка при обработке изображения.
     * @see Operation
     * @see ApiResponse
     */
    @Operation(
            tags = "Пользователи",
            summary = "Обновление аватара авторизованного пользователя",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "OK"
                    ),
                    @ApiResponse(
                            responseCode = "401",
                            description = "Unauthorized"
                    )
            }
    )
    @PatchMapping(value = "/me/image", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> updateUserImage(@RequestParam MultipartFile image,
                                             Authentication authentication) throws IOException {
        log.info("updateUserImage method from UsersController was invoked for User: {} with ImageSize: {}", authentication.getName(), image.getSize());
        userService.updateAvatar(image, authentication.getName());
        return ResponseEntity.ok(HttpStatus.OK);
    }

    /**
     * Получает изображение пользователя по его имени.
     *
     * @param name имя изображения.
     * @return массив байтов, представляющий изображение.
     * @throws IOException если возникает ошибка при чтении изображения.
     * @see Operation
     * @see ApiResponse
     */
    @Operation(
            tags = "Пользователи",
            summary = "Получение картинки пользователя",
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
    @GetMapping(value = "/image/{name}", produces = {MediaType.IMAGE_PNG_VALUE})
    public byte[] getImages(@PathVariable String name) throws IOException {
        log.info("getImages method from UsersController was invoked for Image: {}", name);
        return userService.getImage(name);
    }

}
