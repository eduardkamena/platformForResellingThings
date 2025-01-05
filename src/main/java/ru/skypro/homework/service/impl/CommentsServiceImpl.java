package ru.skypro.homework.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.skypro.homework.dto.Comment;
import ru.skypro.homework.dto.Comments;
import ru.skypro.homework.dto.CreateOrUpdateComment;
import ru.skypro.homework.entity.AdEntity;
import ru.skypro.homework.entity.CommentEntity;
import ru.skypro.homework.entity.UserEntity;
import ru.skypro.homework.exception.AdsNotFoundException;
import ru.skypro.homework.exception.CommentNotFoundException;
import ru.skypro.homework.exception.UserNotFoundException;
import ru.skypro.homework.mapper.CommentMapper;
import ru.skypro.homework.repository.AdsRepository;
import ru.skypro.homework.repository.CommentRepository;
import ru.skypro.homework.repository.UserRepository;
import ru.skypro.homework.service.CommentsService;

import javax.transaction.Transactional;
import java.util.List;

/**
 * Сервис для работы с комментариями.
 * <p>
 * Этот класс реализует интерфейс {@link CommentsService} и предоставляет методы для выполнения операций
 * с комментариями, таких как получение всех комментариев, добавление, обновление и удаление комментариев.
 * </p>
 *
 * @see Service
 * @see Slf4j
 * @see RequiredArgsConstructor
 * @see CommentsService
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class CommentsServiceImpl implements CommentsService {

    private final AdsRepository adsRepository;
    private final UserRepository userRepository;
    private final CommentRepository commentRepository;
    private final CommentMapper commentMapper;

    /**
     * Получает все комментарии для указанного объявления.
     *
     * @param id идентификатор объявления.
     * @return объект {@link Comments}, содержащий список комментариев и их количество.
     */
    @Override
    public Comments getComments(int id) {
        log.info("getComments method from CommentsService was invoked");

        List<CommentEntity> commentEntityList = commentRepository.findAllByAdId(id);
        List<Comment> commentsDTOList = commentMapper.toListCommentDTOFromListCommentEntity(commentEntityList);
        Comments commentsDTO = new Comments();
        commentsDTO.setResults(commentsDTOList);
        commentsDTO.setCount(commentsDTOList.size());

        log.info("Successfully got all Comments for Ad with id: {}", id);
        return commentsDTO;
    }

    /**
     * Добавляет новый комментарий к указанному объявлению.
     *
     * @param id                    идентификатор объявления.
     * @param createOrUpdateComment данные для создания комментария.
     * @param email                 email пользователя, добавляющего комментарий.
     * @return объект {@link Comment}, представляющий созданный комментарий.
     * @throws AdsNotFoundException  если объявление с указанным идентификатором не найдено.
     * @throws UserNotFoundException если пользователь с указанным email не найден.
     */
    @Override
    public Comment addComment(int id, CreateOrUpdateComment createOrUpdateComment, String email) {
        log.info("addComment method from CommentsService was invoked");

        AdEntity adEntity = adsRepository.findById(id)
                .orElseThrow(() -> new AdsNotFoundException("Ad not found with id: " + id));
        UserEntity author = userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("User not found with email: " + email));

        CommentEntity commentEntity = commentMapper.toCommentEntityFromCreateOrUpdateCommentDTO(createOrUpdateComment);
        commentEntity.setAd(adEntity);
        commentEntity.setCreatedAt(System.currentTimeMillis());
        commentEntity.setAuthor(author);
        commentRepository.save(commentEntity);

        log.info("Successfully added comment with id: {} by user: {} for ad: {}",
                commentEntity.getId(), email, adEntity.getId());
        return commentMapper.toCommentDTOFromCommentEntity(commentEntity);
    }

    /**
     * Удаляет комментарий по идентификатору объявления и комментария.
     *
     * @param adId идентификатор объявления.
     * @param id   идентификатор комментария.
     */
    @Override
    @Transactional
    public void deleteComment(int adId, int id) {
        log.info("deleteComment method from CommentsService was invoked");

        commentRepository.deleteByAdIdAndId(adId, id);

        log.info("Successfully deleted comment with id: {} fot Ad with id: {}", id, adId);
    }

    /**
     * Обновляет текст комментария.
     *
     * @param adId                  идентификатор объявления.
     * @param id                    идентификатор комментария.
     * @param createOrUpdateComment новые данные для комментария.
     * @return объект {@link Comment}, представляющий обновленный комментарий.
     * @throws CommentNotFoundException если комментарий с указанным идентификатором не найден.
     */
    @Override
    public Comment updateComment(int adId, int id, CreateOrUpdateComment createOrUpdateComment) {
        log.info("updateComment method from CommentsService was invoked");

        CommentEntity commentEntity = commentRepository.findCommentByIdAndAd_Id(id, adId)
                .orElseThrow(() -> new CommentNotFoundException("Comment not found with id: " + id));
        commentEntity.setText(createOrUpdateComment.getText());
        commentRepository.save(commentEntity);

        log.info("Successfully updated comment with id: {} for Ad with id: {}", id, adId);
        return commentMapper.toCommentDTOFromCommentEntity(commentEntity);
    }

    /**
     * Получает email автора комментария.
     *
     * @param id идентификатор комментария.
     * @return email автора комментария.
     * @throws CommentNotFoundException если комментарий с указанным идентификатором не найден.
     */
    public String getCommentAuthor(int id) {
        log.info("getCommentAuthor method from CommentsService was invoked");

        String email = commentRepository.findById(id)
                .orElseThrow(() -> new CommentNotFoundException("Comment not found with id: " + id))
                .getAuthor().getEmail();

        log.info("Successfully got comment with id: {} by author with email: {}", id, email);
        return email;

    }

}
