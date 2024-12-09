package ru.skypro.homework.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.skypro.homework.entity.AdImage;

@Repository
public interface AdImageRepository extends JpaRepository<AdImage, Integer> {
}
