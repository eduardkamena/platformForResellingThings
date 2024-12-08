package ru.skypro.homework.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.validation.constraints.*;

@Data
public class Register {

    @Schema(description = "логин")
    @NotBlank(message = "Имя пользователя не может быть пустым")
    @Size(min = 4, max = 32,
            message = "Имя пользователя должно содержать от 4 до 32 символов")
    @Email(message = "Email адрес должен быть в формате user@example.com")
    private String username;

    @Schema(description = "пароль")
    @NotBlank(message = "Пароль не может быть пустым")
    @Size(min = 8, max = 16,
            message = "Длина пароля должна быть от 8 до 16 символов")
    private String password;

    @Schema(description = "имя пользователя")
    @NotBlank(message = "Имя должно быть указано")
    @Size(min = 2, max = 16,
            message = "Имя должно содержать от 2 до 16 символов")
    private String firstName;

    @Schema(description = "фамилия пользователя")
    @NotBlank(message = "Должна быть указана фамилия")
    @Size(min = 2, max = 16,
            message = "Фамилия должна содержать от 2 до 16 символов")
    private String lastName;

    @Schema(description = "телефон пользователя")
    @NotBlank(message = "Должен быть указан номер телефона")
    @Pattern(regexp = "\\+7\\s?\\(?\\d{3}\\)?\\s?\\d{3}-?\\d{2}-?\\d{2}"
            ,message = "Телефон должен начинаться с +7 и продолжаться 10 числами")
    private String phone;

    @Schema(description = "роль пользователя")
    @Enumerated(EnumType.STRING)
    @NotBlank (message = "Должна быть определена роль пользователя")
    private Role role;
}
