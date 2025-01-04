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
import ru.skypro.homework.dto.Register;
import ru.skypro.homework.dto.Role;
import ru.skypro.homework.service.RegisterService;

import static ru.skypro.homework.dto.Role.USER;

/**
 * Контроллер для управления регистрацией пользователей.
 * <p>
 * Этот контроллер предоставляет REST API для выполнения операций, связанных с регистрацией пользователей,
 * таких как создание нового аккаунта.
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
@Tag(name = "Регистрация")
public class RegisterController {

    private final RegisterService registerService;

    /**
     * Регистрирует нового пользователя.
     * <p>
     * Этот метод принимает данные для регистрации (логин, пароль, имя, фамилию, телефон и роль) и создает новый аккаунт.
     * В случае успешной регистрации возвращается статус HTTP 201 (Created). Если регистрация невозможна
     * (например, пользователь уже существует), возвращается статус HTTP 400 (Bad Request).
     * </p>
     *
     * @param register объект {@link Register}, содержащий данные для регистрации (логин, пароль, имя, фамилию, телефон и роль).
     * @return {@link ResponseEntity} с пустым телом и статусом HTTP 201 (Created) в случае успешной регистрации,
     * или статусом HTTP 400 (Bad Request) в случае неудачи.
     * @see Register
     * @see Role
     * @see Operation
     * @see ApiResponse
     */
    @Operation(
            tags = "Регистрация",
            summary = "Регистрация пользователя",
            responses = {
                    @ApiResponse(
                            responseCode = "201",
                            description = "Created"
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Bad Request"
                    )
            }
    )
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody Register register) {

        log.info("register method from RegisterController was invoked");

        Role role = register.getRole() == null ? USER : register.getRole();
        if (registerService.register(register, role)) {
            return ResponseEntity.status(HttpStatus.CREATED).build();
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

}
