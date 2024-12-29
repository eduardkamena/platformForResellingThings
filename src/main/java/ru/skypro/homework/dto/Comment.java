package ru.skypro.homework.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class Comment {

    @Schema(description = "id автора объявления")
    private int author;

    @Schema(description = "ссылка на аватар автора объявления")
    private String authorImage;

    @Schema(description = "имя создателя комментария")
    private String authorFirstName;

    @Schema(description = "дата и время создания комментария в миллисекундах с 00:00:00 01.01.1970")
    private long createdAt;

    @Schema(description = "id комментария")
    private int pk;

    @Schema(description = "текст комментария")
    private String text;

}