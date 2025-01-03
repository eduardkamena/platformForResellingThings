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
        List<CommentEntity> commentEntityList = commentRepository.findAllByAdId(id);
        List<Comment> comments = commentMapper.toListCommentDTOFromListCommentEntity(commentEntityList);
        Comments responseWrapperComment = new Comments();
        responseWrapperComment.setResults(comments);
        responseWrapperComment.setCount(comments.size());
        return responseWrapperComment;
    }

    @Override
    public Comment addComment(int id, CreateOrUpdateComment createOrUpdateComment, String email) {
        AdEntity adEntity = adsRepository.findById(id)
                .orElseThrow(() -> new AdsNotFoundException("AdEntity not found with id: " + id));
        UserEntity author = userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("User not found with email: " + email));

        CommentEntity commentEntity = commentMapper.toCommentEntityFromCreateOrUpdateCommentDTO(createOrUpdateComment);
        commentEntity.setAd(adEntity);
        commentEntity.setCreatedAt(System.currentTimeMillis());
        commentEntity.setAuthor(author);
        commentRepository.save(commentEntity);
        log.info("Added comment with id: {} by user: {} for ad: {}", commentEntity.getId(), email, adEntity.getId());
        return commentMapper.toCommentDTOFromCommentEntity(commentEntity);
    }

    @Override
    @Transactional
    public void deleteComment(int adId, int id) {
        commentRepository.deleteByAdIdAndId(adId, id);
        log.info("Deleted comment with id: {}", id);
    }

    @Override
    public Comment updateComment(int adId, int id, CreateOrUpdateComment createOrUpdateComment) {
        CommentEntity commentEntity = commentRepository.findCommentByIdAndAd_Id(id, adId)
                .orElseThrow(() -> new CommentNotFoundException("CommentEntity not found"));
        commentEntity.setText(createOrUpdateComment.getText());
        commentRepository.save(commentEntity);
        log.info("Updated commentEntity with id: {}", id);
        return commentMapper.toCommentDTOFromCommentEntity(commentEntity);
    }

    @Override
    public String getUserNameOfComment(int id) {
        return commentRepository.findById(id)
                .orElseThrow(() -> new CommentNotFoundException("CommentEntity not found"))
                .getAuthor().getEmail();
    }

}
