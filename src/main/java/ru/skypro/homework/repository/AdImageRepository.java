package ru.skypro.homework.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.skypro.homework.entity.AdImage;

public interface AdImageRepository extends JpaRepository<AdImage, Long> {
}
