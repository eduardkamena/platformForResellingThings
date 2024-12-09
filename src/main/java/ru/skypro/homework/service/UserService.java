package ru.skypro.homework.service;

import ru.skypro.homework.entity.User;

import java.util.Optional;

public interface UserService {

    Optional<User> findUserByUsername(String username);

}
