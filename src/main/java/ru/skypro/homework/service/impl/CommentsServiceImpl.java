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

@Slf4j
@Service
@RequiredArgsConstructor
public class CommentsServiceImpl implements CommentsService {

    private final AdsRepository adsRepository;
    private final UserRepository userRepository;
    private final CommentRepository commentRepository;
    private final CommentMapper commentMapper;

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

    @Override
    @Transactional
    public void deleteComment(int adId, int id) {
        log.info("deleteComment method from CommentsService was invoked");

        commentRepository.deleteByAdIdAndId(adId, id);

        log.info("Successfully deleted comment with id: {} fot Ad with id: {}", id, adId);
    }

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

    public String getCommentAuthor(int id) {
        log.info("getCommentAuthor method from CommentsService was invoked");

        String email = commentRepository.findById(id)
                .orElseThrow(() -> new CommentNotFoundException("Comment not found with id: " + id))
                .getAuthor().getEmail();

        log.info("Successfully got comment with id: {} by author with email: {}", id, email);
        return email;

    }

}
