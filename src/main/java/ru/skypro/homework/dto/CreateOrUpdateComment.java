package ru.skypro.homework.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

/**
 * Класс DTO, представляющий данные для создания или обновления комментария.
 * <p>
 * Этот класс содержит текст комментария, который валидируется с использованием аннотаций
 * для проверки корректности введенных данных.
 * </p>
 *
 * @see Data
 * @see Schema
 * @see NotBlank
 * @see Size
 */
@Data
public class CreateOrUpdateComment {

    @Schema(description = "текст комментария")
    @NotBlank
    @Size(min = 8, max = 64,
            message = "Комментарий должен содержать от 8 до 64 символов")
    private String text;

}
