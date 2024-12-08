package ru.skypro.homework.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class ImageDTO {

    @Schema(description = "ссылка на картинку")
    @NotBlank(message = "Должна быть указана ссылка на " +
            "картинку или аватар пользователя")
    private String url;

}