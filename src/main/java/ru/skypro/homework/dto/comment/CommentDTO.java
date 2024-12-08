package ru.skypro.homework.dto.comment;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class CommentDTO {

    @Schema(description = "id автора объявления")
    @NotNull
    private int author;

    @Schema (description = "ссылка на аватар автора объявления")
    private String authorImage;

    @Schema (description = "дата и время создания комментария" +
            " в миллисекундах с 00:00:00 01.01.1970")
    private int createdAt;

    @Schema (description = "id комментария")
    @NotNull
    private Integer pk;

    @Schema (description = "текст комментария")
    private String text;
}
