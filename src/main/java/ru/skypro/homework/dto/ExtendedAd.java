package ru.skypro.homework.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * Класс DTO, представляющий расширенную информацию об объявлении.
 * <p>
 * Этот класс содержит подробные данные об объявлении, включая информацию об авторе,
 * описание товара, контактные данные, изображение, стоимость и заголовок.
 * </p>
 *
 * @see Data
 * @see Schema
 */
@Data
public class ExtendedAd {

    @Schema(description = "id объявления")
    private int pk;

    @Schema(description = "имя автора объявления")
    private String authorFirstName;

    @Schema(description = "фамилия автора объявления")
    private String authorLastName;

    @Schema(description = "описание товара")
    private String description;

    @Schema(description = "login автора объявления")
    private String email;

    @Schema(description = "ссылка на картинку объявления")
    private String image;

    @Schema(description = "телефон автора объявления")
    private String phone;

    @Schema(description = "стоимость товара")
    private int price;

    @Schema(description = "заголовок объявления")
    private String title;

}
