package ru.skypro.homework.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

/**
 * Класс DTO, представляющий данные для создания или обновления объявления.
 * <p>
 * Этот класс содержит информацию, необходимую для создания или обновления объявления,
 * такую как заголовок, цена и описание. Поля валидируются с использованием аннотаций
 * для проверки корректности введенных данных.
 * </p>
 *
 * @see Data
 * @see Schema
 * @see NotBlank
 * @see Size
 */
@Data
public class CreateOrUpdateAd {

    @Schema(description = "заголовок объявления")
    @NotBlank(message = "Должен быть указан заголовок")
    @Size(min = 4, max = 32,
            message = "Заголовок должен содержать от 4 до 32 символов")
    private String title;

    @Schema(description = "цена объявления")
    @NotBlank(message = "Должна быть указана стоимость")
    @Size(max = 10000000,
            message = "Цена должна быть указана в диапазоне от 0 до 10_000_000")
    private int price;

    @Schema(description = "описание объявления")
    @NotBlank(message = "Должно быть указано описание")
    @Size(min = 8, max = 64,
            message = "Описание должно содержать от 8 до 64 символов")
    private String description;

}
