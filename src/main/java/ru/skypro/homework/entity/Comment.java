package ru.skypro.homework.entity;

import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Data
@Table(name = "t_comment")
public class Comment {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "t_comment_id_seq")
    @SequenceGenerator(name = "t_comment_id_seq", sequenceName = "t_comment_id_seq", allocationSize = 1)
    private int pk;

    @Column(name = "c_text")
    private String text;

    @Column(name = "c_date")
    private LocalDateTime createdAt;

    @ManyToOne
    @JoinColumn(name = "c_author_id")
    private User author;

    @ManyToOne
    @JoinColumn(name = "c_announce_id")
    private Ad announce;
}
