package ru.skypro.homework.dto.announce;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class AnnounceDTO {

    @Schema(description = "ID объявления")
    private Long pk;

    @Schema (description = "Имя автора объявления")
    private String authorFirstName;

    @Schema (description = "Фамилия автора объявления")
    private String authorLastName;

    @Schema (description = "Описание товара")
    private String description;

    @Schema (description = "Эл. почта автора объявления")
    private String email;

    @Schema (description = "Картинка объявления")
    private String image;

    @Schema (description = "Номер телефона автора объявления")
    private String phone;

    @Schema (description = "Стоимость товара")
    private Long price;

    @Schema (description = "Заголовок объявления")
    private String title;
}
