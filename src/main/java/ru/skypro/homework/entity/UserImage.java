package ru.skypro.homework.entity;

import lombok.Data;

import javax.persistence.*;

@Entity
@Data
@Table(name = "t_user_image")
public class UserImage {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.SEQUENCE,
            generator = "t_user_image_id_seq")
    @SequenceGenerator(name = "t_user_image_id_seq", sequenceName = "t_user_image_id_seq",
            allocationSize = 1)
    private int id;

    @Column(name = "c_image")
    private String image;
}
