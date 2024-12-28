package ru.skypro.homework.service;

import org.springframework.security.core.Authentication;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.dto.announce.AdDTO;
import ru.skypro.homework.dto.announce.AdsDTO;
import ru.skypro.homework.dto.announce.CreateOrUpdateAdDTO;
import ru.skypro.homework.dto.announce.ExtendedAdDTO;
import ru.skypro.homework.entity.Ad;
import ru.skypro.homework.exception.AdNotFoundException;

import java.io.IOException;

public interface AdService {

    /**
     * /* Добавляет новое объявление.
     *
     *       @param createOrUpdateAdDto DTO с информацией о новом объявлении.
     *       @param image              Изображение объявления.
     *       @param authentication      Аутентификация пользователя.
     *       @return DTO с информацией о созданном объявлении.
     *       @throws IOException Если произошла ошибка при загрузке изображения.
     *      */
    AdDTO addAd(CreateOrUpdateAdDTO createOrUpdateAdDto, MultipartFile image, Authentication authentication) throws IOException;

    /**
     * Возвращает список всех объявлений.
     *
     *       @return DTO со списком всех объявлений.
     */
    AdsDTO getAllAds();

    /**
     * Возвращает список объявлений пользователя.
     *
     *       @param username Имя пользователя.
     *       @return DTO со списком объявлений пользователя.
     */
    AdsDTO getAdsMe(String username);

    /**
     * Возвращает объявление по ID.
     *
     *       @param id ID объявления.
     *       @return Объявление.
     */
    Ad getAd(Integer id);

    /**
     *  Возвращает изображение объявления.
     *
     *       @param id ID объявления.
     *       @return Изображение объявления.
     *       @throws IOException Если произошла ошибка при чтении изображения.
     */
    byte[] getImage(Integer id) throws IOException;

    /**
     * Возвращает подробную информацию о объявлении.
     *
     *       @param id ID объявления.
     *       @return DTO с подробной информацией о объявлении.
     */
    ExtendedAdDTO getAds(Integer id);

    /**
     * Обновляет объявление.
     *
     *       @param id                    Идентификатор объявления.
     *       @param createOrUpdateAdDto    Данные для обновления объявления.
     *       @param authentication        Аутентификация пользователя.
     *       @return Обновленное объявление в виде DTO.
     */
    AdDTO updateAds(Integer id, CreateOrUpdateAdDTO createOrUpdateAdDto, Authentication authentication);

    /**
     * Обновляет изображение объявления.
     *
     *       @param id       Идентификатор объявления.
     *       @param image    Новый файл изображения.
     *       @param authentication Аутентификация пользователя.
     *       @return Бинарные данные обновленного изображения.
     *       @throws IOException          Ошибка ввода-вывода.
     */
    byte[] updateImage(Integer id, MultipartFile image, Authentication authentication) throws IOException;

    /**
     * Удаляет объявление.
     *
     *       @param id                    Идентификатор объявления.
     *       @param authentication        Аутентификация пользователя.
     *       @throws AdNotFoundException  Исключение, если объявление не найдено.
     */
    void removeAd(Integer id, Authentication authentication);

}
