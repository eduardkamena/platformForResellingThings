package ru.skypro.homework.service;

import ru.skypro.homework.dto.Comment;
import ru.skypro.homework.dto.Comments;
import ru.skypro.homework.dto.CreateOrUpdateComment;

public interface CommentsService {

    Comments getComments(int id);

    Comment addComment(int id, CreateOrUpdateComment createOrUpdateComment, String email);

    void deleteComment(int adId, int id);

    Comment updateComment(int adId, int id, CreateOrUpdateComment createOrUpdateComment);

    String getCommentAuthor(int id);

}
