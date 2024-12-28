package ru.skypro.homework.mapper;

import org.springframework.stereotype.Service;
import ru.skypro.homework.dto.comment.CommentDTO;
import ru.skypro.homework.dto.comment.CommentsDTO;
import ru.skypro.homework.dto.comment.CreateOrUpdateCommentDTO;
import ru.skypro.homework.entity.Comment;
import ru.skypro.homework.entity.User;

import java.time.ZoneOffset;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CommentMapper {

    public Comment toEntity (CreateOrUpdateCommentDTO createOrUpdateCommentDto){
        Comment comment = new Comment();
        comment.setText(createOrUpdateCommentDto.getText());
        return comment;
    }

    public CommentDTO toCommentDto(Comment comment){
        CommentDTO commentDto = new CommentDTO();
        commentDto.setPk(comment.getPk());
        commentDto.setCreatedAt(comment.getCreatedAt());
        commentDto.setText(comment.getText());
        User user = comment.getAuthor();
        commentDto.setAuthor(user.getId());
        commentDto.setAuthorFirstName(user.getFirstName());
        commentDto.setAuthorImage("/users/" + user.getUsername() + "/image");
        return commentDto;
    }

    public CommentsDTO toCommentsDto(List<Comment> comments) {
        CommentsDTO commentsDto = new CommentsDTO();
        List<CommentDTO> commentDtoList = comments.stream()
                .map(this::toCommentDto)
                .collect(Collectors.toList());

        commentsDto.setCount(commentDtoList.size());
        commentsDto.setResult(commentDtoList);

        return commentsDto;
    }

}
