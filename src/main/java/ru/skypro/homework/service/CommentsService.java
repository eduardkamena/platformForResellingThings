package ru.skypro.homework.service;

import ru.skypro.homework.dto.Comment;
import ru.skypro.homework.dto.Comments;
import ru.skypro.homework.dto.CreateOrUpdateComment;

/**
 * Интерфейс для работы с комментариями.
 * <p>
 * Этот интерфейс предоставляет методы для выполнения операций с комментариями,
 * таких как получение всех комментариев, добавление, обновление и удаление комментариев,
 * а также получение автора комментария.
 * </p>
 */
public interface CommentsService {

    /**
     * Получает все комментарии для указанного объявления.
     *
     * @param id идентификатор объявления.
     * @return объект {@link Comments}, содержащий список комментариев и их количество.
     */
    Comments getComments(int id);

    /**
     * Добавляет новый комментарий к указанному объявлению.
     *
     * @param id                    идентификатор объявления.
     * @param createOrUpdateComment данные для создания комментария.
     * @param email                 email пользователя, добавляющего комментарий.
     * @return объект {@link Comment}, представляющий созданный комментарий.
     */
    Comment addComment(int id, CreateOrUpdateComment createOrUpdateComment, String email);

    /**
     * Удаляет комментарий по идентификатору объявления и комментария.
     *
     * @param adId идентификатор объявления.
     * @param id   идентификатор комментария.
     */
    void deleteComment(int adId, int id);

    /**
     * Обновляет текст комментария.
     *
     * @param adId                  идентификатор объявления.
     * @param id                    идентификатор комментария.
     * @param createOrUpdateComment новые данные для комментария.
     * @return объект {@link Comment}, представляющий обновленный комментарий.
     */
    Comment updateComment(int adId, int id, CreateOrUpdateComment createOrUpdateComment);

    /**
     * Получает email автора комментария.
     *
     * @param id идентификатор комментария.
     * @return email автора комментария.
     */
    String getCommentAuthor(int id);

}
