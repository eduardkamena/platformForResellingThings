package ru.skypro.homework.service.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.dto.NewPassword;
import ru.skypro.homework.dto.User;
import ru.skypro.homework.entity.UserEntity;
import ru.skypro.homework.exception.UserNotFoundException;
import ru.skypro.homework.mapper.UserMapper;
import ru.skypro.homework.repository.UserRepository;
import ru.skypro.homework.service.ImageService;

import java.io.IOException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceImplMockMvcTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder encoder;

    @Mock
    private ImageService imageService;

    @Mock
    private UserMapper userMapper;

    @InjectMocks
    private UserServiceImpl userService;

    private UserEntity userEntity;
    private User userDTO;
    private NewPassword newPassword;
    private MultipartFile imageFile;

    @BeforeEach
    public void setUp() {
        userEntity = new UserEntity();
        userEntity.setEmail("test@mail.ru");
        userEntity.setPassword("encodedPassword");
        userEntity.setFirstName("Test");
        userEntity.setLastName("User");
        userEntity.setPhone("+79111111111");

        userDTO = new User();
        userDTO.setFirstName("Vasya");
        userDTO.setLastName("Vasev");
        userDTO.setPhone("+79222222222");

        newPassword = new NewPassword();
        newPassword.setCurrentPassword("oldPassword");
        newPassword.setNewPassword("newPassword");

        imageFile = new MockMultipartFile(
                "avatar",
                "test.png",
                "image/png",
                "test image content".getBytes()
        );
    }

    @Test
    public void shouldSetPassword() {
        // given
        when(userRepository.findByEmail("test@mail.ru")).thenReturn(Optional.of(userEntity));
        when(encoder.matches("oldPassword", "encodedPassword")).thenReturn(true);
        when(encoder.encode("newPassword")).thenReturn("newEncodedPassword");

        // when
        userService.setPassword(newPassword, "test@mail.ru");

        // then
        verify(userRepository, times(1)).save(userEntity);
        assertEquals("newEncodedPassword", userEntity.getPassword());
    }

    @Test
    public void shouldThrowSetPasswordWhenUserNotFound() {
        // given
        when(userRepository.findByEmail("unknown@mail.ru")).thenReturn(Optional.empty());

        // when
        userService.setPassword(newPassword, "unknown@mail.ru");

        // then
        verify(userRepository, never()).save(any());
    }

    @Test
    public void shouldThrowSetPasswordWhenCurrentPasswordIsIncorrect() {
        // given
        when(userRepository.findByEmail("test@mail.ru")).thenReturn(Optional.of(userEntity));
        lenient().when(encoder
                        .matches(
                                "wrongPassword",
                                "encodedPassword"))
                .thenReturn(false);

        // when
        userService.setPassword(newPassword, "test@mail.ru");

        // then
        verify(userRepository, never()).save(any());
    }

    @Test
    public void shouldGetUser() {
        // given
        when(userRepository.findByEmail("test@mail.ru")).thenReturn(Optional.of(userEntity));
        when(userMapper.toUserDTOFromUserEntity(userEntity)).thenReturn(userDTO);

        // when
        User result = userService.getUser("test@mail.ru");

        // then
        assertNotNull(result);
        assertEquals("Vasya", result.getFirstName());
        assertEquals("Vasev", result.getLastName());
        assertEquals("+79222222222", result.getPhone());
    }

    @Test
    public void shouldThrowGetUserWhenUserNotFound() {
        // given & when
        when(userRepository.findByEmail("unknown@mail.ru")).thenReturn(Optional.empty());

        // then
        assertThrows(UserNotFoundException.class, () -> userService.getUser("unknown@mail.ru"));
    }

    @Test
    public void shouldUpdateUser() {
        // given & when
        when(userRepository.findByEmail("test@mail.ru")).thenReturn(Optional.of(userEntity));
        when(userMapper.toUserDTOFromUserEntity(userEntity)).thenReturn(userDTO);

        User result = userService.updateUser(userDTO, "test@mail.ru");

        // then
        assertNotNull(result);
        assertEquals("Vasya", userEntity.getFirstName());
        assertEquals("Vasev", userEntity.getLastName());
        assertEquals("+79222222222", userEntity.getPhone());
        verify(userRepository, times(1)).save(userEntity);
    }

    @Test
    public void shouldThrowUpdateUserWhenUserNotFound() {
        // given & when
        when(userRepository.findByEmail("unknown@mail.ru")).thenReturn(Optional.empty());

        // then
        assertThrows(UserNotFoundException.class, () -> userService.updateUser(userDTO, "unknown@mail.ru"));
    }

    @Test
    public void shouldUpdateAvatar() throws IOException {
        // given & when
        userEntity.setImage("oldAvatarPath"); // Устанавливаем начальное значение для image
        when(userRepository.findByEmail("test@mail.ru")).thenReturn(Optional.of(userEntity));
        when(imageService.saveImage(imageFile, "/users")).thenReturn("newAvatarPath");

        userService.updateAvatar(imageFile, "test@mail.ru");

        // then
        verify(imageService, times(1)).deleteImage("oldAvatarPath");
        verify(imageService, times(1)).saveImage(imageFile, "/users");
        verify(userRepository, times(1)).save(userEntity);
        assertEquals("newAvatarPath", userEntity.getImage()); // Проверяем, что путь к аватару обновлен
    }

    @Test
    public void shouldThrowUpdateAvatarWhenUserNotFound() {
        // given & when
        when(userRepository.findByEmail("unknown@mail.ru")).thenReturn(Optional.empty());

        // then
        assertThrows(UserNotFoundException.class, () -> userService.updateAvatar(imageFile, "unknown@mail.ru"));
    }

    @Test
    public void shouldGetImage() throws IOException {
        // given & when
        when(imageService.getImage("test.png")).thenReturn("test image content".getBytes());

        byte[] result = userService.getImage("test.png");

        // then
        assertNotNull(result);
        assertArrayEquals("test image content".getBytes(), result);
    }

}
