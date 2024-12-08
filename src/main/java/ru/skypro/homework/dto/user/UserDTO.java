package ru.skypro.homework.dto.user;

import lombok.Data;
import ru.skypro.homework.dto.Role;

import javax.validation.constraints.*;

@Data
public class UserDTO {


    @NotBlank(message = "Имя пользователя не может быть пустым")
    @Size(min = 4, max = 32,
            message = "Имя пользователя должно содержать от 4 до 32 символов")
    @Email(message = "Email адрес должен быть в формате user@example.com")
    private String username;

    @NotBlank(message = "Пароль не может быть пустым")
    @Size(min = 8, max = 16,
            message = "Длина пароля должна быть от 8 до 16 символов")
    private String password;

    @NotBlank(message = "Имя должно быть указано")
    @Size(min = 2, max = 16,
            message = "Имя должно содержать от 2 до 16 символов")
    private String firstName;

    @NotBlank(message = "Должна быть указана фамилия")
    @Size(min = 2, max = 16,
            message = "Фамилия должна содержать от 2 до 16 символов")
    private String lastName;

    @NotBlank(message = "Должен быть указан номер телефона")
    @Pattern(regexp = "\\+7\\s?\\(?\\d{3}\\)?\\s?\\d{3}-?\\d{2}-?\\d{2}"
            ,message = "Телефон должен начинаться с +7 и продолжаться 10 числами")
    private String phone;

    @NotNull
    private Role role;

    private String image;
}
