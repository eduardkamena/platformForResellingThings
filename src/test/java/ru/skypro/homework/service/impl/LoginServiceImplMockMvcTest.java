package ru.skypro.homework.service.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import ru.skypro.homework.entity.UserEntity;
import ru.skypro.homework.exception.UserNotFoundException;
import ru.skypro.homework.repository.UserRepository;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class LoginServiceImplMockMvcTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder encoder;

    @InjectMocks
    private LoginServiceImpl loginService;

    private UserEntity userEntity;

    @BeforeEach
    public void setUp() {
        userEntity = new UserEntity();
        userEntity.setEmail("test@mail.ru");
        userEntity.setPassword("encodedPassword");
    }

    @Test
    public void shouldLoginSuccess() {
        // given
        String email = "test@mail.ru";
        String password = "password";
        when(userRepository.findByEmail(email)).thenReturn(Optional.of(userEntity));
        when(encoder.matches(password, userEntity.getPassword())).thenReturn(true);

        // when
        boolean result = loginService.login(email, password);

        // then
        assertTrue(result);
        verify(userRepository, times(1)).findByEmail(email);
        verify(encoder, times(1)).matches(password, userEntity.getPassword());
    }

    @Test
    public void shouldThrowLoginWhenUserNotFound() {
        // given
        String email = "unknown@mail.ru";
        when(userRepository.findByEmail(email)).thenReturn(Optional.empty());

        // when & then
        assertThrows(UserNotFoundException.class, () -> loginService.login(email, "password"));
        verify(userRepository, times(1)).findByEmail(email);
        verify(encoder, never()).matches(any(), any());
    }

    @Test
    public void shouldLoginIncorrectPassword() {
        // given
        String email = "test@mail.ru";
        String password = "wrongPassword";
        when(userRepository.findByEmail(email)).thenReturn(Optional.of(userEntity));
        when(encoder.matches(password, userEntity.getPassword())).thenReturn(false);

        // when
        boolean result = loginService.login(email, password);

        // then
        assertFalse(result);
        verify(userRepository, times(1)).findByEmail(email);
        verify(encoder, times(1)).matches(password, userEntity.getPassword());
    }

}
