package ru.skypro.homework.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import static org.springframework.security.config.Customizer.withDefaults;

/**
 * Конфигурация безопасности для приложения.
 * <p>
 * Этот класс настраивает правила безопасности, включая CSRF, CORS, управление сессиями и авторизацию запросов.
 * Также определяет белый список URL-адресов, которые доступны без аутентификации.
 * </p>
 *
 * @see Configuration
 * @see EnableGlobalMethodSecurity
 * @see SecurityFilterChain
 * @see PasswordEncoder
 */
@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfig {

    /**
     * Белый список URL-адресов, доступных без аутентификации.
     */
    private static final String[] AUTH_WHITELIST = {
            "/swagger-resources/**",
            "/swagger-ui.html",
            "/v3/api-docs",
            "/webjars/**",
            "/login",
            "/register"
    };

    /**
     * Настраивает цепочку фильтров безопасности.
     * <p>
     * Отключает CSRF, настраивает авторизацию запросов, разрешает доступ к определенным URL-адресам без аутентификации,
     * а также настраивает CORS и управление сессиями.
     * </p>
     *
     * @param http объект {@link HttpSecurity} для настройки безопасности
     * @return {@link SecurityFilterChain} с примененными настройками безопасности
     * @throws Exception если возникает ошибка при настройке
     */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf()
                .disable()
                .authorizeHttpRequests(
                        (authorization) ->
                                authorization
                                        .mvcMatchers(AUTH_WHITELIST)
                                        .permitAll()
                                        .mvcMatchers(HttpMethod.GET, "/ads", "/ads/image/*", "/users/image/*")
                                        .permitAll()
                                        .mvcMatchers("/ads/**", "/users/**")
                                        .authenticated()
                )
                .cors()
                .and()
                .sessionManagement(configurer -> configurer.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .httpBasic(withDefaults());
        return http.build();
    }

    /**
     * Создает и возвращает кодировщик паролей.
     * <p>
     * Используется алгоритм BCrypt для хеширования паролей.
     * </p>
     *
     * @return {@link PasswordEncoder} для хеширования паролей
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

}
