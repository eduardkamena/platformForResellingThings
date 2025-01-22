package ru.skypro.homework.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import ru.skypro.homework.config.TestSecurityConfig;
import ru.skypro.homework.dto.Login;
import ru.skypro.homework.service.LoginService;

import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(LoginController.class)
@Import(TestSecurityConfig.class)
@ActiveProfiles("test")
public class LoginControllerWebMvcTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private LoginService loginService;

    @Test
    @WithMockUser(username = "test@mail.ru", roles = "USER")
    public void shouldReturnOkWhenLoginIsSuccessful() throws Exception {
        // given
        Login login = new Login();
        login.setUsername("test@mail.ru");
        login.setPassword("password");

        // when
        when(loginService.login(login.getUsername(), login.getPassword())).thenReturn(true);

        // then
        mockMvc.perform(post("/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(login))
                        .with(csrf()))
                .andExpect(status().isOk());
    }

    @Test
    public void shouldReturnUnauthorizedWhenLoginFails() throws Exception {
        // given
        Login login = new Login();
        login.setUsername("test@mail.ru");
        login.setPassword("wrong_password");

        // when
        when(loginService.login(login.getUsername(), login.getPassword())).thenReturn(false);

        // then
        mockMvc.perform(post("/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(login)))
                .andExpect(status().isUnauthorized());
    }

}
