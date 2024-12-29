package ru.skypro.homework.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.skypro.homework.entity.CommentEntity;

import java.util.List;
import java.util.Optional;

@Repository
public interface CommentRepository extends JpaRepository<CommentEntity, Integer> {

    List<CommentEntity> findAllByAdId(Integer id);

    void deleteByAdIdAndId(Integer adId, Integer id);

    Optional<CommentEntity> findCommentByIdAndAd_Id(Integer id, Integer adsId);

    void deleteAllByAd_Id(Integer id);

}
