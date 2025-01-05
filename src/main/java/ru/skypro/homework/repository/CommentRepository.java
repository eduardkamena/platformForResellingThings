package ru.skypro.homework.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.skypro.homework.entity.CommentEntity;

import java.util.List;
import java.util.Optional;

/**
 * Репозиторий для работы с сущностями комментариев (CommentEntity).
 * <p>
 * Этот интерфейс предоставляет методы для выполнения операций с базой данных,
 * таких как поиск, удаление и получение комментариев, связанных с объявлениями.
 * </p>
 *
 * @see Repository
 * @see JpaRepository
 */
@Repository
public interface CommentRepository extends JpaRepository<CommentEntity, Integer> {

    /**
     * Находит все комментарии, связанные с указанным объявлением.
     *
     * @param id идентификатор объявления.
     * @return список комментариев, связанных с указанным объявлением.
     */
    List<CommentEntity> findAllByAdId(Integer id);

    /**
     * Удаляет комментарий по идентификатору объявления и идентификатору комментария.
     *
     * @param adId идентификатор объявления.
     * @param id   идентификатор комментария.
     */
    void deleteByAdIdAndId(Integer adId, Integer id);

    /**
     * Находит комментарий по его идентификатору и идентификатору связанного объявления.
     *
     * @param id    идентификатор комментария.
     * @param adsId идентификатор объявления.
     * @return Optional, содержащий найденный комментарий, если он существует.
     */
    Optional<CommentEntity> findCommentByIdAndAd_Id(Integer id, Integer adsId);

    /**
     * Удаляет все комментарии, связанные с указанным объявлением.
     *
     * @param id идентификатор объявления.
     */
    void deleteAllByAd_Id(Integer id);

}
