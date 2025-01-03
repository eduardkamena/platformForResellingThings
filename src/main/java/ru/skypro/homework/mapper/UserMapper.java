package ru.skypro.homework.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.skypro.homework.security.MyUserDetailsDTO;
import ru.skypro.homework.dto.Register;
import ru.skypro.homework.dto.User;
import ru.skypro.homework.entity.UserEntity;

@Mapper(componentModel = "spring")
public interface UserMapper {

    @Mapping(source = "username", target = "email")
    UserEntity toUserEntityFromRegisterDTO(Register register);

    MyUserDetailsDTO toMyUserDetailsDTOFromUserEntity(UserEntity userEntity);

    User toUserDTOFromUserEntity(UserEntity userEntity);

}
