package ru.skypro.homework.filter;


import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Фильтр для обработки CORS-запросов с поддержкой базовой аутентификации.
 * <p>
 * Этот фильтр добавляет заголовок "Access-Control-Allow-Credentials" со значением "true"
 * к каждому HTTP-ответу, что позволяет браузерам отправлять учетные данные (например, куки)
 * в кросс-доменных запросах.
 * </p>
 *
 * @see Component
 * @see OncePerRequestFilter
 */
@Component
public class BasicAuthCorsFilter extends OncePerRequestFilter {

    /**
     * Метод, выполняющий фильтрацию запроса.
     * <p>
     * Добавляет заголовок "Access-Control-Allow-Credentials" к ответу и передает запрос
     * дальше по цепочке фильтров.
     * </p>
     *
     * @param httpServletRequest  HTTP-запрос.
     * @param httpServletResponse HTTP-ответ.
     * @param filterChain         цепочка фильтров.
     * @throws ServletException если возникает ошибка при обработке запроса.
     * @throws IOException      если возникает ошибка ввода-вывода.
     */
    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest,
                                    HttpServletResponse httpServletResponse,
                                    FilterChain filterChain)
            throws ServletException, IOException {
        httpServletResponse.addHeader("Access-Control-Allow-Credentials", "true");
        filterChain.doFilter(httpServletRequest, httpServletResponse);
    }

}
