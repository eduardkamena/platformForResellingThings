package ru.skypro.homework.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import ru.skypro.homework.security.MyUserDetailsDTO;
import ru.skypro.homework.dto.Register;
import ru.skypro.homework.dto.User;
import ru.skypro.homework.entity.UserEntity;

@Mapper(componentModel = "spring")
public interface UserMapper {

    @Mapping(source = "username", target = "email")
    UserEntity toUser(Register register);

    MyUserDetailsDTO toMyUserDetailsDto(UserEntity userEntity);

    User toUserDto(UserEntity userEntity);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "image", ignore = true)
    void updateUserFromUserDto(User user, @MappingTarget UserEntity userEntity);
}
