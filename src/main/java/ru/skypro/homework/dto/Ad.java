package ru.skypro.homework.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * Класс DTO, представляющий объявление.
 * <p>
 * Этот класс содержит информацию об объявлении, такую как идентификатор автора, ссылка на картинку,
 * идентификатор объявления, цена и заголовок.
 * </p>
 *
 * @see Data
 * @see Schema
 */
@Data
public class Ad {

    @Schema(description = "id автора объявления")
    private int author;

    @Schema(description = "ссылка на картинку объявления")
    private String image;

    @Schema(description = "id объявления")
    private int pk;

    @Schema(description = "цена объявления")
    private int price;

    @Schema(description = "заголовок объявления")
    private String title;

}
