package ru.skypro.homework.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.skypro.homework.config.CustomUserDetails;
import ru.skypro.homework.dto.Register;
import ru.skypro.homework.entity.User;
import ru.skypro.homework.exception.UserNotFoundException;
import ru.skypro.homework.mapper.UserMapperInterface;
import ru.skypro.homework.repository.UserRepository;
import ru.skypro.homework.service.LoggingMethod;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;
    private final UserMapperInterface userMapperInterface;
    private final LoggingMethod loggingMethod;


    @Transactional
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        log.info("Запущен метод сервиса {}", loggingMethod.getMethodName());


        return new CustomUserDetails(userRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException("User not found with username: " + username)));
    }

    public boolean userExists(String username) {
        User notExist = new User();
        User count = userRepository.findByUsername(username).orElse(notExist);
        return !notExist.equals(count);
    }

    public void createUser(Register register, String password) {
        User user = userMapperInterface.fromRegisterToUser(register);
        user.setPassword(password);
        userRepository.save(user);
    }

}
