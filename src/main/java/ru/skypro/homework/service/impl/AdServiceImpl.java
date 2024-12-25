package ru.skypro.homework.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.dto.announce.AdDTO;
import ru.skypro.homework.dto.announce.AdsDTO;
import ru.skypro.homework.dto.announce.CreateOrUpdateAdDTO;
import ru.skypro.homework.dto.announce.ExtendedAdDTO;
import ru.skypro.homework.entity.Ad;
import ru.skypro.homework.entity.User;
import ru.skypro.homework.mapper.AdMapper;
import ru.skypro.homework.repository.ImageRepository;
import ru.skypro.homework.repository.AdRepository;
import ru.skypro.homework.service.AdService;
import ru.skypro.homework.service.LoggingMethod;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Service
@Slf4j
@Transactional
@RequiredArgsConstructor
public class AdServiceImpl implements AdService {

    private final AdRepository adRepository;
    private final ImageRepository imageRepository;
    private final AdMapper adMapper;
    private final ImageServiceImpl imageService;
    private final UserServiceImpl userService;
    private final LoggingMethod loggingMethod;

    @Value("${path.to.photos.folder}")
    private String photoDir;


    /**
     * Метод возвращает список всех объявлений в виде DTO {@link AdDTO}.
     *
     * @return возвращает все объявления из БД
     */
    @Override
    public AdsDTO getAllAds() {
        List<AdDTO> dto = adRepository.findAll().stream()
                .map(adMapper::mapToAdDto)
                .collect(Collectors.toList());
        return new AdsDTO(dto.size(), dto);
    }

    /**
     * Метод добавляет новое объявление в БД
     *
     * @param properties - DTO модель класса {@link CreateOrUpdateAdDTO};
     * @param image      - фотография объявления
     * @return возвращает объявление в качестве DTO модели
     */
    @Override
    public AdDTO createAd(CreateOrUpdateAdDTO properties,
                          MultipartFile image,
                          Authentication authentication) throws IOException {

        log.info("Запущен метод сервиса {}", loggingMethod.getMethodName());

        //создаем сущность
        Ad entity = new Ad();

        //заполняем поля title, price и description, которые берутся из properties
        entity.setTitle(properties.getTitle());
        entity.setPrice(properties.getPrice());
        entity.setDescription(properties.getDescription());

        //заполняем поле author
        entity.setAuthor(userService.getUser(authentication.getName()));

        //заполняем поля
        entity = (Ad) imageService.updateEntitiesPhoto(image, entity);
        log.info("Сущность Ad entity, сформированная в {}", loggingMethod.getMethodName());

        //сохранение сущности Ad entity в БД
        adRepository.save(entity);

        //возврат DTO Ad из метода
        return adMapper.mapToAdDto(entity);
    }

    /**
     * Метод получает информацию об объявлении по id
     *
     * @param id - id объявления
     * @return возвращает DTO модель объявления
     */
    @Override
    public ExtendedAdDTO getAdById(Integer id) {

        log.info("Запущен метод сервиса {}", loggingMethod.getMethodName());
        Ad entity = adRepository.findById(id).orElseThrow(() -> new NoSuchElementException("No value present"));
        return adMapper.mapToExtendedAdDto(entity);
    }

    /**
     * Метод удаляет объявление по id
     *
     * @param id - id объявления
     * @return boolean
     */
    @Transactional
    @Override
    public boolean deleteAdById(Integer id) throws IOException {

        log.info("Запущен метод сервиса {}", loggingMethod.getMethodName());
        Ad ad = adRepository.findById(id).orElseThrow(() -> new NoSuchElementException("No value present"));

        if (ad != null) {
            //удаление объявления из БД
            adRepository.delete(ad);
            //удаление фото из БД
            imageRepository.delete(ad.getImage());
            //получение пути из сущности объявления
            String filePath = ad.getFilePath();
            //создание пути к файлу
            Path path = Path.of(filePath);
            //удаление файла с ПК
            Files.delete(path);
            return true;
        } else {
            return false;
        }
    }

    /**
     * Метод изменяет объявление
     *
     * @param id  - id объявления
     * @param dto - DTO модель класса {@link CreateOrUpdateAdDTO};
     * @return возвращает DTO модель объявления
     */
    @Transactional
    @Override
    public AdDTO updateAdById(Integer id, CreateOrUpdateAdDTO dto) {

        log.info("Запущен метод сервиса {}", loggingMethod.getMethodName());
        Ad entity = adRepository.findById(id).orElseThrow(() -> new NoSuchElementException("No value present"));

        entity.setTitle(dto.getTitle());
        entity.setPrice(dto.getPrice());
        entity.setDescription(dto.getDescription());

        adRepository.save(entity);
        return adMapper.mapToAdDto(entity);
    }

    /**
     * Метод получает все объявления данного пользователя
     *
     * @param username - логин пользователя
     * @return возвращает DTO - список моделей объявления пользователя
     */
    @Transactional
    @Override
    public AdsDTO getCurrentUserAds(String username) {

        log.info("Запущен метод сервиса {}", loggingMethod.getMethodName());
        User author = userService.getUser(username);
        log.info("объект UserEntity получен из БД");

        List<AdDTO> ads = adRepository.findByAuthor(author).stream()
                .map(adMapper::mapToAdDto)
                .collect(Collectors.toList());
        log.info("Получен список объявлений пользователя ads");

        AdsDTO adsDto = new AdsDTO(ads.size(), ads);
        log.info("Сформирован возвращаемый объект adsDto");
        return adsDto;
    }

    @Override
    public void updateImageOnAdById(Integer id, MultipartFile image) throws IOException {

        log.info("Запущен метод сервиса {}", loggingMethod.getMethodName());
        //достаем объявление из БД
        Ad entity = adRepository.findById(id).orElseThrow(RuntimeException::new);
        //заполняем поля и получаем сущность в переменную
        entity = (Ad) imageService.updateEntitiesPhoto(image, entity);
        log.info("Ad entity cоздано - {}", entity);
        //сохранение сущности user в БД
        assert entity != null;
        adRepository.save(entity);
    }

    @Override
    public boolean isAuthorAd(String username, Integer id) {
        log.info("Использован метод сервиса: {}", loggingMethod.getMethodName());

        Ad adEntity = adRepository.findById(id).orElseThrow(RuntimeException::new);
        return adEntity.getAuthor().getUsername().equals(username);
    }

}
