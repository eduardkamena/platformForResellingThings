package ru.skypro.homework.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.skypro.homework.dto.Comment;
import ru.skypro.homework.dto.CreateOrUpdateComment;
import ru.skypro.homework.entity.CommentEntity;

import java.util.List;

/**
 * Интерфейс для маппинга между сущностями и DTO, связанными с комментариями.
 * <p>
 * Этот интерфейс использует MapStruct для преобразования объектов между сущностями (CommentEntity)
 * и DTO (Comment, CreateOrUpdateComment).
 * </p>
 *
 * @see Mapper
 * @see Mapping
 */
@Mapper(componentModel = "spring")
public interface CommentMapper {

    /**
     * Преобразует список сущностей CommentEntity в список DTO Comment.
     *
     * @param commentEntityList список сущностей комментариев.
     * @return список DTO Comment.
     */
    List<Comment> toListCommentDTOFromListCommentEntity(List<CommentEntity> commentEntityList);

    /**
     * Преобразует объект CreateOrUpdateComment в сущность CommentEntity.
     *
     * @param createOrUpdateComment DTO для создания или обновления комментария.
     * @return сущность CommentEntity.
     */
    CommentEntity toCommentEntityFromCreateOrUpdateCommentDTO(CreateOrUpdateComment createOrUpdateComment);

    /**
     * Преобразует сущность CommentEntity в DTO Comment.
     *
     * @param commentEntity сущность комментария.
     * @return DTO Comment.
     */
    @Mapping(target = "author", source = "author.id")
    @Mapping(target = "authorImage", source = "author.image")
    @Mapping(target = "authorFirstName", source = "author.firstName")
    @Mapping(target = "pk", source = "id")
    Comment toCommentDTOFromCommentEntity(CommentEntity commentEntity);

}
