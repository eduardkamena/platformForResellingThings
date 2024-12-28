package ru.skypro.homework.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import ru.skypro.homework.dto.comment.CommentDTO;
import ru.skypro.homework.dto.comment.CommentsDTO;
import ru.skypro.homework.dto.comment.CreateOrUpdateCommentDTO;
import ru.skypro.homework.repository.UserRepository;
import ru.skypro.homework.service.AdService;
import ru.skypro.homework.service.CommentService;
import ru.skypro.homework.service.LoggingMethod;

@RestController
@Slf4j
@RequiredArgsConstructor
@CrossOrigin("http://localhost:3000")
@RequestMapping("/ads")
public class CommentController {

    private final UserRepository userRepository;
    private final CommentService commentService;
    private final AdService adService;
    private final LoggingMethod loggingMethod;

    @Operation(
            tags = "Комментарии",
            summary = "Получение комментариев объявления",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "OK",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = CommentsDTO.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "401",
                            description = "Unauthorized",
                            content = @Content()
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Not found",
                            content = @Content()
                    )
            }
    )
    @GetMapping("/{id}/comments")
    public ResponseEntity<CommentsDTO> getComments(@PathVariable("id") Integer id,
                                                         Authentication authentication) {

        log.info("Запущен метод контроллера: {}", loggingMethod.getMethodName());
        if (authentication.getName() != null) {
            return ResponseEntity.ok(commentService.getCommentsByAdId(id));
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    @Operation(
            tags = "Комментарии",
            summary = "Добавление комментария к объявлению",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "OK",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = CommentDTO.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "401",
                            description = "Unauthorized",
                            content = @Content()
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Not found",
                            content = @Content()
                    )
            }
    )
    @PostMapping(value = "/{id}/comments")
    public ResponseEntity<CommentDTO> addComment(@PathVariable("id") Integer id,
                                                            @RequestBody CreateOrUpdateCommentDTO newComment,
                                                            Authentication authentication) {

        log.info("За запущен метод контроллера: {}", loggingMethod.getMethodName());
        return ResponseEntity.ok(commentService.createCommentToAdById(id, newComment, authentication.getName()));
    }

    @Operation(
            tags = "Комментарии",
            summary = "Удаление комментария",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "OK",
                            content = @Content()
                    ),
                    @ApiResponse(
                            responseCode = "401",
                            description = "Unauthorized",
                            content = @Content()
                    ),
                    @ApiResponse(
                            responseCode = "403",
                            description = "Forbidden",
                            content = @Content()
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Not found",
                            content = @Content()
                    )
            }
    )
    @DeleteMapping("/{adId}/comments/{commentId}")
    @PreAuthorize(value = "hasRole('ADMIN') or @adService.isAuthorsAd(authentication.getName(), #adId)")
    public ResponseEntity<Void> deleteComment(@PathVariable("adId") Integer adId,
                                                    @PathVariable("commentId") Integer commentId,
                                                    Authentication authentication) {

        log.info("За запущен метод контроллера: {}", loggingMethod.getMethodName());
        if (authentication.getName() != null) {
            String result = commentService.deleteCommentFromAd(commentId, authentication.getName());
            if (result.equals("forbidden")) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
            } else if (result.equals("not found")) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
            } else {
                return ResponseEntity.status(HttpStatus.OK).build();
            }
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    @Operation(
            tags = "Комментарии",
            summary = "Обновление комментария",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "OK",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = CommentDTO.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "401",
                            description = "Unauthorized",
                            content = @Content()
                    ),
                    @ApiResponse(
                            responseCode = "403",
                            description = "Forbidden",
                            content = @Content()
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Not found",
                            content = @Content()
                    )
            }
    )
    @PatchMapping("/{adId}/comments/{commentId}")
    @PreAuthorize(value = "hasRole('ADMIN') or @adService.isAuthorsAd(authentication.getName(), #adId)")
    public ResponseEntity<CommentDTO> updateComment(@PathVariable("adId") Integer adId,
                                                    @PathVariable("commentId") Integer commentId,
                                                    @RequestBody CreateOrUpdateCommentDTO newComment,
                                                    Authentication authentication) {
        log.info("За запущен метод контроллера: {}", loggingMethod.getMethodName());
        log.info("adId: {}", adId);
        log.info("commentId: {}", commentId);

        var userRole = authentication.getAuthorities();
        log.info("роль пользователя - {}", userRole);
        log.info("isAuthorAd({})", adService.isAuthorsAd(authentication.getName(), adId));
        if (authentication.getName() != null) {
            CommentDTO comment = commentService.updateComment(commentId, newComment, authentication.getName());
            return ResponseEntity.ok(comment);
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

}
