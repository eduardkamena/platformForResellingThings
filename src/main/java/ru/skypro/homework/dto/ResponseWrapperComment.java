package ru.skypro.homework.dto;

import lombok.Data;

import java.util.List;

@Data
public class ResponseWrapperComment {

    private int count;
    private List<CommentDto> results;
}
