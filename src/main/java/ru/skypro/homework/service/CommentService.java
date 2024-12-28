package ru.skypro.homework.service;

import org.springframework.security.core.Authentication;
import ru.skypro.homework.dto.comment.CommentDTO;
import ru.skypro.homework.dto.comment.CommentsDTO;
import ru.skypro.homework.dto.comment.CreateOrUpdateCommentDTO;
import ru.skypro.homework.entity.Comment;
import ru.skypro.homework.exception.NotFoundException;

public interface CommentService {

    /**
     * Возвращает список комментариев к объявлению.
     *
     *       @param adPk            Идентификатор объявления
     *       @param authentication Аутентификация пользователя
     *       @return Список комментариев к объявлению
     *       @throws NotFoundException Если объявление не найдено
     */
    CommentsDTO getComments(Integer adPk, Authentication authentication);

    /**
     * Добавляет новый комментарий к объявлению.
     *
     *       @param dto           Данные нового комментария
     *       @param authentication Аутентификация пользователя
     *       @return Созданный комментарий
     *       @throws NotFoundException Если объявление не найдено
     */
    CommentDTO addComment(Integer adPk, CreateOrUpdateCommentDTO dto, Authentication authentication);

    /**
     *  Обновляет комментарий.
     *
     *       @param adPk                       Идентификатор объявления
     *       @param commentId                   Идентификатор комментария
     *       @param createOrUpdateCommentDto Данные для обновления комментария
     *       @param authentication             Аутентификация пользователя
     *       @return Обновленный комментарий
     *       @throws NotFoundException Если комментарий не найден
     */
    CommentDTO updateComment(Integer adPk, Integer commentId, CreateOrUpdateCommentDTO createOrUpdateCommentDto, Authentication authentication);

    /**
     * Возвращает комментарий по идентификатору.
     *
     *       @param pk Идентификатор комментария
     *       @return Комментарий
     *       @throws NotFoundException Если комментарий не найден
     */
    Comment getComment(Integer pk);

    /**
     * Удаляет комментарий.
     *
     *       @param adId           Идентификатор объявления
     *       @param commentId      Идентификатор комментария
     *       @param authentication Аутентификация пользователя
     *       @throws NotFoundException Если комментарий не найден
     */
    void deleteComment(Integer adId, Integer commentId, Authentication authentication);

}
