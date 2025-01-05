package ru.skypro.homework.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.skypro.homework.entity.ImageEntity;

/**
 * Репозиторий для работы с сущностями изображений (ImageEntity).
 * <p>
 * Этот интерфейс предоставляет методы для выполнения операций с базой данных,
 * таких как удаление изображений по пути к файлу.
 * </p>
 *
 * @see Repository
 * @see JpaRepository
 */
@Repository
public interface ImageRepository extends JpaRepository<ImageEntity, Integer> {

    /**
     * Удаляет изображение по указанному пути к файлу.
     *
     * @param filePath путь к файлу изображения.
     */
    void deleteByFilePath(String filePath);

}
