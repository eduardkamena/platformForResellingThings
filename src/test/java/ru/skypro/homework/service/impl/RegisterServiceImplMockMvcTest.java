package ru.skypro.homework.service.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import ru.skypro.homework.dto.Register;
import ru.skypro.homework.dto.Role;
import ru.skypro.homework.entity.UserEntity;
import ru.skypro.homework.exception.UserAlreadyExistException;
import ru.skypro.homework.mapper.UserMapper;
import ru.skypro.homework.repository.UserRepository;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class RegisterServiceImplMockMvcTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder encoder;

    @Mock
    private UserMapper userMapper;

    @InjectMocks
    private RegisterServiceImpl registerService;

    private Register register;
    private UserEntity userEntity;

    @BeforeEach
    public void setUp() {
        register = new Register();
        register.setUsername("test@mail.ru");
        register.setPassword("password");
        register.setFirstName("Test");
        register.setLastName("User");
        register.setPhone("+79111111111");

        userEntity = new UserEntity();
        userEntity.setEmail("test@mail.ru");
        userEntity.setPassword("encodedPassword");
        userEntity.setFirstName("Test");
        userEntity.setLastName("User");
        userEntity.setPhone("+79111111111");
        userEntity.setRole(Role.USER);
    }

    @Test
    public void shouldRegisterWhenSuccess() {
        // given
        when(userRepository.findByEmail(register.getUsername())).thenReturn(Optional.empty());
        when(userMapper.toUserEntityFromRegisterDTO(register)).thenReturn(userEntity);
        when(encoder.encode("encodedPassword")).thenReturn("encodedPassword"); // Учитываем изменение пароля

        // when
        boolean result = registerService.register(register, Role.USER);

        // then
        assertTrue(result);
        verify(userRepository, times(1)).findByEmail(register.getUsername());
        verify(userMapper, times(1)).toUserEntityFromRegisterDTO(register);
        verify(encoder, times(1)).encode("encodedPassword");
        verify(userRepository, times(1)).save(userEntity);
    }

    @Test
    public void shouldThrowRegisterWhenUserAlreadyExists() {
        // given
        when(userRepository.findByEmail(register.getUsername())).thenReturn(Optional.of(userEntity));

        // when & then
        assertThrows(UserAlreadyExistException.class, () -> {
            registerService.register(register, Role.USER);
        });
        verify(userRepository, times(1)).findByEmail(register.getUsername());
        verify(userMapper, never()).toUserEntityFromRegisterDTO(any());
        verify(encoder, never()).encode(any());
        verify(userRepository, never()).save(any());
    }

}
