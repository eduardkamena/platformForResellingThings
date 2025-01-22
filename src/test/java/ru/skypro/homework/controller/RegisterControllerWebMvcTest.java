package ru.skypro.homework.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import ru.skypro.homework.config.TestSecurityConfig;
import ru.skypro.homework.dto.Register;
import ru.skypro.homework.dto.Role;
import ru.skypro.homework.service.RegisterService;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(RegisterController.class)
@Import(TestSecurityConfig.class)
@ActiveProfiles("test")
public class RegisterControllerWebMvcTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private RegisterService registerService;

    @Test
    public void shouldReturnCreatedWhenRegistrationIsSuccessful() throws Exception {
        // given
        Register register = new Register();
        register.setUsername("test@mail.ru");
        register.setPassword("password");
        register.setFirstName("Test");
        register.setLastName("User");
        register.setPhone("+79111111111");
        register.setRole(Role.USER);

        // when
        when(registerService.register(any(Register.class), eq(Role.USER))).thenReturn(true);

        // then
        mockMvc.perform(post("/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(register)))
                .andExpect(status().isCreated());
    }

    @Test
    public void shouldReturnBadRequestWhenRegistrationFails() throws Exception {
        // given
        Register register = new Register();
        register.setUsername("test@mail.ru");
        register.setPassword("password");
        register.setFirstName("Test");
        register.setLastName("User");
        register.setPhone("+79111111111");
        register.setRole(Role.USER);

        // when
        when(registerService.register(any(Register.class), eq(Role.USER))).thenReturn(false);

        // then
        mockMvc.perform(post("/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(register)))
                .andExpect(status().isBadRequest());
    }

}
