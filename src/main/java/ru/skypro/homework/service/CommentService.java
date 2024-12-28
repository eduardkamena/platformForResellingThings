package ru.skypro.homework.service;

import ru.skypro.homework.dto.comment.CommentDTO;
import ru.skypro.homework.dto.comment.CommentsDTO;
import ru.skypro.homework.dto.comment.CreateOrUpdateCommentDTO;

public interface CommentService {

    CommentsDTO getCommentsByAdId(Integer id);

    CommentDTO createCommentToAdById(Integer id, CreateOrUpdateCommentDTO createOrUpdateComment, String username);

    String deleteCommentFromAd(Integer commentId, String username);

    CommentDTO updateComment(Integer commentId, CreateOrUpdateCommentDTO createOrUpdateComment, String username);

}
