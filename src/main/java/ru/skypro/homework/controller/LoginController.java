package ru.skypro.homework.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import ru.skypro.homework.dto.Login;
import ru.skypro.homework.service.LoginService;

/**
 * Контроллер для управления авторизацией пользователей.
 * <p>
 * Этот контроллер предоставляет REST API для выполнения операций, связанных с авторизацией пользователей,
 * таких как вход в систему.
 * </p>
 *
 * @see RestController
 * @see Tag
 * @see CrossOrigin
 * @see Slf4j
 */
@Slf4j
@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequiredArgsConstructor
@Tag(name = "Авторизация")
public class LoginController {

    private final LoginService loginService;

    /**
     * Выполняет авторизацию пользователя.
     * <p>
     * Этот метод проверяет учетные данные пользователя (логин и пароль) и возвращает статус HTTP 200 (OK),
     * если авторизация прошла успешно. В случае неудачи возвращается статус HTTP 401 (Unauthorized).
     * </p>
     *
     * @param login объект {@link Login}, содержащий логин и пароль пользователя.
     * @return {@link ResponseEntity} с пустым телом и статусом HTTP 200 (OK) в случае успешной авторизации,
     * или статусом HTTP 401 (Unauthorized) в случае неудачи.
     * @see Login
     * @see Operation
     * @see ApiResponse
     */
    @Operation(
            tags = "Авторизация",
            summary = "Авторизация пользователя",
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
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Login login) {

        log.info("login method from LoginController was invoked");

        if (loginService.login(login.getUsername(), login.getPassword())) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

}
