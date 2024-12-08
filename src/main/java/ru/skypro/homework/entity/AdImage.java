package ru.skypro.homework.entity;

import lombok.Data;

import javax.persistence.*;

@Entity
@Data
@Table(name = "t_announce_image")
public class AdImage {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.SEQUENCE,
            generator = "t_announce_image_id_seq")
    @SequenceGenerator(name = "t_announce_image_id_seq", sequenceName = "t_announce_image_id_seq",
            allocationSize = 1)
    private int id;

    @Column(name = "c_image")
    private String image;
}
