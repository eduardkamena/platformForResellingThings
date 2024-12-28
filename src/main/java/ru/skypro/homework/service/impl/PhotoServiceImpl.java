package ru.skypro.homework.service.impl;

import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.skypro.homework.entity.Image;
import ru.skypro.homework.exception.PhotoOnDatabaseIsAbsentException;
import ru.skypro.homework.exception.PhotoOnPcIsAbsentException;
import ru.skypro.homework.exception.UserNotFoundException;
import ru.skypro.homework.repository.ImageRepository;
import ru.skypro.homework.service.ImageService;
import ru.skypro.homework.service.LoggingMethod;
import ru.skypro.homework.service.PhotoService;

import java.io.IOException;

@Service
@Slf4j
@RequiredArgsConstructor
public class PhotoServiceImpl implements PhotoService {

    private final ImageRepository imageRepository;
    private final ImageService imageService;
    private final LoggingMethod loggingMethod;

    /**
     * Метод возвращает фото с ПК, а если его там нет по каким-то причинам,
     * то перенаправляет запрос фото в базу данных.
     * @param photoId
     * @return byte[] массив байт
     * @throws IOException
     */
    public byte[] getPhoto(Integer photoId) throws NoSuchFieldException {
        log.info("Запущен метод сервиса {}", loggingMethod.getMethodName());
        log.info("photoId: {}", photoId);

        Image photo = imageRepository.findById(photoId).orElseThrow(
                () -> new PhotoOnDatabaseIsAbsentException("Ошибка: фото отсутствует в базе данных "));
        log.info("Фото найдено - {}", photo.getData() != null);

        //Если картинка запрошенная с ПК не получена по какой-то причине, достаем ее из БД
        if (imageService.getPhotoFromDisk(photo) == null) {
            return imageRepository.findById(photoId).orElseThrow(
                    () -> new PhotoOnPcIsAbsentException("Ошибка: фото отсутствует на ПК ")).getData();
        }
        //Если предыдущее условие не выполнилось и с картинкой все в порядке, то достаем ее с ПК
        return imageService.getPhotoFromDisk(photo);
    }

}
