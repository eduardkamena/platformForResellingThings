package ru.skypro.homework.entity;

import lombok.Data;

@Data
public class ModelImage {

    private Image photo;
    private String filePath; //путь на ПК
    private String image; //URL для контроллера

}
