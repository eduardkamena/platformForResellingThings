package ru.skypro.homework.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import ru.skypro.homework.dto.Ad;
import ru.skypro.homework.dto.CreateOrUpdateAd;
import ru.skypro.homework.dto.ExtendedAd;
import ru.skypro.homework.entity.AdEntity;

import java.util.List;

/**
 * Интерфейс для маппинга между сущностями и DTO, связанными с объявлениями.
 * <p>
 * Этот интерфейс использует MapStruct для преобразования объектов между сущностями (AdEntity)
 * и DTO (Ad, ExtendedAd, CreateOrUpdateAd).
 * </p>
 *
 * @see Mapper
 * @see Mapping
 * @see MappingTarget
 */
@Mapper(componentModel = "spring")
public interface AdsMapper {

    /**
     * Преобразует объект CreateOrUpdateAd в сущность AdEntity.
     *
     * @param createOrUpdateAd DTO для создания или обновления объявления.
     * @return сущность AdEntity.
     */
    AdEntity toAdEntityFromCreateOrUpdateAdDTO(CreateOrUpdateAd createOrUpdateAd);

    /**
     * Преобразует сущность AdEntity в DTO Ad.
     *
     * @param adEntity сущность объявления.
     * @return DTO Ad.
     */
    @Mapping(target = "author", source = "author.id")
    @Mapping(target = "pk", source = "id")
    Ad toAdDTOFromAdEntity(AdEntity adEntity);

    /**
     * Преобразует список сущностей AdEntity в список DTO Ad.
     *
     * @param adEntityList список сущностей объявлений.
     * @return список DTO Ad.
     */
    List<Ad> toListAdDTOFromListAdEntity(List<AdEntity> adEntityList);

    /**
     * Преобразует сущность AdEntity в DTO ExtendedAd.
     *
     * @param adEntity сущность объявления.
     * @return DTO ExtendedAd.
     */
    @Mapping(target = "pk", source = "id")
    @Mapping(target = "authorFirstName", source = "author.firstName")
    @Mapping(target = "authorLastName", source = "author.lastName")
    @Mapping(target = "email", source = "author.email")
    @Mapping(target = "phone", source = "author.phone")
    ExtendedAd toExtendedAdDTOFromAdEntity(AdEntity adEntity);

    /**
     * Обновляет сущность AdEntity на основе данных из DTO CreateOrUpdateAd.
     *
     * @param createOrUpdateAd DTO для создания или обновления объявления.
     * @param adEntity         сущность объявления, которая будет обновлена.
     */
    void toCreateOrUpdateAdDTOFromAdEntity(CreateOrUpdateAd createOrUpdateAd,
                                           @MappingTarget AdEntity adEntity);

}
