package ru.skypro.homework.dto.user;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import ru.skypro.homework.dto.Role;

import javax.validation.constraints.*;

@Data
public class UserDTO {

    @Schema (description = "Логин пользователя(email)")
    @NotBlank(message = "Имя пользователя не может быть пустым")
    @Size(min = 4, max = 32,
            message = "Имя пользователя должно содержать от 4 до 32 символов")
    @Email(message = "Email адрес должен быть в формате user@example.com")
    private String username;

    @Schema (description = "Пароль")
    @NotBlank(message = "Пароль не может быть пустым")
    @Size(min = 8, max = 16,
            message = "Длина пароля должна быть от 8 до 16 символов")
    private String password;

    @Schema (description = "Имя пользователя")
    @NotBlank(message = "Имя должно быть указано")
    @Size(min = 2, max = 16,
            message = "Имя должно содержать от 2 до 16 символов")
    private String firstName;

    @Schema (description = "Фамилия пользователя")
    @NotBlank(message = "Должна быть указана фамилия")
    @Size(min = 2, max = 16,
            message = "Фамилия должна содержать от 2 до 16 символов")
    private String lastName;

    @Schema (description = "Телефон пользователя")
    @NotBlank(message = "Должен быть указан номер телефона")
    @Pattern(regexp = "\\+7\\s?\\(?\\d{3}\\)?\\s?\\d{3}-?\\d{2}-?\\d{2}"
            ,message = "Телефон должен начинаться с +7 и продолжаться 10 числами")
    private String phone;

    @Schema (description = "Права доступа пользователя")
    @NotNull
    private Role role;

    @Schema (description = "Аватар пользователя")
    private String image;
}
