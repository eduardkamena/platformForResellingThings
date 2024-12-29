package ru.skypro.homework.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Data
public class UpdateUser {

    @Schema(description = "имя пользователя")
    @NotBlank(message = "Имя должно быть указано")
    @Size(min = 3, max = 10,
            message = "Имя должно содержать от 3 до 10 символов")
    private String firstName;

    @Schema(description = "фамилия пользователя")
    @NotBlank(message = "Должна быть указана фамилия")
    @Size(min = 3, max = 10,
            message = "Фамилия должна содержать от 3 до 10 символов")
    private String lastName;

    @Schema(description = "телефон пользователя")
    @NotBlank(message = "Должен быть указан номер телефона")
    @Pattern(regexp = "\\+7\\s?\\(?\\d{3}\\)?\\s?\\d{3}-?\\d{2}-?\\d{2}",
            message = "Телефон должен начинаться с +7 и продолжаться 10 числами")
    private String phone;

}
