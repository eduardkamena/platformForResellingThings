package ru.skypro.homework.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.dto.user.NewPasswordDTO;
import ru.skypro.homework.dto.user.UpdateUserDTO;
import ru.skypro.homework.dto.user.UserDTO;
import ru.skypro.homework.entity.User;
import ru.skypro.homework.mapper.UserMapper;
import ru.skypro.homework.service.LoggingMethod;
import ru.skypro.homework.service.UserService;

import javax.validation.Valid;
import java.io.IOException;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/users")
@CrossOrigin(origins = "http://localhost:3000")
public class UserController {

    private final UserService userService;
    private final LoggingMethod loggingMethod;

    @Operation(
            summary = "Обновление пароля", tags = "Пользователи",
            responses = {
                    @ApiResponse(responseCode = "200", description = "OK", content = @Content()),
                    @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content()),
                    @ApiResponse(responseCode = "403", description = "Forbidden", content = @Content()),
                    @ApiResponse(responseCode = "404", description = "Not found", content = @Content())
            }
    )
    @PostMapping("/set_password") // http://localhost:8080/users/set_password
    public ResponseEntity<Void> setPassword(@RequestBody @Valid NewPasswordDTO newPass,
                                            Authentication authentication) {

        log.info("За запущен метод контроллера: {}", loggingMethod.getMethodName());

        userService.setPassword(newPass, authentication);
        return ResponseEntity.ok().build();
    }

    @Operation(
            summary = "Получение информации об авторизованном пользователе", tags = "Пользователи",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Пользователь найден",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = UserDTO.class))),
                    @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content()),
                    @ApiResponse(responseCode = "403", description = "Forbidden", content = @Content()),
                    @ApiResponse(responseCode = "404", description = "Not found", content = @Content())
            }
    )
    @GetMapping("/me") // http://localhost:8080/users/me
    public ResponseEntity<UserDTO> getUser(Authentication authentication) {

        log.info("Запущен метод контроллера: {}", loggingMethod.getMethodName());

        User user = userService.getUser(authentication.getName());
        if (user != null) {
            return ResponseEntity.ok(UserMapper.mapFromUserEntityToUserDTO(user));
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    @Operation(
            summary = "Обновление информации об авторизованном пользователе", tags = "Пользователи",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Пользователь найден",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = UpdateUserDTO.class))),
                    @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content()),
            }
    )
    @PatchMapping("/me") // http://localhost:8080/users/me
    public ResponseEntity<UpdateUserDTO> updateUser(@RequestBody @Valid UpdateUserDTO updateUser,
                                                    Authentication authentication) {

        log.info("Запущен метод контроллера: {}", loggingMethod.getMethodName());

        User user = userService.updateUser(updateUser, authentication);
        if (user != null) {
            return ResponseEntity.ok(UserMapper.mapFromUserEntityToUpdateUserDTO(user));
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    @Operation(
            summary = "Обновление аватара авторизованного пользователя", tags = "Пользователи",
            responses = {
                    @ApiResponse(responseCode = "200", description = "OK", content = @Content()),
                    @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content()),
            }
    )
    @PatchMapping(value = "/me/image", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Void> updateUserImage(@RequestParam MultipartFile image,
                                                Authentication authentication) throws IOException {

        log.info("За запущен метод контроллера: {}", loggingMethod.getMethodName());

        userService.updateUserImage(image, authentication);
        return ResponseEntity.ok().build();
    }


    // закомментировал методы требующие проверки

//    @GetMapping(path = "/me")
//    public UserDTO getCurrentUserInfo(Authentication authentication) {
//        return userService.findUserByUsername(authentication.getName());
//    }
//
//    @PostMapping(path = "/set_password")
//    public void setPassword(@RequestBody @Valid NewPasswordDTO newPasswordDTO,
//                                         Authentication authentication) {
//        userService.changePassword(newPasswordDTO, authentication.getName());
//    }
}
