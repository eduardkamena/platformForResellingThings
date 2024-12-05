package ru.skypro.homework.dto.comment;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

@Data
public class CommentsListDTO {

    @Schema(description = "Количество комментариев объявления")
    private Long count;

    @Schema (description = "Список всех комментариев объявления")
    private List<CommentDTO> result;
}
