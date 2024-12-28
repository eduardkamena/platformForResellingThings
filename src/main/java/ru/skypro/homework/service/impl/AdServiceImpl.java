package ru.skypro.homework.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.dto.announce.AdDTO;
import ru.skypro.homework.dto.announce.AdsDTO;
import ru.skypro.homework.dto.announce.CreateOrUpdateAdDTO;
import ru.skypro.homework.dto.announce.ExtendedAdDTO;
import ru.skypro.homework.entity.Ad;
import ru.skypro.homework.entity.Comment;
import ru.skypro.homework.entity.Image;
import ru.skypro.homework.entity.User;
import ru.skypro.homework.exception.AdNotFoundException;
import ru.skypro.homework.mapper.AdMapper;
import ru.skypro.homework.repository.CommentRepository;
import ru.skypro.homework.repository.ImageRepository;
import ru.skypro.homework.repository.AdRepository;
import ru.skypro.homework.repository.UserRepository;
import ru.skypro.homework.service.AdService;
import ru.skypro.homework.service.LoggingMethod;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

import static java.nio.file.StandardOpenOption.CREATE_NEW;

@Service
@Slf4j
@Transactional
@RequiredArgsConstructor
public class AdServiceImpl implements AdService {

    @Value("${path.to.ad.photo}")
    private String photoPath;
    private final AdMapper adMapper;
    private final AdRepository adRepository;
    private final CommentRepository commentRepository;
    private final UserRepository userRepository;
    private final ImageRepository imageRepository;

    @Override
    public AdDTO addAd(CreateOrUpdateAdDTO createOrUpdateAdDto, MultipartFile image, Authentication authentication) throws IOException {
        User user = userRepository.findByUsername(authentication.getName()).orElseThrow(() -> new UsernameNotFoundException(authentication.getName()));
        Ad ad = adMapper.toEntity(createOrUpdateAdDto);
        ad.setAuthor(user);
        ad = adRepository.save(ad);
        return adMapper.toAdDto(adRepository.save(uploadImage(ad, image)));
    }

    private Ad uploadImage(Ad ad, MultipartFile image) throws IOException {
        Path filePath = Path.of(photoPath, ad.hashCode() + "." + StringUtils.getFilenameExtension(image.getOriginalFilename()));
        Files.createDirectories(filePath.getParent());
        Files.deleteIfExists(filePath);
        try (
                InputStream is = image.getInputStream();
                OutputStream os = Files.newOutputStream(filePath, CREATE_NEW);
                BufferedInputStream bis = new BufferedInputStream(is, 1024);
                BufferedOutputStream bos = new BufferedOutputStream(os, 1024)
        ) {
            bis.transferTo(bos);
            // Создаем объект Image и сохраняем его
            Image adImage = new Image();
            adImage.setFilePath(filePath.toString());
            adImage = imageRepository.save(adImage); // Сохраняем Image в репозитории

            // Устанавливаем сохраненное Image в Ad
            ad.setImage(adImage);
            return adRepository.save(ad); // Сохраняем Ad в репозитории
        }

    }

    @Override
    public AdsDTO getAllAds() {
        return adMapper.toAdsDto(adRepository.findAll());
    }

    @Override
    public AdsDTO getAdsMe(String username) {
        User user = userRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException(username));
        return adMapper.toAdsDto(adRepository.findAllByAuthorId(user.getId()));
    }

    @Override
    public Ad getAd(Integer id) {
        return adRepository.findById(id).orElseThrow(() -> new AdNotFoundException(id));
    }

    @Override
    public byte[] getImage(Integer id) throws IOException {
        Ad ad = getAd(id);
        if (ad.getImage() == null) {
            throw new FileNotFoundException("Image not found for Ad with ID " + id);
        }
        return Files.readAllBytes(Path.of(ad.getImage().getFilePath())); // Получаем путь из объекта Image
    }

    @Override
    public ExtendedAdDTO getAds(Integer id) {
        return adMapper.toExtendedAdDto(getAd(id));
    }

    @Override
    public AdDTO updateAds(Integer id, CreateOrUpdateAdDTO createOrUpdateAdDto, Authentication authentication) {
        Ad ad = getAd(id);
        ad.setDescription(createOrUpdateAdDto.getDescription());
        ad.setTitle(createOrUpdateAdDto.getTitle());
        ad.setPrice(createOrUpdateAdDto.getPrice());
        return adMapper.toAdDto(adRepository.save(ad));
    }

    @Override
    public byte[] updateImage(Integer id, MultipartFile image, Authentication authentication) throws IOException {
        Ad ad = getAd(id);
        ad = uploadImage(ad, image); // Сохраняем новое изображение
        return Files.readAllBytes(Path.of(ad.getImage().getFilePath())); // Читаем данные из нового изображения
    }

    @Override
    public void removeAd(Integer id, Authentication authentication) throws AdNotFoundException {
        if (adRepository.existsById(id)) {

            List<Comment> comments = commentRepository.findByAdPk(id);
            commentRepository.deleteAll(comments);

            adRepository.delete(getAd(id));
        } else {
            throw new AdNotFoundException(id);
        }
    }

}
