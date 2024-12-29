package ru.skypro.homework.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.skypro.homework.dto.Comment;
import ru.skypro.homework.dto.CreateOrUpdateComment;
import ru.skypro.homework.entity.CommentEntity;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CommentMapper {

    List<Comment> toListDto(List<CommentEntity> commentEntityList);

    CommentEntity toCommentFromCreateComment(CreateOrUpdateComment createOrUpdateComment);

    @Mapping(target = "author", source = "author.id")
    @Mapping(target = "authorImage", source = "author.image")
    @Mapping(target = "authorFirstName", source = "author.firstName")
    @Mapping(target = "pk", source = "id")
    Comment toCommentDtoFromComment(CommentEntity commentEntity);

}
