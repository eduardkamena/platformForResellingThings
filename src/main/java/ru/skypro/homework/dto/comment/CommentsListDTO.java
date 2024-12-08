package ru.skypro.homework.dto.comment;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

@Data
public class CommentsListDTO {

    @Schema(description = "общее количество комментариев")
    private int count;

    @Schema (description = "список всех комментариев объявления")
    private List<CommentDTO> result;
}
