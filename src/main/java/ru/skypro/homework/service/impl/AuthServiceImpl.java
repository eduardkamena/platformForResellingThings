package ru.skypro.homework.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.skypro.homework.dto.Register;
import ru.skypro.homework.entity.User;
import ru.skypro.homework.exception.UserAlreadyExistException;
import ru.skypro.homework.exception.UserNotFoundException;
import ru.skypro.homework.exception.WrongPasswordException;
import ru.skypro.homework.mapper.UserMapper;
import ru.skypro.homework.repository.UserRepository;
import ru.skypro.homework.service.AuthService;
import ru.skypro.homework.service.LoggingMethod;


@Service
@Slf4j
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserDetailsServiceImpl userDetailsService;
    private final LoggingMethod loggingMethod;
    private final UserRepository userRepository;
    private final PasswordEncoder encoder = new BCryptPasswordEncoder();

    /**
     * Метод авторизации проверяет, существует ли в базе данных пользователь с
     * указанными параметрами метода - логином и паролем.
     * Метод {@link UserDetailsServiceImpl#loadUserByUsername(String userName)} проверяет наличие пользователя
     * с указанным логином в БД. И если такого пользователя нет, то выбрасывает исключение
     * {@link UsernameNotFoundException}. Если пользователь найден, то он возвращается из метода.
     * <p>Далее метод <b>encoder.matches()</b> сравнивает пароли.
     * Если проверка пройдена, то метод возвращает true, если же нет, то метод выбрасывает исключение
     * {@link WrongPasswordException}.</p>
     *
     * @param userName
     * @param password
     * @return true - если пользователь существует в БД, и пароли совпадают;
     * {@link UsernameNotFoundException} - если пользователь не найден в БД;
     * {@link WrongPasswordException} - если пароли не совпадают.
     */
    @Override
    public boolean login(String userName, String password) {
        log.info("Запущен метод сервиса {}", loggingMethod.getMethodName());

        if (!userDetailsService.userExists(userName)) {
            // может вместо false выбросить исключение UsernameNotFoundException ?
            return false;
        }
        UserDetails userDetails = userDetailsService.loadUserByUsername(userName);
        if (!encoder.matches(password, userDetails.getPassword())) {
            throw new WrongPasswordException("Неверный пароль");
        }
        return true;
    }

//    @Override
//    public boolean register(Register register) {
//        if (manager.userExists(register.getUsername())) {
//            return false;
//        }
//        manager.createUser(
//                User.builder()
//                        .passwordEncoder(this.encoder::encode)
//                        .password(register.getPassword())
//                        .username(register.getUsername())
//                        .roles(register.getRole().name())
//                        .build());
//        return true;
//    }

//    @Override
//    public boolean register(Register register) {
//        if (userService.userExists(register.getUsername())) {
//            return false;
//        }
//        userService.createUser(register, encoder.encode(register.getPassword()));
//        return true;
//    }

    /**
     * Метод регистрирует нового пользователя.
     * <p>Метод получает ДТО {@link Register} из контроллера. Далее ДТО конвертируется методом
     * {@link UserMapper#mapFromRegisterToUserEntity(Register)} в сущность {@link User}.</p>
     * <p>Если пользователь с таким логином ({@link User#getUsername()}) в репозитории найден,
     * то выбрасывается исключение {@link UserAlreadyExistException}.</p>
     * <p>Если проверка пройдена успешно и такого логина нет в базе данных, то в таком случае
     * пароль кодируется, сохраняется в сущность {@link User}. А сама сущность сохраняется в
     * базе данных.</p>
     *
     * @param register
     * @return true в случае, если пользователь уникален и сохранен в БД;
     * в случае, если проверка на уникальность логина не пройдена, выбрасывается исключение
     * {@link UserAlreadyExistException}.
     */
    @Override
    public boolean register(Register register) {
        log.info("Запущен метод сервиса {}", loggingMethod.getMethodName());
        User user = UserMapper.mapFromRegisterToUserEntity(register);
        if (userRepository.findUserByUsername(user.getUsername()) != null) {
            throw new UserAlreadyExistException("Такой пользователь существует");
        }
        user.setPassword(encoder.encode(user.getPassword()));
        userRepository.save(user);
        return true;
    }

}
