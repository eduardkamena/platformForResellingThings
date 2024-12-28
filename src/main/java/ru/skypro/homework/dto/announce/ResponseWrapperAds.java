package ru.skypro.homework.dto.announce;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

@Data
public class ResponseWrapperAds {
    @Schema(description = "общее количество объявлений")
    private int count;

    @Schema (description = "список всех объявлений")
    private List<AdsDto> result;

}
