package ru.skypro.homework.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
public class CreateOrUpdateComment {

    @Schema(description = "текст комментария")
    @NotBlank
    @Size(min = 8, max = 64,
            message = "Комментарий должен содержать от 8 до 64 символов")
    private String text;

}