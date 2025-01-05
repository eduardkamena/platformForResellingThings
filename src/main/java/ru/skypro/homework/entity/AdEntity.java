package ru.skypro.homework.entity;

import lombok.Data;

import javax.persistence.*;

/**
 * Класс Entity, представляющий сущность объявления в базе данных.
 * <p>
 * Этот класс отображает таблицу "ads" в базе данных и содержит информацию об объявлении,
 * такую как идентификатор, цена, заголовок, описание, изображение и автор объявления.
 * </p>
 *
 * @see Entity
 * @see Data
 * @see Id
 * @see GeneratedValue
 * @see Column
 * @see ManyToOne
 * @see JoinColumn
 */
@Data
@Entity(name = "ads")
public class AdEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "price")
    private int price;

    @Column(name = "title")
    private String title;

    @Column(name = "description")
    private String description;

    @Column(name = "image")
    private String image;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "author_id")
    private UserEntity author;

}
