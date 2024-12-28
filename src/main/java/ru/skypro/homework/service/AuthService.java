package ru.skypro.homework.service;

import ru.skypro.homework.dto.Register;
import ru.skypro.homework.dto.Role;

public interface AuthService {
    boolean login(String userName, String password);

    boolean register(Register register, Role role);
}
