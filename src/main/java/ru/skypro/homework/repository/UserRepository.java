package ru.skypro.homework.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.skypro.homework.entity.UserEntity;

import java.util.Optional;

/**
 * Репозиторий для работы с сущностями пользователей (UserEntity).
 * <p>
 * Этот интерфейс предоставляет методы для выполнения операций с базой данных,
 * таких как поиск пользователя по email.
 * </p>
 *
 * @see Repository
 * @see JpaRepository
 */
@Repository
public interface UserRepository extends JpaRepository<UserEntity, Integer> {

    /**
     * Находит пользователя по email.
     *
     * @param email email пользователя.
     * @return Optional, содержащий найденного пользователя, если он существует.
     */
    Optional<UserEntity> findByEmail(String email);

}
