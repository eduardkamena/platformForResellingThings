package ru.skypro.homework.entity;

import liquibase.pro.packaged.I;
import lombok.Data;

import javax.persistence.*;

@Data
@Entity(name="ads")
public class AdEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private int price;
    private String title;
    private String description;
    private String image;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "author_id")
    private UserEntity author;
}
