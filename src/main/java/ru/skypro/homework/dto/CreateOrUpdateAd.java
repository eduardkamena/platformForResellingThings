package ru.skypro.homework.dto;

import lombok.Data;

@Data
public class CreateOrUpdateAd {

    private String title;
    private int price;
    private String description;

}