package ru.skypro.homework.service;

import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.dto.*;

import java.io.IOException;

public interface AdsService {

    ResponseWrapperAds getAllAds();

    ResponseWrapperAds getAdsMe(String email);

    AdsDto addAd(CreateAds createAds, String email, MultipartFile image);

    ResponseWrapperComment getComments(Integer id);

    CommentDto addComment(Integer id, CreateComment createComment, String email);

    FullAds getAds(Integer id);

    void removeAd(Integer id);

    AdsDto updateAds(CreateAds createAds, Integer id);

    void deleteComment(Integer adId, Integer id);

    CommentDto updateComment(Integer adId, Integer id, CreateComment createComment);

    void updateAdsImage(Integer id, MultipartFile image);

    byte[] getImage(String name) throws IOException;

    CommentDto getCommentDto(Integer adId,Integer id);
}
