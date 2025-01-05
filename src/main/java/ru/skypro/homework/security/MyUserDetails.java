package ru.skypro.homework.security;

import lombok.Setter;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.RequestScope;

import java.util.Collection;
import java.util.Collections;
import java.util.Optional;

/**
 * Класс, реализующий интерфейс {@link UserDetails} для работы с данными пользователя в Spring Security.
 * <p>
 * Этот класс предоставляет информацию о пользователе, такую как его учетные данные (логин и пароль),
 * роли и статус аккаунта. Используется для аутентификации и авторизации пользователей.
 * </p>
 *
 * @see Component
 * @see RequestScope
 * @see UserDetails
 * @see MyUserDetailsDTO
 */
@Component
@Setter
@RequestScope(proxyMode = ScopedProxyMode.TARGET_CLASS)
public class MyUserDetails implements UserDetails {

    private MyUserDetailsDTO myUserDetailsDto;

    /**
     * Возвращает список ролей пользователя.
     *
     * @return коллекция объектов {@link GrantedAuthority}, представляющих роли пользователя.
     */
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Optional.ofNullable(myUserDetailsDto)
                .map(MyUserDetailsDTO::getRole)
                .map(role -> "ROLE_" + role)
                .map(SimpleGrantedAuthority::new)
                .map(Collections::singleton)
                .orElse(Collections.emptySet());
    }

    /**
     * Возвращает пароль пользователя.
     *
     * @return пароль пользователя.
     */
    @Override
    public String getPassword() {
        return Optional.ofNullable(myUserDetailsDto)
                .map(MyUserDetailsDTO::getPassword)
                .orElse(null);
    }

    /**
     * Возвращает имя пользователя (email).
     *
     * @return email пользователя.
     */
    @Override
    public String getUsername() {
        return Optional.ofNullable(myUserDetailsDto)
                .map(MyUserDetailsDTO::getEmail)
                .orElse(null);
    }

    /**
     * Проверяет, не истек ли срок действия аккаунта.
     *
     * @return всегда {@code true}, так как срок действия аккаунта не ограничен.
     */
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    /**
     * Проверяет, не заблокирован ли аккаунт.
     *
     * @return всегда {@code true}, так как аккаунт не блокируется.
     */
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    /**
     * Проверяет, не истек ли срок действия учетных данных.
     *
     * @return всегда {@code true}, так как срок действия учетных данных не ограничен.
     */
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    /**
     * Проверяет, активен ли аккаунт.
     *
     * @return всегда {@code true}, так как аккаунт всегда активен.
     */
    @Override
    public boolean isEnabled() {
        return true;
    }

}
