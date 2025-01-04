package ru.skypro.homework.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

/**
 * Класс DTO, представляющий данные для изменения пароля пользователя.
 * <p>
 * Этот класс содержит текущий и новый пароль пользователя, которые валидируются
 * с использованием аннотаций для проверки корректности введенных данных.
 * </p>
 *
 * @see Data
 * @see Schema
 * @see NotBlank
 * @see Size
 */
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
