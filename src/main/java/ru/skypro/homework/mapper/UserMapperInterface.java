package ru.skypro.homework.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;
import ru.skypro.homework.dto.Register;
import ru.skypro.homework.dto.user.UserDTO;
import ru.skypro.homework.entity.User;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING,
        unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface UserMapperInterface {


    @Mapping(target = "username", source = "email")
    @Mapping(target = "image", ignore = true)
    User toUserEntity(UserDTO userDTO);

    @Mapping(target = "email", source = "username")
    @Mapping(target = "image", ignore = true)
    UserDTO toUserDTO(User user);

    User fromRegisterToUser(Register register);
}

