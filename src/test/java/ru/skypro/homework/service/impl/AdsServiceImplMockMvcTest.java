package ru.skypro.homework.service.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.dto.Ad;
import ru.skypro.homework.dto.Ads;
import ru.skypro.homework.dto.CreateOrUpdateAd;
import ru.skypro.homework.dto.ExtendedAd;
import ru.skypro.homework.entity.AdEntity;
import ru.skypro.homework.entity.UserEntity;
import ru.skypro.homework.exception.AdsNotFoundException;
import ru.skypro.homework.exception.UserNotFoundException;
import ru.skypro.homework.mapper.AdsMapper;
import ru.skypro.homework.repository.AdsRepository;
import ru.skypro.homework.repository.CommentRepository;
import ru.skypro.homework.repository.UserRepository;
import ru.skypro.homework.service.ImageService;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AdsServiceImplMockMvcTest {

    @Mock
    private AdsRepository adsRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private CommentRepository commentRepository;

    @Mock
    private ImageService imageService;

    @Mock
    private AdsMapper adsMapper;

    @InjectMocks
    private AdsServiceImpl adsService;

    private AdEntity adEntity;
    private UserEntity userEntity;
    private CreateOrUpdateAd createOrUpdateAd;
    private MultipartFile image;

    @BeforeEach
    public void setUp() {
        userEntity = new UserEntity();
        userEntity.setEmail("test@mail.ru");

        adEntity = new AdEntity();
        adEntity.setId(1);
        adEntity.setAuthor(userEntity);

        createOrUpdateAd = new CreateOrUpdateAd();
        createOrUpdateAd.setTitle("Бабушкин компот");
        createOrUpdateAd.setPrice(100_000);
        createOrUpdateAd.setDescription("Няма-няма");

        image = mock(MultipartFile.class);
    }

    @Test
    public void shouldGetAllAds() {
        // given
        when(adsRepository.findAll()).thenReturn(List.of(adEntity));
        when(adsMapper.toListAdDTOFromListAdEntity(List.of(adEntity))).thenReturn(List.of(new Ad()));

        // when
        Ads result = adsService.getAllAds();

        // then
        assertNotNull(result);
        assertEquals(1, result.getCount());
        verify(adsRepository, times(1)).findAll();
    }

    @Test
    public void shouldGetAdsMe() {
        // given
        when(userRepository.findByEmail("test@mail.ru")).thenReturn(Optional.of(userEntity));
        when(adsRepository.findByAuthor(userEntity)).thenReturn(List.of(adEntity));
        when(adsMapper.toListAdDTOFromListAdEntity(List.of(adEntity))).thenReturn(List.of(new Ad()));

        // when
        Ads result = adsService.getAdsMe("test@mail.ru");

        // then
        assertNotNull(result);
        assertEquals(1, result.getCount());
        verify(userRepository, times(1)).findByEmail("test@mail.ru");
        verify(adsRepository, times(1)).findByAuthor(userEntity);
    }

    @Test
    public void shouldThrowGetAdsMeWhenUserNotFound() {
        // given
        when(userRepository.findByEmail("unknown@mail.ru")).thenReturn(Optional.empty());

        // when & then
        assertThrows(UserNotFoundException.class, () -> adsService.getAdsMe("unknown@mail.ru"));
    }

    @Test
    public void shouldAddAd() throws IOException {
        // given
        when(userRepository.findByEmail("test@mail.ru")).thenReturn(Optional.of(userEntity));
        when(adsMapper.toAdEntityFromCreateOrUpdateAdDTO(createOrUpdateAd)).thenReturn(adEntity);
        when(imageService.saveImage(image, "/ads")).thenReturn("image_path");
        when(adsMapper.toAdDTOFromAdEntity(adEntity)).thenReturn(new Ad());

        // when
        Ad result = adsService.addAd(createOrUpdateAd, "test@mail.ru", image);

        // then
        assertNotNull(result);
        verify(adsRepository, times(1)).save(adEntity);
        verify(imageService, times(1)).saveImage(image, "/ads");
    }

    @Test
    public void shouldThrowAddAdWhenUserNotFound() {
        // given
        when(userRepository.findByEmail("unknown@mail.ru")).thenReturn(Optional.empty());

        // when & then
        assertThrows(UserNotFoundException.class,
                () -> adsService.addAd(createOrUpdateAd, "unknown@mail.ru", image));
    }

    @Test
    public void shouldGetAds() {
        // given
        when(adsRepository.findById(1)).thenReturn(Optional.of(adEntity));
        when(adsMapper.toExtendedAdDTOFromAdEntity(adEntity)).thenReturn(new ExtendedAd());

        // when
        ExtendedAd result = adsService.getAds(1);

        // then
        assertNotNull(result);
        verify(adsRepository, times(1)).findById(1);
    }

    @Test
    public void shouldThrowGetAdsWhenAdNotFound() {
        // given
        when(adsRepository.findById(999)).thenReturn(Optional.empty());

        // when & then
        assertThrows(AdsNotFoundException.class, () -> adsService.getAds(999));
    }

    @Test
    public void shouldRemoveAd() {
        // given
        when(adsRepository.findById(1)).thenReturn(Optional.of(adEntity));
        doNothing().when(commentRepository).deleteAllByAd_Id(1);
        doNothing().when(imageService).deleteImage(adEntity.getImage());

        // when
        adsService.removeAd(1);

        // then
        verify(adsRepository, times(1)).delete(adEntity);
        verify(commentRepository, times(1)).deleteAllByAd_Id(1);
        verify(imageService, times(1)).deleteImage(adEntity.getImage());
    }

    @Test
    public void shouldUpdateAds() {
        // given
        when(adsRepository.findById(1)).thenReturn(Optional.of(adEntity));
        when(adsMapper.toAdDTOFromAdEntity(adEntity)).thenReturn(new Ad());

        // when
        Ad result = adsService.updateAds(createOrUpdateAd, 1);

        // then
        assertNotNull(result);
        verify(adsRepository, times(1)).save(adEntity);
    }

    @Test
    public void shouldUpdateImage() throws IOException {
        // given
        adEntity.setImage("old_image_path");
        when(adsRepository.findById(1)).thenReturn(Optional.of(adEntity));
        when(imageService.saveImage(image, "/ads")).thenReturn("new_image_path");

        // when
        adsService.updateImage(1, image);

        // then
        verify(imageService, times(1)).deleteImage("old_image_path");
        verify(imageService, times(1)).saveImage(image, "/ads");
        verify(adsRepository, times(1)).save(adEntity);
    }

    @Test
    public void shouldGetImage() throws IOException {
        // given
        when(imageService.getImage("image_name")).thenReturn(new byte[0]);

        // when
        byte[] result = adsService.getImage("image_name");

        // then
        assertNotNull(result);
        verify(imageService, times(1)).getImage("image_name");
    }

}
