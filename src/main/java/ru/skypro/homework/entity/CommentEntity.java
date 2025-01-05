package ru.skypro.homework.entity;

import lombok.Data;

import javax.persistence.*;

/**
 * Класс Entity, представляющий сущность комментария в базе данных.
 * <p>
 * Этот класс отображает таблицу "comments" в базе данных и содержит информацию о комментарии,
 * такую как идентификатор, дата и время создания комментария в миллисекундах с 00:00:00 01.01.1970,
 * текст комментария, автор комментария и связанное объявление.
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
@Entity(name = "comments")
public class CommentEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "created_at")
    private Long createdAt;

    @Column(name = "text")
    private String text;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "author_id")
    private UserEntity author;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ad_id")
    private AdEntity ad;

}
