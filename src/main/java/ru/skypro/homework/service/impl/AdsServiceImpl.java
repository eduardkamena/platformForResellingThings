package ru.skypro.homework.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.dto.*;
import ru.skypro.homework.entity.AdEntity;
import ru.skypro.homework.exception.AdsNotFoundException;
import ru.skypro.homework.exception.CommentNotFoundException;
import ru.skypro.homework.exception.UserWithEmailNotFoundException;
import ru.skypro.homework.mapper.AdsMapper;
import ru.skypro.homework.mapper.CommentMapper;
import ru.skypro.homework.repository.AdsRepository;
import ru.skypro.homework.repository.CommentRepository;
import ru.skypro.homework.repository.UserRepository;
import ru.skypro.homework.service.AdsService;
import ru.skypro.homework.service.ImageService;

import javax.transaction.Transactional;
import java.io.IOException;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class AdsServiceImpl implements AdsService {

    private final AdsRepository adsRepository;
    private final UserRepository userRepository;
    private final CommentRepository commentRepository;
    private final ImageService imageService;
    private final AdsMapper adsMapper;
    private final CommentMapper commentMapper;

    @Override
    public Ads getAllAds() {
        List<AdEntity> adEntityList = adsRepository.findAll();
        List<Ad> adList = adsMapper.toDtos(adEntityList);
        Ads ads = new Ads();
        ads.setCount(adEntityList.size());
        ads.setResults(adList);
        return ads;
    }

    @Override
    public Ads getAdsMe(String email) {
        List<AdEntity> adEntityList = adsRepository.findByAuthor(userRepository.findByEmail(email)
                .orElseThrow(() -> new UserWithEmailNotFoundException(email)));
        List<Ad> adList = adsMapper.toDtos(adEntityList);
        Ads ads = new Ads();
        ads.setResults(adList);
        ads.setCount(adEntityList.size());
        return ads;
    }

    @Override
    public Ad addAd(CreateOrUpdateAd createOrUpdateAd, String email, MultipartFile image) {
        AdEntity adEntity = adsMapper.toAdsFromCreateAds(createOrUpdateAd);
        adEntity.setAuthor(userRepository.findByEmail(email)
                .orElseThrow(() -> new UserWithEmailNotFoundException(email)));
        adEntity.setImage(imageService.saveImage(image, "/ads"));
        adsRepository.save(adEntity);
        return adsMapper.toAdsDto(adEntity);
    }

    @Override
    public ExtendedAd getAds(int id) {
        AdEntity adEntity = adsRepository.findById(id)
                .orElseThrow(() -> new AdsNotFoundException("AdEntity not found by id: " + id));
        return adsMapper.toFullAds(adEntity);
    }

    @Transactional
    @Override
    public void removeAd(int id) {
        commentRepository.deleteAllByAd_Id(id);
        AdEntity adEntity = adsRepository.findById(id)
                .orElseThrow(() -> new AdsNotFoundException("AdEntity not found by id: " + id));
        imageService.deleteFileIfNotNull(adEntity.getImage());
        log.info("Removed AdEntity with id: ", id);
        adsRepository.delete(adEntity);
    }

    @Override
    public Ad updateAds(CreateOrUpdateAd createOrUpdateAd, int id) {
        AdEntity adEntity = adsRepository.findById(id)
                .orElseThrow(() -> new AdsNotFoundException("AdEntity not found by id: " + id));
        adsMapper.updateAds(createOrUpdateAd, adEntity);
        adsRepository.save(adEntity);
        log.info("Updated AdEntity with id: ", id);
        return adsMapper.toAdsDto(adEntity);
    }

    @Override
    public void updateImage(int id, MultipartFile image) {
        AdEntity adEntity = adsRepository.findById(id)
                .orElseThrow(() -> new AdsNotFoundException("AdEntity not found"));
        imageService.deleteFileIfNotNull(adEntity.getImage());
        adEntity.setImage(imageService.saveImage(image, "/ads"));
        adsRepository.save(adEntity);
    }

    @Override
    public byte[] getImage(String name) throws IOException {
        return imageService.getImage(name);
    }

    public String getUserNameOfComment(int id) {
        return commentRepository.findById(id)
                .orElseThrow(() -> new CommentNotFoundException("CommentEntity not found"))
                .getAuthor().getEmail();
    }

}
