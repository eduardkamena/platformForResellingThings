package ru.skypro.homework.service;

import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.dto.*;

import java.io.IOException;

/**
 * Интерфейс для работы с объявлениями.
 * <p>
 * Этот интерфейс предоставляет методы для выполнения операций с объявлениями,
 * таких как получение всех объявлений, добавление, обновление и удаление объявлений,
 * а также управление изображениями объявлений.
 * </p>
 */
public interface AdsService {

    /**
     * Получает все объявления.
     *
     * @return объект {@link Ads}, содержащий список всех объявлений и их количество.
     */
    Ads getAllAds();

    /**
     * Получает объявления текущего пользователя.
     *
     * @param email email пользователя.
     * @return объект {@link Ads}, содержащий список объявлений пользователя и их количество.
     */
    Ads getAdsMe(String email);

    /**
     * Добавляет новое объявление.
     *
     * @param createOrUpdateAd данные для создания объявления.
     * @param email            email пользователя, создающего объявление.
     * @param image            изображение объявления.
     * @return объект {@link Ad}, представляющий созданное объявление.
     * @throws IOException если возникает ошибка при обработке изображения.
     */
    Ad addAd(CreateOrUpdateAd createOrUpdateAd, String email, MultipartFile image) throws IOException;

    /**
     * Получает информацию об объявлении по его идентификатору.
     *
     * @param id идентификатор объявления.
     * @return объект {@link ExtendedAd}, содержащий расширенную информацию об объявлении.
     */
    ExtendedAd getAds(int id);

    /**
     * Удаляет объявление по его идентификатору.
     *
     * @param id идентификатор объявления.
     */
    void removeAd(int id);

    /**
     * Обновляет информацию об объявлении.
     *
     * @param createOrUpdateAd новые данные для объявления.
     * @param id               идентификатор объявления.
     * @return объект {@link Ad}, представляющий обновленное объявление.
     */
    Ad updateAds(CreateOrUpdateAd createOrUpdateAd, int id);

    /**
     * Обновляет изображение объявления.
     *
     * @param id    идентификатор объявления.
     * @param image новое изображение.
     * @throws IOException если возникает ошибка при обработке изображения.
     */
    void updateImage(int id, MultipartFile image) throws IOException;

    /**
     * Получает изображение объявления по его имени.
     *
     * @param name имя изображения.
     * @return массив байтов, представляющий изображение.
     * @throws IOException если возникает ошибка при чтении изображения.
     */
    byte[] getImage(String name) throws IOException;

}
