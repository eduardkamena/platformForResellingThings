package ru.skypro.homework.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

/**
 * Класс DTO, представляющий список комментариев к объявлению.
 * <p>
 * Этот класс содержит общее количество комментариев и список всех комментариев, связанных с объявлением.
 * Используется для возврата списка комментариев в API.
 * </p>
 *
 * @see Data
 * @see Schema
 */
@Data
public class Comments {

    @Schema(description = "общее количество комментариев")
    private int count;

    @Schema(description = "список всех комментариев объявления")
    private List<Comment> results;

}
