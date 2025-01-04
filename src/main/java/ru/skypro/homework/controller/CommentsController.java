package ru.skypro.homework.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import ru.skypro.homework.dto.Comment;
import ru.skypro.homework.dto.CreateOrUpdateComment;
import ru.skypro.homework.dto.Comments;
import ru.skypro.homework.service.CommentsService;

/**
 * Контроллер для управления комментариями к объявлениям.
 * <p>
 * Этот контроллер предоставляет REST API для выполнения операций с комментариями, таких как:
 * получение комментариев к объявлению, добавление нового комментария, обновление и удаление существующих комментариев.
 * </p>
 *
 * @see RestController
 * @see RequestMapping
 * @see Tag
 * @see CrossOrigin
 * @see Slf4j
 */
@Slf4j
@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequiredArgsConstructor
@RequestMapping("/ads")
@Tag(name = "Комментарии")
public class CommentsController {

    private final CommentsService commentsService;

    /**
     * Получает все комментарии для указанного объявления.
     *
     * @param id идентификатор объявления.
     * @return {@link ResponseEntity} с объектом {@link Comments}, содержащим список комментариев, и статусом HTTP 200 (OK).
     * @see Comments
     * @see Operation
     * @see ApiResponse
     */
    @Operation(
            tags = "Комментарии",
            summary = "Получение комментариев объявления",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "OK",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = Comments.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "401",
                            description = "Unauthorized"
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Not found"
                    )
            }
    )
    @GetMapping("/{id}/comments")
    public ResponseEntity<Comments> getComments(@PathVariable int id) {
        log.info("getComments method from CommentsController was invoked");
        return ResponseEntity.ok(commentsService.getComments(id));
    }

    /**
     * Добавляет новый комментарий к указанному объявлению.
     *
     * @param id                    идентификатор объявления.
     * @param createOrUpdateComment данные для создания или обновления комментария.
     * @param authentication        объект аутентификации текущего пользователя.
     * @return {@link ResponseEntity} с объектом {@link Comment} и статусом HTTP 200 (OK).
     * @see Comment
     * @see CreateOrUpdateComment
     * @see Operation
     * @see ApiResponse
     */
    @Operation(
            tags = "Комментарии",
            summary = "Добавление комментария к объявлению",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "OK",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = Comment.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "401",
                            description = "Unauthorized"
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Not found"
                    )
            }
    )
    @PostMapping("/{id}/comments")
    public ResponseEntity<Comment> addComment(@PathVariable int id,
                                              @RequestBody CreateOrUpdateComment createOrUpdateComment,
                                              Authentication authentication) {
        log.info("addComment method from CommentsController was invoked with commentText: {} and with userName: {}",
                createOrUpdateComment.getText(), authentication.getName());
        return ResponseEntity.ok(commentsService.addComment(id, createOrUpdateComment, authentication.getName()));
    }

    /**
     * Удаляет комментарий по его идентификатору.
     * <p>
     * Доступно только администраторам или авторам комментария.
     * </p>
     *
     * @param adId      идентификатор объявления.
     * @param commentId идентификатор комментария.
     * @return {@link ResponseEntity} с пустым телом и статусом HTTP 200 (OK).
     * @see Operation
     * @see ApiResponse
     * @see PreAuthorize
     */
    @Operation(
            tags = "Комментарии",
            summary = "Удаление комментария",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "OK"
                    ),
                    @ApiResponse(
                            responseCode = "403",
                            description = "Forbidden"
                    ),
                    @ApiResponse(
                            responseCode = "401",
                            description = "Unauthorized"
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Not found"
                    )
            }
    )
    @PreAuthorize("hasRole('ADMIN') or " +
            "@commentsServiceImpl.getCommentAuthor(#commentId)==authentication.principal.username")
    @DeleteMapping("/{adId}/comments/{commentId}")
    public ResponseEntity<?> deleteComment(@PathVariable int adId, @PathVariable int commentId) {
        log.info("deleteComment method from CommentsController was invoked for adId: {} and commentId: {}",
                adId, commentId);
        commentsService.deleteComment(adId, commentId);
        return ResponseEntity.ok(HttpStatus.NO_CONTENT);
    }

    /**
     * Обновляет комментарий по его идентификатору.
     * <p>
     * Доступно только администраторам или авторам комментария.
     * </p>
     *
     * @param adId                  идентификатор объявления.
     * @param commentId             идентификатор комментария.
     * @param createOrUpdateComment новые данные для комментария.
     * @return {@link ResponseEntity} с объектом {@link Comment} и статусом HTTP 200 (OK).
     * @see Comment
     * @see CreateOrUpdateComment
     * @see Operation
     * @see ApiResponse
     * @see PreAuthorize
     */
    @Operation(
            tags = "Комментарии",
            summary = "Обновление комментария",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "OK",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = Comment.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "403",
                            description = "Forbidden"
                    ),
                    @ApiResponse(
                            responseCode = "401",
                            description = "Unauthorized"
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Not found"
                    )
            }
    )
    @PreAuthorize("hasRole('ADMIN') or " +
            "@commentsServiceImpl.getCommentAuthor(#commentId)==authentication.principal.username")
    @PatchMapping("/{adId}/comments/{commentId}")
    public ResponseEntity<Comment> updateComment(@PathVariable int adId,
                                                 @PathVariable int commentId,
                                                 @RequestBody CreateOrUpdateComment createOrUpdateComment) {
        log.info("updateComment method from CommentsController was invoked for adId: {}, commentId: {} and with newComment: {}",
                adId, commentId, createOrUpdateComment.getText());
        return ResponseEntity.ok(commentsService.updateComment(adId, commentId, createOrUpdateComment));
    }

}
