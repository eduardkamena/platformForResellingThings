package ru.skypro.homework.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

@Data
public class Ads {

    @Schema(description = "общее количество объявлений")
    private int count;

    @Schema(description = "список всех объявлений")
    private List<Ad> results;

}
