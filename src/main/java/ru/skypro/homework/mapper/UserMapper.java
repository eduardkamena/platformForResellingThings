package ru.skypro.homework.mapper;

import org.mapstruct.*;
import ru.skypro.homework.dto.Register;
import ru.skypro.homework.dto.user.MyUserDetailsDto;
import ru.skypro.homework.dto.user.UserDto;
import ru.skypro.homework.entity.User;

@Mapper(componentModel = "spring")
public interface UserMapper {

    @Mapping(source = "username", target = "email")
    User toUser(Register register);

    MyUserDetailsDto toMyUserDetailsDto(User user);

    UserDto toUserDto(User user);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "image", ignore = true)
    void updateUserFromUserDto(UserDto userDto, @MappingTarget User user);
}
