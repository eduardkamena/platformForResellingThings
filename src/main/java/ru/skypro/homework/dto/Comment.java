package ru.skypro.homework.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * Класс DTO, представляющий комментарий к объявлению.
 * <p>
 * Этот класс содержит информацию о комментарии, такую как идентификатор автора, ссылка на аватар автора,
 * имя автора, дата и время создания комментария в миллисекундах с 00:00:00 01.01.1970,
 * идентификатор комментария и текст комментария.
 * </p>
 *
 * @see Data
 * @see Schema
 */
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
