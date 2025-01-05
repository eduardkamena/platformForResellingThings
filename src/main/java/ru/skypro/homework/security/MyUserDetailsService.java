package ru.skypro.homework.security;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import ru.skypro.homework.exception.UserNotFoundException;
import ru.skypro.homework.mapper.UserMapper;
import ru.skypro.homework.repository.UserRepository;

/**
 * Сервис для загрузки данных пользователя в Spring Security.
 * <p>
 * Этот класс реализует интерфейс {@link UserDetailsService} и используется для поиска пользователя
 * по email и преобразования его данных в объект {@link MyUserDetails}, необходимый для аутентификации.
 * </p>
 *
 * @see Service
 * @see RequiredArgsConstructor
 * @see UserDetailsService
 * @see MyUserDetails
 */
@Service
@RequiredArgsConstructor
public class MyUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;
    private final MyUserDetails myUserDetails;
    private final UserMapper userMapper;

    /**
     * Загружает данные пользователя по email.
     *
     * @param email email пользователя.
     * @return объект {@link MyUserDetails}, содержащий данные пользователя.
     * @throws UsernameNotFoundException если пользователь с указанным email не найден.
     */
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        MyUserDetailsDTO myUserDetailsDto = userRepository.findByEmail(email)
                .map(userMapper::toMyUserDetailsDTOFromUserEntity)
                .orElseThrow(() -> new UserNotFoundException(email));
        myUserDetails.setMyUserDetailsDto(myUserDetailsDto);
        return myUserDetails;
    }

}
