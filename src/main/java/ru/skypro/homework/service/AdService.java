package ru.skypro.homework.service;

import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.dto.announce.AdDTO;
import ru.skypro.homework.dto.announce.AdsDTO;
import ru.skypro.homework.dto.announce.CreateOrUpdateAdDTO;

public interface AdService {

    AdsDTO getAllAds();

    AdDTO addAd(CreateOrUpdateAdDTO properties, MultipartFile image, Authentication authentication) throws IOException;

    ExtendedAd getAds(Integer id);

    boolean removeAd(Integer id) throws IOException;

    Ad updateAds(Integer id, CreateOrUpdateAd dto);

    Ads getAdsMe(String username);

    void updateImage(Integer id, MultipartFile image) throws IOException;

}
