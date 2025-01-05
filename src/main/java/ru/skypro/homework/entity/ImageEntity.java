package ru.skypro.homework.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

/**
 * Класс Entity, представляющий сущность изображения в базе данных.
 * <p>
 * Этот класс отображает таблицу "images" в базе данных и содержит информацию об изображении,
 * такую как идентификатор, путь к файлу, размер файла, тип медиа и бинарные данные изображения.
 * Изображения используются как картинки объявления, так и аватары пользователей.
 * </p>
 *
 * @see Entity
 * @see Data
 * @see NoArgsConstructor
 * @see Id
 * @see GeneratedValue
 * @see Column
 * @see JsonIgnore
 * @see Lob
 */
@Data
@Entity(name = "images")
@NoArgsConstructor
public class ImageEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "file_path")
    private String filePath;

    @Column(name = "file_size")
    private long fileSize;

    @Column(name = "media_type")
    private String mediaType;

    @JsonIgnore
    @Lob
    @Column(name = "data")
    private byte[] data;

}
