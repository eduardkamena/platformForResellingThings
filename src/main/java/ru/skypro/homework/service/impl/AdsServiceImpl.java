package ru.skypro.homework.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.dto.*;
import ru.skypro.homework.entity.AdEntity;
import ru.skypro.homework.exception.AdsNotFoundException;
import ru.skypro.homework.exception.UserNotFoundException;
import ru.skypro.homework.mapper.AdsMapper;
import ru.skypro.homework.repository.AdsRepository;
import ru.skypro.homework.repository.CommentRepository;
import ru.skypro.homework.repository.UserRepository;
import ru.skypro.homework.service.AdsService;
import ru.skypro.homework.service.ImageService;

import javax.transaction.Transactional;
import java.io.IOException;
import java.util.List;

/**
 * Сервис для работы с объявлениями.
 * <p>
 * Этот класс реализует интерфейс {@link AdsService} и предоставляет методы для выполнения операций
 * с объявлениями, таких как получение всех объявлений, добавление, обновление и удаление объявлений,
 * а также управление изображениями объявлений.
 * </p>
 *
 * @see Service
 * @see Slf4j
 * @see RequiredArgsConstructor
 * @see AdsService
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class AdsServiceImpl implements AdsService {

    private final AdsRepository adsRepository;
    private final UserRepository userRepository;
    private final CommentRepository commentRepository;
    private final ImageService imageService;
    private final AdsMapper adsMapper;

    /**
     * Получает все объявления.
     *
     * @return объект {@link Ads}, содержащий список всех объявлений и их количество.
     */
    @Override
    public Ads getAllAds() {
        log.info("getAllAds method from AdsService was invoked");

        List<AdEntity> adEntityList = adsRepository.findAll();
        List<Ad> adList = adsMapper.toListAdDTOFromListAdEntity(adEntityList);
        Ads ads = new Ads();
        ads.setCount(adEntityList.size());
        ads.setResults(adList);

        log.info("Successfully got all Ads with count: {}", adEntityList.size());
        return ads;
    }

    /**
     * Получает объявления текущего пользователя.
     *
     * @param email email пользователя.
     * @return объект {@link Ads}, содержащий список объявлений пользователя и их количество.
     * @throws UserNotFoundException если пользователь с указанным email не найден.
     */
    @Override
    public Ads getAdsMe(String email) {
        log.info("getAdsMe method from AdsService was invoked");

        List<AdEntity> adEntityList = adsRepository.findByAuthor(userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("User not found by username (email): " + email)));
        List<Ad> adList = adsMapper.toListAdDTOFromListAdEntity(adEntityList);
        Ads ads = new Ads();
        ads.setResults(adList);
        ads.setCount(adEntityList.size());

        log.info("Successfully got Ads with count: {} for user: {}", adEntityList.size(), email);
        return ads;
    }

    /**
     * Добавляет новое объявление.
     *
     * @param createOrUpdateAd данные для создания объявления.
     * @param email            email пользователя, создающего объявление.
     * @param image            изображение объявления.
     * @return объект {@link Ad}, представляющий созданное объявление.
     * @throws IOException           если возникает ошибка при обработке изображения.
     * @throws UserNotFoundException если пользователь с указанным email не найден.
     */
    @Override
    public Ad addAd(CreateOrUpdateAd createOrUpdateAd, String email, MultipartFile image) throws IOException {
        log.info("addAd method from AdsService was invoked");

        AdEntity adEntity = adsMapper.toAdEntityFromCreateOrUpdateAdDTO(createOrUpdateAd);
        adEntity.setAuthor(userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("User not found by username (email): " + email)));
        adEntity.setImage(imageService.saveImage(image, "/ads"));
        adsRepository.save(adEntity);

        log.info("Successfully added Ad with id: {} by author: {}", adEntity.getId(), email);
        return adsMapper.toAdDTOFromAdEntity(adEntity);
    }

    /**
     * Получает информацию об объявлении по его идентификатору.
     *
     * @param id идентификатор объявления.
     * @return объект {@link ExtendedAd}, содержащий расширенную информацию об объявлении.
     * @throws AdsNotFoundException если объявление с указанным идентификатором не найдено.
     */
    @Override
    public ExtendedAd getAds(int id) {
        log.info("getAds method from AdsService was invoked");

        AdEntity adEntity = adsRepository.findById(id)
                .orElseThrow(() -> new AdsNotFoundException("Ad not found by id: " + id));

        log.info("Successfully found Ad in repository with id: {}", id);
        return adsMapper.toExtendedAdDTOFromAdEntity(adEntity);
    }

    /**
     * Удаляет объявление по его идентификатору.
     *
     * @param id идентификатор объявления.
     * @throws AdsNotFoundException если объявление с указанным идентификатором не найдено.
     */
    @Transactional
    @Override
    public void removeAd(int id) {
        log.info("removeAd method from AdsService was invoked");

        commentRepository.deleteAllByAd_Id(id);
        AdEntity adEntity = adsRepository.findById(id)
                .orElseThrow(() -> new AdsNotFoundException("Ad not found by id: " + id));
        imageService.deleteImage(adEntity.getImage());

        log.info("Successfully removed Ad with id: {}", id);
        adsRepository.delete(adEntity);
    }

    /**
     * Обновляет информацию об объявлении.
     *
     * @param createOrUpdateAd новые данные для объявления.
     * @param id               идентификатор объявления.
     * @return объект {@link Ad}, представляющий обновленное объявление.
     * @throws AdsNotFoundException если объявление с указанным идентификатором не найдено.
     */
    @Override
    public Ad updateAds(CreateOrUpdateAd createOrUpdateAd, int id) {
        log.info("updateAds method from AdsService was invoked");

        AdEntity adEntity = adsRepository.findById(id)
                .orElseThrow(() -> new AdsNotFoundException("Ad not found by id: " + id));
        adsMapper.toCreateOrUpdateAdDTOFromAdEntity(createOrUpdateAd, adEntity);
        adsRepository.save(adEntity);

        log.info("Successfully updated Ad with id: {}", id);
        return adsMapper.toAdDTOFromAdEntity(adEntity);
    }

    /**
     * Обновляет изображение объявления.
     *
     * @param id    идентификатор объявления.
     * @param image новое изображение.
     * @throws IOException          если возникает ошибка при обработке изображения.
     * @throws AdsNotFoundException если объявление с указанным идентификатором не найдено.
     */
    @Override
    public void updateImage(int id, MultipartFile image) throws IOException {
        log.info("updateImage method from AdsService was invoked");

        AdEntity adEntity = adsRepository.findById(id)
                .orElseThrow(() -> new AdsNotFoundException("Ad not found by id: " + id));
        imageService.deleteImage(adEntity.getImage());
        log.info("Successfully deleted old image for Ad with id: {}", id);

        adEntity.setImage(imageService.saveImage(image, "/ads"));
        adsRepository.save(adEntity);

        log.info("Successfully updated image for Ad with id: {}", id);
    }

    /**
     * Получает изображение объявления по его имени.
     *
     * @param name имя изображения.
     * @return массив байтов, представляющий изображение.
     * @throws IOException если возникает ошибка при чтении изображения.
     */
    @Override
    public byte[] getImage(String name) throws IOException {
        log.info("getImage method from AdsService was invoked " +
                "and successfully got Ad image with name: {}", name);
        return imageService.getImage(name);
    }

}
