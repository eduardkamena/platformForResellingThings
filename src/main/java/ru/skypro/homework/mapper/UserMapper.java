package ru.skypro.homework.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.skypro.homework.security.MyUserDetailsDTO;
import ru.skypro.homework.dto.Register;
import ru.skypro.homework.dto.User;
import ru.skypro.homework.entity.UserEntity;

/**
 * Интерфейс для маппинга между сущностями и DTO, связанными с пользователями.
 * <p>
 * Этот интерфейс использует MapStruct для преобразования объектов между сущностями (UserEntity)
 * и DTO (Register, User и MyUserDetailsDTO в пакете security).
 * </p>
 *
 * @see Mapper
 * @see Mapping
 */
@Mapper(componentModel = "spring")
public interface UserMapper {

    /**
     * Преобразует объект Register в сущность UserEntity.
     * <p>
     * Поле "username" из Register маппится в поле "email" в UserEntity.
     * </p>
     *
     * @param register DTO для регистрации пользователя.
     * @return сущность UserEntity.
     */
    @Mapping(source = "username", target = "email")
    UserEntity toUserEntityFromRegisterDTO(Register register);

    /**
     * Преобразует сущность UserEntity в DTO MyUserDetailsDTO в пакете security.
     *
     * @param userEntity сущность пользователя.
     * @return DTO MyUserDetailsDTO.
     */
    MyUserDetailsDTO toMyUserDetailsDTOFromUserEntity(UserEntity userEntity);

    /**
     * Преобразует сущность UserEntity в DTO User.
     *
     * @param userEntity сущность пользователя.
     * @return DTO User.
     */
    User toUserDTOFromUserEntity(UserEntity userEntity);

}
