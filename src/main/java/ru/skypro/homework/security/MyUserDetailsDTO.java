package ru.skypro.homework.security;

import lombok.Data;
import ru.skypro.homework.dto.Role;

@Data
public class MyUserDetailsDTO {

    private Integer id;
    private String email;
    private String password;
    private Role role;
}
