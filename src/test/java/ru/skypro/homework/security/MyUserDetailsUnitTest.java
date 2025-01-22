package ru.skypro.homework.security;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import ru.skypro.homework.dto.Role;

import java.util.Collection;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class MyUserDetailsUnitTest {

    private MyUserDetails myUserDetails;
    private MyUserDetailsDTO myUserDetailsDto;

    @BeforeEach
    public void setUp() {
        myUserDetails = new MyUserDetails();
        myUserDetailsDto = mock(MyUserDetailsDTO.class);
    }

    @Test
    public void shouldGetAuthorities() {
        // given
        when(myUserDetailsDto.getRole()).thenReturn(Role.USER);

        // when
        myUserDetails.setMyUserDetailsDto(myUserDetailsDto);
        Collection<? extends GrantedAuthority> authorities = myUserDetails.getAuthorities();

        // then
        assertEquals(1, authorities.size());
        assertTrue(authorities.contains(new SimpleGrantedAuthority("ROLE_USER")));
    }

    @Test
    public void shouldGetAuthoritiesWhenRoleIsNull() {
        // given
        when(myUserDetailsDto.getRole()).thenReturn(null);

        // when
        myUserDetails.setMyUserDetailsDto(myUserDetailsDto);
        Collection<? extends GrantedAuthority> authorities = myUserDetails.getAuthorities();

        // then
        assertTrue(authorities.isEmpty());
    }

    @Test
    public void shouldGetPassword() {
        // given
        when(myUserDetailsDto.getPassword()).thenReturn("password");

        // when
        myUserDetails.setMyUserDetailsDto(myUserDetailsDto);
        String password = myUserDetails.getPassword();

        // then
        assertEquals("password", password);
    }

    @Test
    public void shouldGetUsername() {
        // given
        when(myUserDetailsDto.getEmail()).thenReturn("test@mail.ru");

        // when
        myUserDetails.setMyUserDetailsDto(myUserDetailsDto);
        String username = myUserDetails.getUsername();

        // then
        assertEquals("test@mail.ru", username);
    }

    @Test
    public void shouldIsAccountNonExpired() {
        // when & then
        assertTrue(myUserDetails.isAccountNonExpired());
    }

    @Test
    public void shouldIsAccountNonLocked() {
        // when & then
        assertTrue(myUserDetails.isAccountNonLocked());
    }

    @Test
    public void shouldIsCredentialsNonExpired() {
        // when & then
        assertTrue(myUserDetails.isCredentialsNonExpired());
    }

    @Test
    public void shouldIsEnabled() {
        // when & then
        assertTrue(myUserDetails.isEnabled());
    }

}
