package ru.skypro.homework.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.skypro.homework.entity.AdEntity;
import ru.skypro.homework.entity.UserEntity;

import java.util.List;

/**
 * Репозиторий для работы с сущностями объявлений (AdEntity).
 * <p>
 * Этот интерфейс предоставляет методы для выполнения операций с базой данных,
 * таких как поиск объявлений по автору.
 * </p>
 *
 * @see Repository
 * @see JpaRepository
 */
@Repository
public interface AdsRepository extends JpaRepository<AdEntity, Integer> {

    /**
     * Находит все объявления, созданные указанным автором.
     *
     * @param author сущность пользователя, являющегося автором объявлений.
     * @return список объявлений, созданных указанным автором.
     */
    List<AdEntity> findByAuthor(UserEntity author);

}
