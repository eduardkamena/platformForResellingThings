package ru.skypro.homework.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * Класс, представляющий объявление.
 * <p>
 * Этот класс содержит информацию об объявлении, такую как идентификатор автора, ссылка на изображение,
 * идентификатор объявления, цена и заголовок.
 * </p>
 *
 * @see Data
 * @see Schema
 */
@Data
public class Ad {

    /**
     * Идентификатор автора объявления.
     */
    @Schema(description = "id автора объявления")
    private int author;

    /**
     * Ссылка на изображение объявления.
     */
    @Schema(description = "ссылка на картинку объявления")
    private String image;

    /**
     * Идентификатор объявления.
     */
    @Schema(description = "id объявления")
    private int pk;

    /**
     * Цена объявления.
     */
    @Schema(description = "цена объявления")
    private int price;

    /**
     * Заголовок объявления.
     */
    @Schema(description = "заголовок объявления")
    private String title;

}