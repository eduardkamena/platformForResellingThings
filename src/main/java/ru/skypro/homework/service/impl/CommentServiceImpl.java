package ru.skypro.homework.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.skypro.homework.dto.Role;
import ru.skypro.homework.dto.comment.CommentDTO;
import ru.skypro.homework.dto.comment.CommentsDTO;
import ru.skypro.homework.dto.comment.CreateOrUpdateCommentDTO;
import ru.skypro.homework.entity.Ad;
import ru.skypro.homework.entity.Comment;
import ru.skypro.homework.entity.User;
import ru.skypro.homework.mapper.CommentMapper;
import ru.skypro.homework.mapper.URLPhotoEnum;
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

    /**
     * Метод получает список всех комментариев объявления
     *
     * @param id - id объявления
     * @return возвращает DTO - список моделей комментариев
     */
    @Transactional
    @Override
    public CommentsDTO getCommentsByAdId(Integer id) {
        log.info("Запущен метод сервиса {}", loggingMethod.getMethodName());
        List<CommentDTO> comments = commentRepository.findByAdId(id).stream()
                .map(commentMapper::mapToCommentDto)
                .collect(Collectors.toList());

        return new CommentsDTO(comments.size(), comments);
    }

    /**
     * Метод добавляет комментарий к объявлению
     *
     * @param id                    - id объявления
     * @param createOrUpdateComment - DTO модель класса {@link CreateOrUpdateCommentDTO}
     * @param username              - логин пользователя
     * @return DTO модель комментария
     */
    @Transactional
    @Override
    public CommentDTO createCommentToAdById(Integer id, CreateOrUpdateCommentDTO createOrUpdateComment, String username) {
        log.info("Запущен метод сервиса {}", loggingMethod.getMethodName());

        User author = userService.getUser(username);
        Ad ad = adRepository.findById(id).orElseThrow(() -> new NoSuchElementException("No value present"));

        //Создаем сущность comment и заполняем поля
        Comment entity = new Comment();
        entity.setAuthor(author);
        entity.setAd(ad);
        entity.setText(createOrUpdateComment.getText());
        entity.setCreatedAt(System.currentTimeMillis());

        //Сохраняем сущность commentEntity в БД
        commentRepository.save(entity);

        //Заполняем поле с комментариями у пользователя и сохраняем в БД
        author.getComments().add(entity);
        userRepository.save(author);

        //Создаем возвращаемую сущность ДТО comment и заполняем поля
        CommentDTO commentDTO = new CommentDTO();
        commentDTO.setAuthor(author.getId());

        Integer avatarId = author.getPhoto().getId();
        log.info("id автора комментария - {}", author.getId());
        log.info("URL для получения аватара автора комментария: /photo/image/{}", avatarId);
        commentDTO.setAuthorImage(URLPhotoEnum.URL_PHOTO_CONSTANT.getString() + avatarId);

        commentDTO.setAuthorFirstName(author.getFirstName());
        commentDTO.setCreatedAt(entity.getCreatedAt());
        commentDTO.setPk(commentRepository.findFirstByText(createOrUpdateComment.getText()).getId());
        commentDTO.setText(commentRepository.findFirstByText(createOrUpdateComment.getText()).getText());

        return commentDTO;
    }

    /**
     * Метод удаляет комментарий
     *
     * @param commentId - id комментария
     * @param username  - логин пользователя
     * @return строку с результатом выполнения метода
     */
    @Override
    public String deleteCommentFromAd(Integer commentId, String username) {

        log.info("Запущен метод сервиса {}", loggingMethod.getMethodName());
        Optional<Comment> comment = commentRepository.findById(commentId);
        if (comment.isPresent()) {
            User author = userService.getUser(username);
            if (author.getRole().equals(Role.ADMIN)) {
                commentRepository.delete(comment.get());
                return "комментарий удален";
            } else if (author.getRole().equals(Role.USER)) {
                if (comment.get().getAuthor().getUsername().equals(author.getUsername())) {
                    commentRepository.delete(comment.get());
                    return "комментарий удален";
                } else {
                    return "forbidden"; //'403' For the user deletion is forbidden
                }
            }
        }
        return "not found"; //'404' Comment not found
    }

    /**
     * Метод обновляет комментарий
     *
     * @param commentId             - id комментария
     * @param createOrUpdateComment - DTO модель класса {@link CreateOrUpdateCommentDTO}
     * @param username              - логин пользователя
     * @return строку с результатом выполнения метода
     */
    @Transactional
    @Override
    public CommentDTO updateComment(Integer commentId, CreateOrUpdateCommentDTO createOrUpdateComment, String username) {

        log.info("Запущен метод сервиса {}", loggingMethod.getMethodName());
        Optional<Comment> commentOptional = commentRepository.findById(commentId);
        if (commentOptional.isPresent()) {
            Comment comment = commentOptional.get();
            User author = userService.getUser(username);
            if (author.getComments().contains(comment)) {
                comment.setText(createOrUpdateComment.getText());
                commentRepository.save(comment);
                return commentMapper.mapToCommentDto(comment); //'200' Ok, comment updated
            } else {
                return commentMapper.mapToCommentDto(comment); //'403' For the user update is forbidden
            }
        }
        return null; //'404' Comment not found
    }

}
