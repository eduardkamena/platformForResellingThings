package ru.skypro.homework.dto;

import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
public class Login {

    @NotNull
    @Size(min = 4, max = 32)
    private String username;

    @NotNull
    @Size(min = 8, max = 16)
    private String password;
}
