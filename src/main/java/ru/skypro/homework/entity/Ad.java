package ru.skypro.homework.entity;

import lombok.Data;

import javax.persistence.*;
import java.util.List;

@Entity
@Data
@Table(name = "t_announce")
public class Ad {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "t_announce_id_seq")
    @SequenceGenerator(name = "t_announce_id_seq", sequenceName = "t_announce_id_seq", allocationSize = 1)
    private int author;

    @ManyToOne
    @JoinColumn(name = "c_author_id")
    private User user;

    @Column(name = "c_description")
    private String description;

    @OneToOne
    @JoinColumn(name = "c_announce_image")
    private AdImage image;

    @Column(name = "c_price")
    private int price;

    @Column(name = "c_title")
    private String title;

    @OneToMany(mappedBy = "announce", cascade = CascadeType.ALL,
            orphanRemoval = true, fetch = FetchType.LAZY)
    private List<Comment> comments;
}
