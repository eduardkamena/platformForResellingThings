package ru.skypro.homework.dto.announce;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class FullAds {

    @Schema(description = "id объявления")
    @NotNull
    private Integer pk;

    @Schema (description = "имя автора объявления")
    private String authorFirstName;

    @Schema (description = "фамилия автора объявления")
    private String authorLastName;

    @Schema (description = "описание товара")
    private String description;

    @Schema (description = "login автора объявления")
    private String email;

    @Schema (description = "ссылка на картинку объявления")
    private String image;

    @Schema (description = "телефон автора объявления")
    private String phone;

    @Schema (description = "стоимость товара")
    private int price;

    @Schema (description = "заголовок объявления")
    private String title;
}
