package ru.skypro.homework.dto;

import lombok.Data;

@Data
public class User {

    private int id;
    private String email;
    private String firstName;
    private String lastName;
    private String phone;
    private Role role;
    private String image;

}
