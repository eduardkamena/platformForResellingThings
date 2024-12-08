package ru.skypro.homework.entity;

import lombok.Data;
import ru.skypro.homework.dto.Role;

import javax.persistence.*;

@Entity
@Data
@Table(name = "t_user")
public class User {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "t_user_id_seq")
    @SequenceGenerator(name = "t_user_id_seq", sequenceName = "t_user_id_seq", allocationSize = 1)
    private int id;

    @Column(name = "c_username")
    private String email;

    @Column(name = "c_firstname")
    private String firstName;

    @Column(name = "c_lastname")
    private String lastName;

    @Column(name = "c_phone")
    private String phone;

    @Column(name = "c_role")
    @Enumerated(EnumType.STRING)
    private Role role;

    @OneToOne
    @JoinColumn(name = "c_image")
    private UserImage image;

}
