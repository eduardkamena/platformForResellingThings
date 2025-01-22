package ru.skypro.homework.security;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.context.ActiveProfiles;
import ru.skypro.homework.dto.Role;
import ru.skypro.homework.entity.UserEntity;
import ru.skypro.homework.exception.UserNotFoundException;
import ru.skypro.homework.mapper.UserMapper;
import ru.skypro.homework.repository.UserRepository;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@ActiveProfiles("test")
public class MyUserDetailsServiceMockMvcTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private MyUserDetails myUserDetails;

    @Mock
    private UserMapper userMapper;

    @InjectMocks
    private MyUserDetailsService myUserDetailsService;

    private UserEntity userEntity;
    private MyUserDetailsDTO myUserDetailsDto;

    @BeforeEach
    public void setUp() {
        userEntity = new UserEntity();
        userEntity.setEmail("test@mail.ru");
        userEntity.setPassword("password");
        userEntity.setRole(Role.USER);

        myUserDetailsDto = new MyUserDetailsDTO();
        myUserDetailsDto.setEmail("test@mail.ru");
        myUserDetailsDto.setPassword("password");
        myUserDetailsDto.setRole(Role.USER);
    }

    @Test
    public void shouldLoadUserByUsername() {
        // given
        when(userRepository.findByEmail("test@mail.ru")).thenReturn(Optional.of(userEntity));
        when(userMapper.toMyUserDetailsDTOFromUserEntity(userEntity)).thenReturn(myUserDetailsDto);

        when(myUserDetails.getUsername()).thenReturn("test@mail.ru"); // Возвращаем ожидаемый email
        when(myUserDetails.getPassword()).thenReturn("password");

        // when
        UserDetails result = myUserDetailsService.loadUserByUsername("test@mail.ru");

        // then
        assertNotNull(result);
        assertEquals("test@mail.ru", result.getUsername());
        assertEquals("password", result.getPassword());
        verify(myUserDetails).setMyUserDetailsDto(myUserDetailsDto);
    }

    @Test
    public void shouldThrowLoadUserByUsernameWhenUserNotFound() {
        // given
        when(userRepository.findByEmail("unknown@mail.ru")).thenReturn(Optional.empty());

        // when & then
        assertThrows(UserNotFoundException.class, () -> myUserDetailsService.loadUserByUsername("unknown@mail.ru"));
    }

}
