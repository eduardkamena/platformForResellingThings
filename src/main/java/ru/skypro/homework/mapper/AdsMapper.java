package ru.skypro.homework.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import ru.skypro.homework.dto.announce.AdsDto;
import ru.skypro.homework.dto.announce.CreateAds;
import ru.skypro.homework.dto.announce.FullAds;
import ru.skypro.homework.entity.Ads;

import java.util.List;

@Mapper(componentModel = "spring")
public interface AdsMapper {

    Ads toAdsFromCreateAds(CreateAds createAds);

    @Mapping(target = "author", source = "user.id")
    @Mapping(target = "pk", source = "id")
    AdsDto toAdsDto(Ads ads);

    List<AdsDto> toDtos(List<Ads> adsList);

    @Mapping(target = "pk", source = "id")
    @Mapping(target = "authorFirstName",source = "user.firstName")
    @Mapping(target = "authorLastName",source = "user.lastName")
    @Mapping(target = "email",source = "user.email")
    @Mapping(target = "phone",source = "user.phone")
    FullAds toFullAds(Ads ads);

    void updateAds(CreateAds createAds, @MappingTarget Ads ads);
}
