package ru.skypro.homework.mapper;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.dto.Register;
import ru.skypro.homework.dto.user.UpdateUserDTO;
import ru.skypro.homework.dto.user.UserDTO;
import ru.skypro.homework.entity.Image;
import ru.skypro.homework.entity.User;
import ru.skypro.homework.service.LoggingMethod;

import java.io.IOException;

/**
 * Класс конвертирует одни сущности, связанные с пользователем, в другие
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class UserMapper {

    private final LoggingMethod loggingMethod;

    /**
     * {@link Register} -> {@link User}
     * @param dto {@link Register}
     * @return entity class {@link User}
     */
    public static User mapFromRegisterToUserEntity(Register dto) {
        User entity = new User();
        entity.setUsername(dto.getUsername());
        entity.setPassword(dto.getPassword());
        entity.setFirstName(dto.getFirstName());
        entity.setLastName(dto.getLastName());
        entity.setPhone(dto.getPhone());
        entity.setRole(dto.getRole());
        return entity;

    }

    /**
     * {@link User} -> {@link UserDTO}
     * @param entity {@link User}
     * @return dto class {@link UserDTO}
     */
    public static UserDTO mapFromUserEntityToUserDTO(User entity) {
        UserDTO dto = new UserDTO();
        dto.setId(entity.getId());
        dto.setEmail(entity.getUsername());
        dto.setFirstName(entity.getFirstName());
        dto.setLastName(entity.getLastName());
        dto.setPhone(entity.getPhone());
        // Проверка image на null
        if (entity.getImage() != null) {
            dto.setImage(URLPhotoEnum.URL_PHOTO_CONSTANT.getString() + entity.getImage().getId());
        } else {
            dto.setImage(null);
        }
        return dto;
    }

    /**
     * {@link User} -> {@link UpdateUserDTO}
     * @param entity {@link User}
     * @return dto class {@link UpdateUserDTO}
     */
    public static UpdateUserDTO mapFromUserEntityToUpdateUserDTO(User entity) {
        UpdateUserDTO dto = new UpdateUserDTO();
        dto.setFirstName(entity.getFirstName());
        dto.setLastName(entity.getLastName());
        dto.setPhone(entity.getPhone());
        return dto;
    }

    /**
     * {@link MultipartFile} -> {@link Image}
     * @param image {@link MultipartFile}
     * @return {@link Image}
     */
    public Image mapMultipartFileToImage(MultipartFile image) {

        log.info("Запущен метод сервиса {}", loggingMethod.getMethodName());
        Image entity = new Image();
        try {
            entity.setData(image.getBytes());
            entity.setMediaType(image.getContentType());
            entity.setFileSize(image.getSize());
        } catch (IOException e) {
            throw new RuntimeException("Ошибка конвертации MultipartFile в PhotoEntity, " +
                    "место ошибки - userMapper.mapMultiPartFileToPhoto()");
        }
        return entity;
    }

}
