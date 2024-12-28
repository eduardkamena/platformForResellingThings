package ru.skypro.homework.dto.user;

import lombok.Data;
import ru.skypro.homework.dto.Role;

@Data
public class MyUserDetailsDto {

    private Integer id;
    private String email;
    private String password;
    private Role role;
}
