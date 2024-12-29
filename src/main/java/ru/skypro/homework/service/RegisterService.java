package ru.skypro.homework.service;

import ru.skypro.homework.dto.Register;
import ru.skypro.homework.dto.Role;

public interface RegisterService {

    boolean register(Register register, Role role);

}
