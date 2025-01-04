package ru.skypro.homework.service;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

/**
 * Интерфейс для работы с изображениями.
 * <p>
 * Этот интерфейс предоставляет методы для выполнения операций с изображениями,
 * таких как сохранение, получение и удаление изображений.
 * </p>
 */
public interface ImageService {

    /**
     * Сохраняет изображение на диск и в базу данных.
     *
     * @param image файл изображения.
     * @param name  имя директории для сохранения изображения.
     * @return путь к сохраненному изображению.
     * @throws IOException если возникает ошибка при обработке изображения.
     */
    String saveImage(MultipartFile image, String name) throws IOException;

    /**
     * Получает изображение по его имени.
     *
     * @param name имя изображения.
     * @return массив байтов, представляющий изображение.
     * @throws IOException если возникает ошибка при чтении изображения.
     */
    byte[] getImage(String name) throws IOException;

    /**
     * Удаляет изображение с диска и из базы данных.
     *
     * @param path путь к изображению.
     */
    void deleteImage(String path);

}
