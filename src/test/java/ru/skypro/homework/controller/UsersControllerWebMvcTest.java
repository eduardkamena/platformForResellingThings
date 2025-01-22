package ru.skypro.homework.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.config.TestSecurityConfig;
import ru.skypro.homework.dto.NewPassword;
import ru.skypro.homework.dto.Role;
import ru.skypro.homework.dto.User;
import ru.skypro.homework.service.UserService;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UsersController.class)
@Import(TestSecurityConfig.class)
@ActiveProfiles("test")
public class UsersControllerWebMvcTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private UserService userService;

    @Test
    @WithMockUser(username = "test@mail.ru", roles = "USER")
    public void shouldUpdatePassword() throws Exception {
        // given
        NewPassword newPassword = new NewPassword();
        newPassword.setCurrentPassword("oldPassword");
        newPassword.setNewPassword("newPassword");

        // when
        doNothing().when(userService).setPassword(any(NewPassword.class), eq("test@mail.ru"));

        // then
        mockMvc.perform(post("/users/set_password")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newPassword)))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "test@mail.ru", roles = "USER")
    public void shouldGetUser() throws Exception {
        // given
        User user = new User();
        user.setEmail("test@mail.ru");
        user.setFirstName("Test");
        user.setLastName("User");
        user.setPhone("+79111111111");
        user.setRole(Role.USER);

        // when
        when(userService.getUser("test@mail.ru")).thenReturn(user);

        // then
        mockMvc.perform(get("/users/me"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value("test@mail.ru"))
                .andExpect(jsonPath("$.firstName").value("Test"))
                .andExpect(jsonPath("$.lastName").value("User"))
                .andExpect(jsonPath("$.phone").value("+79111111111"))
                .andExpect(jsonPath("$.role").value("USER"));
    }

    @Test
    @WithMockUser(username = "test@mail.ru", roles = "USER")
    public void shouldUpdateUser() throws Exception {
        // given
        User user = new User();
        user.setFirstName("Vasya");
        user.setLastName("Vasilek");
        user.setPhone("+79222222222");

        // when
        when(userService.updateUser(any(User.class), eq("test@mail.ru"))).thenReturn(user);

        // then
        mockMvc.perform(patch("/users/me")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(user)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName").value("Vasya"))
                .andExpect(jsonPath("$.lastName").value("Vasilek"))
                .andExpect(jsonPath("$.phone").value("+79222222222"));
    }

    @Test
    @WithMockUser(username = "test@mail.ru", roles = "USER")
    public void shouldUpdateUserImage() throws Exception {
        // given
        MockMultipartFile image = new MockMultipartFile("image", "test.png", MediaType.IMAGE_PNG_VALUE, "test image".getBytes());

        // when
        doNothing().when(userService).updateAvatar(any(MultipartFile.class), eq("test@mail.ru"));

        // then
        mockMvc.perform(multipart("/users/me/image")
                        .file(image)
                        .with(request -> {
                            request.setMethod("PATCH");
                            return request;
                        })
                        .contentType(MediaType.MULTIPART_FORM_DATA))
                .andExpect(status().isOk());
    }

    @Test
    public void shouldGetUserImage() throws Exception {
        // given
        byte[] imageBytes = "test image content".getBytes();

        // when
        when(userService.getImage(any(String.class))).thenReturn(imageBytes);

        // then
        mockMvc.perform(MockMvcRequestBuilders.get("/users/image/test.png")
                        .contentType(MediaType.IMAGE_PNG_VALUE)) // Указываем Content-Type
                .andExpect(status().isOk())
                .andExpect(content().bytes(imageBytes)); // Проверяем, что тело ответа содержит ожидаемые байты
    }

}
