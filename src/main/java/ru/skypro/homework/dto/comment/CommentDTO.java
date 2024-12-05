package ru.skypro.homework.dto.comment;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class CommentDTO {

    @Schema(description = "ID автора объявления")
    private Long author;

    @Schema (description = "Аватар автора объявления")
    private String image;

    @Schema (description = "ID комментария")
    private Long pk;

    @Schema (description = "Дата и время создания комментария")
    private Long createdAt;

    @Schema (description = "Текст комментария")
    private String text;
}
