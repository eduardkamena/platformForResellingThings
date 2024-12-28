package ru.skypro.homework.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.dto.user.NewPasswordDTO;
import ru.skypro.homework.dto.user.UpdateUserDTO;
import ru.skypro.homework.dto.user.UserDTO;
import ru.skypro.homework.entity.User;
import ru.skypro.homework.exception.PasswordIsNotMatchException;
import ru.skypro.homework.exception.UserNotFoundException;
import ru.skypro.homework.mapper.UserMapper;
import ru.skypro.homework.mapper.UserMapperInterface;
import ru.skypro.homework.repository.ImageRepository;
import ru.skypro.homework.repository.UserRepository;
import ru.skypro.homework.service.LoggingMethod;
import ru.skypro.homework.service.UserService;

import java.io.IOException;

/**
 * Сервис хранящий логику для управления данными пользователей.
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final ImageRepository imageRepository;
    private final UserMapper userMapper;
    private final ImageServiceImpl imageService;
    private final LoggingMethod loggingMethod;

    // Нужно пересмотреть (рефактор/соединить/удалить)
    private final UserMapperInterface userMapperInterface;
    // private final PasswordEncoder encoder ?
    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Value("${path.to.photos.folder}")
    private String photoDir;

    /**
     * Метод обновляет пароль текущего, авторизованного пользователя.
     * <p>Метод получает объект newPass, который содержит два поля со старым и новым паролями.
     * А так же объект {@link Authentication} из которого можно получить логин
     * авторизованного пользователя.</p>
     * Далее метод ищет пользователя с соответствующим логином в репозитории и сохраняет его
     * в переменную userEntity. Логин получаем из объекта {@link Authentication}.
     * <p>Далее, метод делает проверку на совпадение старого пароля, введенного пользователем,
     * и пароля сохраненного в БД. Если пароли совпали, от используя сеттер, в переменную, содержащую пользователя,
     * сохраняется новый пароль. Переменная (объект userEntity) с новым, измененным паролем
     * сохраняется в БД.</p>
     *
     * @param newPass        объект {@link NewPasswordDTO}, содержащий старый и новый пароли.
     * @param authentication содержит логин авторизованного пользователя.
     */
    @Override
    public void setPassword(NewPasswordDTO newPass, Authentication authentication) {

        log.info("Запущен метод сервиса {}", loggingMethod.getMethodName());
        //получаем в переменную старый пароль
        String oldPassword = newPass.getCurrentPassword();
        //получаем в переменную новый пароль и кодируем его
        String encodeNewPassword = passwordEncoder.encode(newPass.getNewPassword());
        //Находим в БД сущность авторизованного пользователя используя логин из authentication
        //проверка на null не нужна, тк тот факт, что пользователь авторизовался,
        //говорит о том, что он есть в БД
        User entity = userRepository.findUserByUsername(authentication.getName());
        //проверяем совпадают ли старый пароль, введенный пользователем, и пароль сохраненный в БД
        if (!passwordEncoder.matches(oldPassword, entity.getPassword())) {
            throw new PasswordIsNotMatchException("Пароли не совпадают");
        } else {
            //пароли совпадают, а значит устанавливаем новый пароль в соответствующее поле сущности
            entity.setPassword(encodeNewPassword);
        }
        //сохраняем сущность в БД
        userRepository.save(entity);
    }

    /**
     * Метод возвращает информацию о текущем, авторизованном пользователе.
     * Метод, используя объект {@link Authentication#getName()} как параметр userName,
     * находит в БД {@link UserRepository}, пользователя с соответствующими данными и возвращает его.
     *
     * @param username
     * @return объект userEntity
     */
    @Transactional
    @Override
    public User getUser(String username) {

        log.info("Запущен метод сервиса {}", loggingMethod.getMethodName());
        User user = userRepository.findUserByUsername(username);
        if (user == null) {
            throw new UserNotFoundException("Пользователя с таким логином в базе данных нет");
        }
        return user;
    }

    /**
     * Метод изменяет данные пользователя, а именно имя, фамилию и номер телефона.
     * <p>В начале метод получает из {@link Authentication} логин авторизованного пользователя
     * и записывает его в переменную.</p>
     * <p>По логину находит данные пользователя в БД и кладет их в сущность user.
     * Сущность user заполняется измененными данными из парамера updateUser.</p>
     * <p>В итоге измененный объект user сохраняется в БД, и он же возвращается из метода.</p>
     *
     * @param updateUser     объект содержащий поля с именем, фамилией и номером телефона.
     * @param authentication
     * @return объект user
     */
    @Transactional
    @Override
    public User updateUser(UpdateUserDTO updateUser, Authentication authentication) {

        log.info("Запущен метод сервиса {}", loggingMethod.getMethodName());
        //Получаем логин авторизованного пользователя из БД
        String userName = authentication.getName();
        //Находим данные авторизованного пользователя
        User user = userRepository.findUserByUsername(userName);
        //Меняем данные пользователя на данные из DTO updateUser
        user.setFirstName(updateUser.getFirstName());
        user.setLastName(updateUser.getLastName());
        user.setPhone(updateUser.getPhone());
        //сохраняем измененные данные в БД
        userRepository.save(user);
        return user;
    }

    @Transactional
    @Override
    public void updateUserImage(MultipartFile image, Authentication authentication) throws IOException {
        log.info("Запущен метод сервиса {}", loggingMethod.getMethodName());

        //достаем пользователя из БД
        User entity = userRepository.findUserByUsername(authentication.getName());

        //заполняем поля и возвращаем
        entity = (User) imageService.updateEntitiesPhoto(image, entity);
        log.info("userEntity создано - {}", entity);

        //сохранение сущности user в БД
        assert entity != null;
        userRepository.save(entity);
    }

    // Нужно пересмотреть (рефактор/соединить/удалить)
    public UserDTO findUserByUsername(String username) {
        User user = userRepository.findByUsername(username).orElseThrow(() -> new UserNotFoundException("User not found with username: " + username));
        return userMapperInterface.toUserDTO(user);
    }

    // Нужно пересмотреть (рефактор/соединить/удалить)
    public void changePassword(NewPasswordDTO newPasswordDTO, String username) {
        User user = userRepository.findByUsername(username).orElseThrow(() -> new UserNotFoundException("User not found with username: " + username));

        System.out.println(user.getPassword());
        if (passwordEncoder.matches(newPasswordDTO.getCurrentPassword(), user.getPassword())) {
            user.setPassword(passwordEncoder.encode(newPasswordDTO.getNewPassword()));
            userRepository.save(user);
        }
    }

}
