package ru.skypro.homework.entity;

import lombok.Data;
import ru.skypro.homework.dto.Role;

import javax.persistence.*;

/**
 * Класс Entity, представляющий сущность пользователя в базе данных.
 * <p>
 * Этот класс отображает таблицу "users" в базе данных и содержит информацию о пользователе,
 * такую как идентификатор, email (логин), пароль, имя, фамилия, телефон, ссылка на аватар и роль.
 * </p>
 *
 * @see Entity
 * @see Data
 * @see Id
 * @see GeneratedValue
 * @see Column
 * @see Enumerated
 * @see Role
 */
@Data
@Entity(name = "users")
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "email")
    private String email;

    @Column(name = "password")
    private String password;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "phone")
    private String phone;

    @Column(name = "image")
    private String image;

    @Column(name = "role")
    @Enumerated(EnumType.STRING)
    private Role role;

}
