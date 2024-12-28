package ru.skypro.homework.mapper;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.dto.announce.AdDTO;
import ru.skypro.homework.dto.announce.AdsDTO;
import ru.skypro.homework.dto.announce.CreateOrUpdateAdDTO;
import ru.skypro.homework.dto.announce.ExtendedAdDTO;
import ru.skypro.homework.entity.Ad;
import ru.skypro.homework.entity.Image;
import ru.skypro.homework.entity.User;
import ru.skypro.homework.repository.UserRepository;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class AdMapper {

    public AdDTO toAdDto(Ad ad) {
        AdDTO adDto = new AdDTO();

        adDto.setPk(ad.getPk());
        adDto.setAuthor(ad.getAuthor().getId());
        adDto.setImage("/ads/" + ad.getPk() + "/image");
        adDto.setPrice((ad.getPrice()));
        adDto.setTitle(ad.getTitle());

        return adDto;
    }

    public AdsDTO toAdsDto(List<Ad> ads) {
        AdsDTO adsDto = new AdsDTO();
        List<AdDTO> adDtoList = ads.stream()
                .map(this::toAdDto)
                .collect(Collectors.toList());

        adsDto.setCount(adDtoList.size());
        adsDto.setResult(adDtoList);

        return adsDto;
    }

    public Ad toEntity (CreateOrUpdateAdDTO createOrUpdateAdDto) {
        Ad ad = new Ad();

        ad.setTitle(createOrUpdateAdDto.getTitle());
        ad.setDescription(createOrUpdateAdDto.getDescription());
        ad.setPrice(createOrUpdateAdDto.getPrice());

        return ad;

    }

    public ExtendedAdDTO toExtendedAdDto(Ad ad) {
        ExtendedAdDTO extendedAdDto = new ExtendedAdDTO();

        extendedAdDto.setPk(ad.getPk());
        extendedAdDto.setAuthorFirstName(ad.getAuthor().getFirstName());
        extendedAdDto.setAuthorLastName(ad.getAuthor().getLastName());
        extendedAdDto.setDescription(ad.getDescription());
        extendedAdDto.setEmail(ad.getAuthor().getUsername());
        extendedAdDto.setImage("/ads/" + ad.getPk() + "/image");
        extendedAdDto.setPhone(ad.getAuthor().getPhone());
        extendedAdDto.setPrice(ad.getPrice());
        extendedAdDto.setTitle(ad.getTitle());

        return extendedAdDto;
    }

}
