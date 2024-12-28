package ru.skypro.homework.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.skypro.homework.dto.Role;
import ru.skypro.homework.dto.comment.CommentDTO;
import ru.skypro.homework.dto.comment.CommentsDTO;
import ru.skypro.homework.dto.comment.CreateOrUpdateCommentDTO;
import ru.skypro.homework.entity.Ad;
import ru.skypro.homework.entity.Comment;
import ru.skypro.homework.entity.User;
import ru.skypro.homework.exception.NotFoundException;
import ru.skypro.homework.mapper.CommentMapper;
import ru.skypro.homework.repository.AdRepository;
import ru.skypro.homework.repository.CommentRepository;
import ru.skypro.homework.repository.UserRepository;
import ru.skypro.homework.service.CommentService;
import ru.skypro.homework.service.LoggingMethod;
import ru.skypro.homework.service.UserService;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;
    private final AdRepository adRepository;
    private final UserRepository userRepository;
    private final CommentMapper commentMapper;
    private final UserService userService;
    private final LoggingMethod loggingMethod;

    @Override
    public CommentsDTO getComments(Integer adPk, Authentication authentication) {
        if (adRepository.existsById(adPk)) {
            return commentMapper.toCommentsDto(commentRepository.findByAdPk(adPk));
        }
        else {
            throw new NotFoundException("Ad is not found");
        }
    }

    public CommentDTO addComment(Integer pk, CreateOrUpdateCommentDTO dto, Authentication authentication) {
        if (adRepository.existsById(pk)){
            User user = userRepository.findByUsername(authentication.getName()).orElseThrow(RuntimeException::new);
            Ad ad = adRepository.findById(pk).orElse(null);
            Comment comment = commentMapper.toEntity(dto);
            comment.setAuthor(user);
            comment.setAd(ad);
            comment.setText(dto.getText());
            comment.setCreatedAt(System.currentTimeMillis());
            return commentMapper.toCommentDto(commentRepository.save(comment));}
        else {
            throw new NotFoundException("Ad is not found");}
    }

    @Override
    public CommentDTO updateComment(Integer adPk,
                                    Integer commentId,
                                    CreateOrUpdateCommentDTO createOrUpdateCommentDto,
                                    Authentication authentication) throws NotFoundException{
        if (commentRepository.existsById(commentId)){
            Comment comment = getComment(commentId);
            comment.setText(createOrUpdateCommentDto.getText());
            return commentMapper.toCommentDto(commentRepository.save(comment));}
        else {
            throw new NotFoundException("Comment is not found");}
    }

    @Override
    public Comment getComment(Integer pk) {
        return commentRepository.findById(pk).orElseThrow();
    }

    @Override
    public void deleteComment(Integer adId, Integer commentId, Authentication authentication) throws NotFoundException {
        if (commentRepository.existsById(commentId)) {
            commentRepository.delete(getComment(commentId));
        } else {
            throw new NotFoundException("Comment is not found");
        }
    }

}
