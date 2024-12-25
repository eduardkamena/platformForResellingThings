package ru.skypro.homework.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "images")
public class Image {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "image_seq")
    @SequenceGenerator(name = "image_seq", sequenceName = "IMAGE_ID_SEQ", allocationSize = 1)
    private int id;

    @Column(name = "filePath")
    private String filePath;

    @Column(name = "fileSize")
    private long fileSize;

    @Column(name = "mediaType")
    private String mediaType;

    @Lob
    @Column(name = "byteData")
    private byte[] data;

}
