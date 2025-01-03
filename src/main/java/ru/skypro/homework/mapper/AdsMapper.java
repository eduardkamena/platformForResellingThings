package ru.skypro.homework.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import ru.skypro.homework.dto.Ad;
import ru.skypro.homework.dto.CreateOrUpdateAd;
import ru.skypro.homework.dto.ExtendedAd;
import ru.skypro.homework.entity.AdEntity;

import java.util.List;

@Mapper(componentModel = "spring")
public interface AdsMapper {

    AdEntity toAdEntityFromCreateOrUpdateAdDTO(CreateOrUpdateAd createOrUpdateAd);

    @Mapping(target = "author", source = "author.id")
    @Mapping(target = "pk", source = "id")
    Ad toAdDTOFromAdEntity(AdEntity adEntity);

    List<Ad> toListAdDTOFromListAdEntity(List<AdEntity> adEntityList);

    @Mapping(target = "pk", source = "id")
    @Mapping(target = "authorFirstName", source = "author.firstName")
    @Mapping(target = "authorLastName", source = "author.lastName")
    @Mapping(target = "email", source = "author.email")
    @Mapping(target = "phone", source = "author.phone")
    ExtendedAd toExtendedAdDTOFromAdEntity(AdEntity adEntity);

    void toCreateOrUpdateAdDTOFromAdEntity(CreateOrUpdateAd createOrUpdateAd, @MappingTarget AdEntity adEntity);

}
