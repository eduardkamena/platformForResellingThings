package ru.skypro.homework.service;

import org.springframework.security.core.Authentication;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.dto.announce.AdDTO;
import ru.skypro.homework.dto.announce.AdsDTO;
import ru.skypro.homework.dto.announce.CreateOrUpdateAdDTO;
import ru.skypro.homework.dto.announce.ExtendedAdDTO;

import java.io.IOException;

public interface AdService {

    AdsDTO getAllAds();

    AdDTO createAd(CreateOrUpdateAdDTO properties, MultipartFile image, Authentication authentication) throws IOException;

    ExtendedAdDTO getAdById(Integer id);

    boolean deleteAdById(Integer id) throws IOException;

    AdDTO updateAdById(Integer id, CreateOrUpdateAdDTO dto);

    AdsDTO getCurrentUserAds(String username);

    void updateImageOnAdById(Integer id, MultipartFile image) throws IOException;

    boolean isAuthorAd(String username, Integer id);

}
