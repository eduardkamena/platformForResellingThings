package ru.skypro.homework.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import ru.skypro.homework.dto.Role;

import javax.persistence.*;

@Entity
@Data
@Table(name = "t_user")
public class User {

    @Id
    @Column(name = "id")
    @Schema(description = "id пользователя")
    private int id;

    @Column(name = "c_username")
    @Schema(description = "логин пользователя")
    private String email;

    @Column(name = "c_firstname")
    @Schema(description = "имя пользователя")
    private String firstName;

    @Column(name = "c_lastname")
    @Schema(description = "фамилия пользователя")
    private String lastName;

    @Column(name = "c_phone")
    @Schema(description = "телефон пользователя")
    private String phone;

    @Column(name = "c_role")
    @Schema(description = "роль пользователя")
    @Enumerated(EnumType.STRING)
    private Role role;

    @Column(name = "c_image")
    @Schema(description = "ссылка на аватар пользователя")
    private String image;

}
