package ru.skypro.homework.security;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import ru.skypro.homework.exception.UserNotFoundException;
import ru.skypro.homework.mapper.UserMapper;
import ru.skypro.homework.repository.UserRepository;

@Service
@RequiredArgsConstructor
public class MyUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;
    private final MyUserDetails myUserDetails;
    private final UserMapper userMapper;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        MyUserDetailsDTO myUserDetailsDto = userRepository.findByEmail(email)
                .map(userMapper::toMyUserDetailsDTOFromUserEntity)
                .orElseThrow(() -> new UserNotFoundException(email));
        myUserDetails.setMyUserDetailsDto(myUserDetailsDto);
        return myUserDetails;
    }

}
