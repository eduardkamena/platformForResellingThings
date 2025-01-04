package ru.skypro.homework.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

/**
 * Класс DTO, представляющий список объявлений.
 * <p>
 * Этот класс содержит общее количество объявлений и список всех объявлений.
 * Используется для возврата списка объявлений в API.
 * </p>
 *
 * @see Data
 * @see Schema
 */
@Data
public class Ads {

    @Schema(description = "общее количество объявлений")
    private int count;

    @Schema(description = "список всех объявлений")
    private List<Ad> results;

}
