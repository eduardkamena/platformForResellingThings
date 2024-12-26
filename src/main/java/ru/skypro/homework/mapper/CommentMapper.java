package ru.skypro.homework.mapper;

import org.springframework.stereotype.Service;
import ru.skypro.homework.dto.comment.CommentDTO;
import ru.skypro.homework.entity.Comment;

import java.time.ZoneOffset;

@Service
public class CommentMapper {

    /**
     * Entity -> DTO mapping
     *
     * @param entity input entity class
     * @return DTO class
     */
    public CommentDTO mapToCommentDto(Comment entity) {
        CommentDTO dto = new CommentDTO();
        dto.setAuthor(entity.getAuthor().getId());
        dto.setAuthorImage(URLPhotoEnum.URL_PHOTO_CONSTANT.getString() + entity.getAuthor().getPhoto().getId());
        dto.setAuthorFirstName(entity.getAuthor().getFirstName());
        dto.setCreatedAt(entity.getCreatedAt());
        dto.setPk(entity.getId());
        dto.setText(entity.getText());
        return dto;
    }

}
