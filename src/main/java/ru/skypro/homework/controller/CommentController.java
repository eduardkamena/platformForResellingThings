package ru.skypro.homework.controller;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.skypro.homework.dto.comment.CommentDTO;
import ru.skypro.homework.dto.comment.CommentsDTO;
import ru.skypro.homework.dto.comment.CreateOrUpdateCommentDTO;
import ru.skypro.homework.service.AdService;
import ru.skypro.homework.service.CommentService;

@RestController
@RequestMapping("/ads")
@RequiredArgsConstructor
public class CommentController {
    private final CommentService commentService;
    private final AdService adService;

    @GetMapping(path = "/{id}/comments")
    @Operation(description = "Получение комментариев объявлеиня")
    public ResponseEntity<CommentsDTO> getCommentsByAdId(@PathVariable("id") Integer adId) {
        return null;
    }

    @PostMapping(path = "/{id}/comments")
    @Operation(description = "Добавление комментария к объявлению")
    public ResponseEntity<CommentDTO> createCommentToAdById(@PathVariable("id") Integer adId,
                                                            @RequestBody CreateOrUpdateCommentDTO newComment) {
        return null;
    }

    @DeleteMapping(path = "/ads/{adId}/comments/{id}")
    @Operation(description = "Удаление комментария")
    public ResponseEntity<Void> deleteCommentFromAd(@PathVariable("adId") Integer adId,
                                                    @PathVariable("id") Integer commentId) {
        return null;
    }
}
