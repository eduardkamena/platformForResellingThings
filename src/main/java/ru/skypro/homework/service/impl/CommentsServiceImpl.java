package ru.skypro.homework.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.skypro.homework.dto.Comment;
import ru.skypro.homework.dto.Comments;
import ru.skypro.homework.dto.CreateOrUpdateComment;
import ru.skypro.homework.entity.AdEntity;
import ru.skypro.homework.entity.CommentEntity;
import ru.skypro.homework.exception.AdsNotFoundException;
import ru.skypro.homework.exception.CommentNotFoundException;
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
        List<Comment> comments = commentMapper.toListDto(commentEntityList);
        Comments responseWrapperComment = new Comments();
        responseWrapperComment.setResults(comments);
        responseWrapperComment.setCount(comments.size());
        return responseWrapperComment;
    }

    @Override
    public Comment addComment(int id, CreateOrUpdateComment createOrUpdateComment, String email) {
        AdEntity adEntity = adsRepository.findById(id)
                .orElseThrow(() -> new AdsNotFoundException("AdEntity not found"));
        CommentEntity commentEntity = commentMapper.toCommentFromCreateComment(createOrUpdateComment);
        commentEntity.setAd(adEntity);
        commentEntity.setCreatedAt(System.currentTimeMillis());
        commentEntity.setAuthor(userRepository.findByEmail(email).get());
        commentRepository.save(commentEntity);
        log.info("Added commentEntity with id: ", commentEntity.getId());
        return commentMapper.toCommentDtoFromComment(commentEntity);
    }

    @Override
    @Transactional
    public void deleteComment(int adId, int id) {
        commentRepository.deleteByAdIdAndId(adId, id);
        log.info("Deleted comment with id: ", id);
    }

    @Override
    public Comment updateComment(int adId, int id, CreateOrUpdateComment createOrUpdateComment) {
        CommentEntity commentEntity = commentRepository.findCommentByIdAndAd_Id(id, adId)
                .orElseThrow(() -> new CommentNotFoundException("CommentEntity not found"));
        commentEntity.setText(createOrUpdateComment.getText());
        commentRepository.save(commentEntity);
        log.info("Updated commentEntity with id: ", id);
        return commentMapper.toCommentDtoFromComment(commentEntity);
    }

}
