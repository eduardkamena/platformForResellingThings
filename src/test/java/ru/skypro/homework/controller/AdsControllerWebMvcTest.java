package ru.skypro.homework.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.config.TestSecurityConfig;
import ru.skypro.homework.dto.Ad;
import ru.skypro.homework.dto.Ads;
import ru.skypro.homework.dto.CreateOrUpdateAd;
import ru.skypro.homework.dto.ExtendedAd;
import ru.skypro.homework.service.AdsService;

import java.util.Collections;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AdsController.class)
@Import(TestSecurityConfig.class)
@ActiveProfiles("test")
public class AdsControllerWebMvcTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AdsService adsService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @WithMockUser(username = "test@mail.ru", roles = "USER")
    public void shouldGetAllAds() throws Exception {
        // given
        Ads ads = new Ads();
        ads.setCount(1);
        ads.setResults(Collections.singletonList(new Ad()));

        // when
        when(adsService.getAllAds()).thenReturn(ads);

        // then
        mockMvc.perform(MockMvcRequestBuilders.get("/ads")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.count").value(1))
                .andExpect(jsonPath("$.results").isArray());
    }

    @Test
    @WithMockUser(username = "test@mail.ru", roles = "USER")
    public void shouldAddAd() throws Exception {
        // given
        CreateOrUpdateAd createOrUpdateAd = new CreateOrUpdateAd();
        createOrUpdateAd.setTitle("Бабушкин компот");
        createOrUpdateAd.setPrice(100_000);
        createOrUpdateAd.setDescription("Няма-няма");

        MockMultipartFile image = new MockMultipartFile(
                "image",
                "test.png",
                MediaType.IMAGE_PNG_VALUE,
                "test image".getBytes());
        MockMultipartFile properties = new MockMultipartFile(
                "properties",
                "",
                MediaType.APPLICATION_JSON_VALUE,
                objectMapper.writeValueAsBytes(createOrUpdateAd));

        Ad ad = new Ad();
        ad.setPk(1);
        ad.setTitle("Бабушкин компот");

        // when
        when(adsService.addAd(
                any(CreateOrUpdateAd.class),
                any(String.class),
                any(MultipartFile.class)))
                .thenReturn(ad);

        // then
        mockMvc.perform(MockMvcRequestBuilders.multipart("/ads")
                        .file(image)
                        .file(properties)
                        .contentType(MediaType.MULTIPART_FORM_DATA))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.pk").value(1))
                .andExpect(jsonPath("$.title").value("Бабушкин компот"));
    }

    @Test
    @WithMockUser(username = "test@mail.ru", roles = "USER")
    public void shouldGetAds() throws Exception {
        // given
        ExtendedAd extendedAd = new ExtendedAd();
        extendedAd.setPk(1);
        extendedAd.setTitle("Бабушкин компот");

        // when
        when(adsService.getAds(anyInt())).thenReturn(extendedAd);

        // then
        mockMvc.perform(MockMvcRequestBuilders.get("/ads/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.pk").value(1))
                .andExpect(jsonPath("$.title").value("Бабушкин компот"));
    }

    @Test
    @WithMockUser(username = "admin@mail.ru", roles = "ADMIN")
    public void shouldRemoveAd() throws Exception {
        // when & then
        mockMvc.perform(MockMvcRequestBuilders.delete("/ads/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "admin@mail.ru", roles = "ADMIN")
    public void shouldUpdateAds() throws Exception {
        // given
        CreateOrUpdateAd createOrUpdateAd = new CreateOrUpdateAd();
        createOrUpdateAd.setTitle("Новый бабушкин компот");
        createOrUpdateAd.setPrice(200);
        createOrUpdateAd.setDescription("Просто чума");

        Ad ad = new Ad();
        ad.setPk(1);
        ad.setTitle("Новый бабушкин компот");

        // when
        when(adsService.updateAds(any(CreateOrUpdateAd.class), anyInt())).thenReturn(ad);

        // then
        mockMvc.perform(MockMvcRequestBuilders.patch("/ads/1")
                        .content(objectMapper.writeValueAsString(createOrUpdateAd))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.pk").value(1))
                .andExpect(jsonPath("$.title").value("Новый бабушкин компот"));
    }

    @Test
    @WithMockUser(username = "test@mail.ru", roles = "USER")
    public void shouldGetAdsMe() throws Exception {
        // given
        Ads ads = new Ads();
        ads.setCount(1);
        ads.setResults(Collections.singletonList(new Ad()));

        // when
        when(adsService.getAdsMe(any(String.class))).thenReturn(ads);

        // then
        mockMvc.perform(MockMvcRequestBuilders.get("/ads/me")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.count").value(1))
                .andExpect(jsonPath("$.results").isArray());
    }

    @Test
    @WithMockUser(username = "admin@mail.ru", roles = "ADMIN")
    public void shouldUpdateImage() throws Exception {
        // given
        MockMultipartFile image = new MockMultipartFile(
                "image",
                "test.png",
                MediaType.IMAGE_PNG_VALUE,
                "test image".getBytes()
        ); // мокаем файл изображения

        // when
        doNothing().when(adsService).updateImage(eq(1), any(MultipartFile.class));

        // then
        mockMvc.perform(MockMvcRequestBuilders.multipart(HttpMethod.PATCH, "/ads/1/image")
                        .file(image)
                        .contentType(MediaType.MULTIPART_FORM_DATA))
                .andExpect(status().isOk());

        verify(adsService, times(1)).updateImage(eq(1), any(MultipartFile.class));
    }

    @Test
    @WithMockUser(username = "test@mail.ru", roles = "USER")
    public void shouldGetImages() throws Exception {
        // given
        byte[] imageBytes = "test image".getBytes();

        // when
        when(adsService.getImage(any(String.class))).thenReturn(imageBytes);

        // then
        mockMvc.perform(MockMvcRequestBuilders.get("/ads/image/test.png")
                        .contentType(MediaType.IMAGE_PNG_VALUE))
                .andExpect(status().isOk())
                .andExpect(content().bytes(imageBytes));
    }

}
