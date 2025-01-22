package ru.skypro.homework.service.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
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

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CommentsServiceImplMockMvcTest {

    @Mock
    private AdsRepository adsRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private CommentRepository commentRepository;

    @Mock
    private CommentMapper commentMapper;

    @InjectMocks
    private CommentsServiceImpl commentsService;

    private AdEntity adEntity;
    private UserEntity userEntity;
    private CommentEntity commentEntity;
    private CreateOrUpdateComment createOrUpdateComment;

    @BeforeEach
    public void setUp() {
        adEntity = new AdEntity();
        adEntity.setId(1);

        userEntity = new UserEntity();
        userEntity.setEmail("test@mail.ru");

        commentEntity = new CommentEntity();
        commentEntity.setId(1);
        commentEntity.setAd(adEntity);
        commentEntity.setAuthor(userEntity);
        commentEntity.setText("Дорого!");

        createOrUpdateComment = new CreateOrUpdateComment();
        createOrUpdateComment.setText("Очень дорого!!");
    }

    @Test
    public void shouldGetComments() {
        // given
        when(commentRepository.findAllByAdId(1)).thenReturn(List.of(commentEntity));
        when(commentMapper.toListCommentDTOFromListCommentEntity(List.of(commentEntity)))
                .thenReturn(List.of(new Comment()));

        // when
        Comments result = commentsService.getComments(1);

        // then
        assertNotNull(result);
        assertEquals(1, result.getCount());
        verify(commentRepository, times(1)).findAllByAdId(1);
    }

    @Test
    public void shouldAddComment() {
        // given
        when(adsRepository.findById(1)).thenReturn(Optional.of(adEntity));
        when(userRepository.findByEmail("test@mail.ru")).thenReturn(Optional.of(userEntity));
        when(commentMapper.toCommentEntityFromCreateOrUpdateCommentDTO(createOrUpdateComment))
                .thenReturn(commentEntity);
        when(commentMapper.toCommentDTOFromCommentEntity(commentEntity)).thenReturn(new Comment());

        // when
        Comment result = commentsService.addComment(1, createOrUpdateComment, "test@mail.ru");

        // then
        assertNotNull(result);
        verify(commentRepository, times(1)).save(commentEntity);
    }

    @Test
    public void shouldThrowAddCommentWhenAdNotFound() {
        // given
        when(adsRepository.findById(1)).thenReturn(Optional.empty());

        // when / then
        assertThrows(AdsNotFoundException.class, () -> commentsService.addComment(1, createOrUpdateComment, "test@mail.ru"));
    }

    @Test
    public void shouldThrowAddCommentWhenUserNotFound() {
        // given
        when(adsRepository.findById(1)).thenReturn(Optional.of(adEntity));
        when(userRepository.findByEmail("test@mail.ru")).thenReturn(Optional.empty());

        // when / then
        assertThrows(UserNotFoundException.class, () -> commentsService.addComment(1, createOrUpdateComment, "test@mail.ru"));
    }

    @Test
    public void shouldDeleteComment() {
        // given
        doNothing().when(commentRepository).deleteByAdIdAndId(1, 1);

        // when
        commentsService.deleteComment(1, 1);

        // then
        verify(commentRepository, times(1)).deleteByAdIdAndId(1, 1);
    }

    @Test
    public void shouldUpdateComment() {
        // given
        when(commentRepository.findCommentByIdAndAd_Id(1, 1)).thenReturn(Optional.of(commentEntity));
        when(commentMapper.toCommentDTOFromCommentEntity(commentEntity)).thenReturn(new Comment());

        // when
        Comment result = commentsService.updateComment(1, 1, createOrUpdateComment);

        // then
        assertNotNull(result);
        assertEquals("Очень дорого!!", commentEntity.getText());
        verify(commentRepository, times(1)).save(commentEntity);
    }

    @Test
    public void shouldThrowUpdateCommentWhenCommentNotFound() {
        // given
        when(commentRepository.findCommentByIdAndAd_Id(1, 1)).thenReturn(Optional.empty());

        // when / then
        assertThrows(CommentNotFoundException.class, () -> commentsService.updateComment(1, 1, createOrUpdateComment));
    }

    @Test
    public void shouldGetCommentAuthor() {
        // given
        when(commentRepository.findById(1)).thenReturn(Optional.of(commentEntity));

        // when
        String result = commentsService.getCommentAuthor(1);

        // then
        assertEquals("test@mail.ru", result);
    }

    @Test
    public void shouldThrowGetCommentAuthorWhenCommentNotFound() {
        // given
        when(commentRepository.findById(1)).thenReturn(Optional.empty());

        // when / then
        assertThrows(CommentNotFoundException.class, () -> commentsService.getCommentAuthor(1));
    }

}
