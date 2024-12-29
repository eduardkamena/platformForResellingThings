package ru.skypro.homework.service;

import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.dto.*;

import java.io.IOException;

public interface AdsService {

    Ads getAllAds();

    Ads getAdsMe(String email);

    Ad addAd(CreateOrUpdateAd createOrUpdateAd, String email, MultipartFile image);

    ExtendedAd getAds(int id);

    void removeAd(int id);

    Ad updateAds(CreateOrUpdateAd createOrUpdateAd, int id);

    void updateImage(int id, MultipartFile image);

    byte[] getImage(String name) throws IOException;

}
