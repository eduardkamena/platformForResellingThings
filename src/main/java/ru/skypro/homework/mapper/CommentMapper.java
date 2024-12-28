package ru.skypro.homework.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import ru.skypro.homework.dto.comment.CommentDto;
import ru.skypro.homework.dto.comment.CreateComment;
import ru.skypro.homework.entity.Comment;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CommentMapper {

    List<CommentDto> toListDto(List<Comment> commentList);

    Comment toCommentFromCreateComment(CreateComment createComment);

    @Mapping(target = "author", source = "user.id")
    @Mapping(target = "authorImage", source = "user.image")
    @Mapping(target = "authorFirstName", source = "user.firstName")
    @Mapping(target = "pk", source = "id")
    CommentDto toCommentDtoFromComment(Comment comment);

    @Mapping(target = "user.id", ignore = true)
    @Mapping(target = "user.image",ignore = true)
    @Mapping(target = "user.firstName",ignore = true)
    void updateCommentFromCommentDto(CommentDto commentDto,@MappingTarget Comment comment);

}
