package ru.skypro.homework.service;

import ru.skypro.homework.dto.Login;
import ru.skypro.homework.dto.Register;

public interface AuthService {
    boolean login(String userName, String password);

    boolean login(Login loginDto);

    boolean register(Register register);
}
