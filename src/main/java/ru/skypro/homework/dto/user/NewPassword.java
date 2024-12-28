package ru.skypro.homework.dto.user;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
public class NewPassword {

    @NotBlank(message = "Пароль не может быть пустым")
    @Size(min = 8, max = 16,
            message = "Длина пароля должна быть от 8 до 16 символов")
    @Schema(description = "текущий пароль")
    private String currentPassword;

    @NotBlank(message = "Пароль не может быть пустым")
    @Size(min = 8, max = 16,
            message = "Длина пароля должна быть от 8 до 16 символов")
    @Schema(description = "новый пароль")
    private String newPassword;
}
