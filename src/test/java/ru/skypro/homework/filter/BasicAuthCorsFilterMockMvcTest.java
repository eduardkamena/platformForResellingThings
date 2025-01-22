package ru.skypro.homework.filter;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockFilterChain;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.ActiveProfiles;

import javax.servlet.ServletException;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
@ActiveProfiles("test")
public class BasicAuthCorsFilterMockMvcTest {

    @InjectMocks
    private BasicAuthCorsFilter basicAuthCorsFilter;

    @Mock
    private MockFilterChain mockFilterChain;

    @Test
    public void shouldDoFilterInternal() throws ServletException, IOException {
        // given
        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();

        // when
        basicAuthCorsFilter.doFilterInternal(request, response, mockFilterChain);

        // then
        assertEquals("true", response.getHeader("Access-Control-Allow-Credentials"));
        verify(mockFilterChain).doFilter(request, response); // Проверяем, что фильтр передал запрос дальше по цепочке
    }

}
