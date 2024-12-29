package ru.skypro.homework.dto;

import lombok.Data;

@Data
public class MyUserDetailsDto {

    private Integer id;
    private String email;
    private String password;
    private Role role;
}
