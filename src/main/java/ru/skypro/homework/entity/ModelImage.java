package ru.skypro.homework.entity;

import lombok.Data;

@Data
public class ModelImage {

    private Image image;
    private String filePath; //путь на ПК
    private String imagePath; //URL для контроллера

}
