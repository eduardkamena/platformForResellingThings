package ru.skypro.homework.service;

import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.dto.*;

import java.io.IOException;

public interface AdsService {

    Ads getAllAds();

    Ads getAdsMe(String email);

    Ad addAd(CreateOrUpdateAd createOrUpdateAd, String email, MultipartFile image);

    Comments getComments(int id);

    Comment addComment(int id, CreateOrUpdateComment createOrUpdateComment, String email);

    ExtendedAd getAds(int id);

    void removeAd(int id);

    Ad updateAds(CreateOrUpdateAd createOrUpdateAd, int id);

    void deleteComment(int adId, int id);

    Comment updateComment(int adId, int id, CreateOrUpdateComment createOrUpdateComment);

    void updateAdsImage(int id, MultipartFile image);

    byte[] getImage(String name) throws IOException;

    Comment getCommentDto(int adId, int id);
}
