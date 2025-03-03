# Platform for reselling things

## Описание проекта

Проект представляет собой платформу для перепродажи вещей, взаимодействующую с фронтенд-частью сайта. Основная цель проекта — предоставить пользователям возможность размещать объявления о продаже товаров, оставлять комментарии к объявлениям, а также управлять своими данными и объявлениями. Проект включает в себя бэкенд-часть, которая обеспечивает авторизацию, аутентификацию, управление ролями пользователей, а также CRUD-операции для объявлений и комментариев.

## Используемые технологии и библиотеки

### Основные технологии:

- **Java 11** — язык программирования, на котором написан бэкенд.
- **Spring Boot 2.7.10** — фреймворк для создания приложений на Java.
- **Spring Data JPA** — для работы с базой данных через ORM.
- **Spring Security** — для обеспечения безопасности и управления доступом.
- **Liquibase** — для управления миграциями базы данных.
- **PostgreSQL** — основная база данных для хранения данных.
- **H2 Database** — используется для тестирования.

### Дополнительные библиотеки:

- **MapStruct** — для маппинга объектов между слоями приложения.
- **Lombok** — для автоматической генерации boilerplate-кода (геттеры, сеттеры, конструкторы и т.д.).
- **Swagger** — для документирования API.
- **Spring Validation** — для валидации данных.

### Фронтенд:

- **Docker** — для запуска фронтенд-части проекта.
- **React** — фронтенд-часть, запускаемая через Docker-контейнер.

## Запуск проекта

### Запуск фронтенда

Чтобы запустить фронтенд с помощью Docker, выполните следующую команду в командной строке (или терминале):

```bash
docker run -p 3000:3000 --rm ghcr.io/dmitry-bizin/front-react-avito:v1.21
```
После выполнения команды фронтенд будет доступен по адресу: http://localhost:3000.

### Запуск бэкенда

1. Убедитесь, что у вас установлена **Java 11** и **PostgreSQL**.
2. Создайте базу данных в **PostgreSQL**.
3. Настройте подключение к базе данных в файле `application.properties`:
    ```properties
    spring.datasource.url=jdbc:postgresql://localhost:PORT/DATABASE_NAME
    spring.datasource.username=USERNAME
    spring.datasource.password=PASSWORD
    ```
4. Запустите проект с помощью **Maven**:
    ```bash
    mvn spring-boot:run
    ```
   
## Функционал бэкенда

### Основные возможности:

1. **Авторизация и аутентификация пользователей:**
    - Регистрация и вход в систему.
    - Хранение паролей в зашифрованном виде.
2. **Распределение ролей:**
   - Пользователь (USER) — может создавать, редактировать и удалять свои объявления и комментарии.
   - Администратор (ADMIN) — может редактировать и удалять любые объявления и комментарии.
3. **CRUD-операции для объявлений:**
   - Создание, редактирование, удаление и просмотр объявлений.
   - Возможность загрузки изображений для объявлений.
4. **CRUD-операции для комментариев:**
   - Пользователи могут оставлять комментарии под объявлениями.
   - Редактирование и удаление комментариев.
5. **Управление изображениями:**
   - Загрузка и отображение изображений для объявлений.
   - Управление аватарками пользователей.
6. **Валидация данных:**
   - Проверка корректности вводимых данных (например, email, пароль, длина текста).

## Структура проекта

### Контроллеры:

- `AdsController` — управление объявлениями.
- `CommentsController` — управление комментариями.
- `LoginController` — авторизация и аутентификация.
- `RegisterController` — регистрация пользователей.
- `UsersController` — управление пользователями.

### Сервисы:

- `AdsService` — логика работы с объявлениями.
- `CommentsService` — логика работы с комментариями.
- `ImageService` — управление изображениями.
- `LoginService` — логика авторизации.
- `RegisterService` — логика регистрации.
- `UserService` — управление пользователями.

### Репозитории:

- `AdsRepository` — работа с данными объявлений.
- `CommentsRepository` — работа с данными комментариев.
- `ImageRepository` — работа с изображениями.
- `UserRepository` — работа с данными пользователей.

### Сущности (Entity):

- `AdEntity`, `CommentEntity`, `ImageEntity`, `UserEntity` — модели данных.

### Объекты (DTO):

- `Ad`, `Ads`, `Comment`, `Comments`, `CreateOrUpdateAd`, `CreateOrUpdateComment`, `ExtendedAd`, `Login`, `NewPassword`, `Register`, `UpdateUser`, `User` — объекты передачи данных.

## Дополнительные сведения

- **Логирование:** В проекте используется логирование с помощью Lombok (`@Slf4j`). Уровень логирования для сервисов установлен на `DEBUG`.
- **Тестирование:** Для тестирования используется H2 Database и Spring Boot Test.

### Тестирование
В проекте использовались следующие виды тестирования:
- **Unit-тестирование** — для тестирования отдельных методов и классов.
- **Интеграционное тестирование** — для тестирования взаимодействия между компонентами.
- **Тестирование безопасности** — для проверки работы механизмов аутентификации и авторизации.
- **Тестирование фильтров** — для проверки корректности работы фильтров.
- **Тестирование маппинга** — для проверки преобразования объектов.
- **Тестирование исключений** — для проверки обработки ошибок.
- **Тестирование с использованием моков** — для изоляции тестируемых компонентов от зависимостей.

## Заключение

Этот проект представляет собой полноценную платформу для перепродажи вещей с возможностью взаимодействия с фронтендом. Бэкенд-часть обеспечивает безопасность, управление данными и взаимодействие с базой данных, а фронтенд предоставляет удобный интерфейс для пользователей.

## Проблемы и отзывы

Если вы столкнулись с какой-то проблемой или у вас есть предложения, пожалуйста, напишите в [GitHub Issues](https://github.com/eduardkamena/platformForResellingThings/issues).

## Над проектом работали:

- **Эдуард Каменских** — [eduardkamena](https://github.com/eduardkamena)
- **Олег Симаков** — [gitWestender](https://github.com/gitWestender)
- **Александр Говорин** — [Zhizhna](https://github.com/Zhizhna)