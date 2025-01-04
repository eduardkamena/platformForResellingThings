<h1>README.md</h1>

<h2>Описание проекта</h2>
<p>
Проект представляет собой платформу для перепродажи вещей, 
взаимодействующую с фронтенд-частью сайта. Основная цель проекта — 
предоставить пользователям возможность размещать объявления о продаже товаров, 
оставлять комментарии к объявлениям, а также управлять своими данными и объявлениями. 
Проект включает в себя бэкенд-часть, которая обеспечивает авторизацию, аутентификацию, 
управление ролями пользователей, а также CRUD-операции для объявлений и комментариев.
</p>

<h2>Используемые технологии и библиотеки</h2>

<h3>Основные технологии:</h3>
<ul>
    <li><strong>Java 11</strong> — язык программирования, на котором написан бэкенд.</li>
    <li><strong>Spring Boot 2.7.10</strong> — фреймворк для создания приложений на Java.</li>
    <li><strong>Spring Data JPA</strong> — для работы с базой данных через ORM.</li>
    <li><strong>Spring Security</strong> — для обеспечения безопасности и управления доступом.</li>
    <li><strong>Liquibase</strong> — для управления миграциями базы данных.</li>
    <li><strong>PostgreSQL</strong> — основная база данных для хранения данных.</li>
    <li><strong>H2 Database</strong> — используется для тестирования.</li>
</ul>

<h3>Дополнительные библиотеки:</h3>
<ul>
    <li><strong>MapStruct</strong> — для маппинга объектов между слоями приложения.</li>
    <li><strong>Lombok</strong> — для автоматической генерации boilerplate-кода 
        (геттеры, сеттеры, конструкторы и т.д.).</li>
    <li><strong>Swagger</strong> — для документирования API.</li>
    <li><strong>Spring Validation</strong> — для валидации данных.</li>
</ul>

<h3>Фронтенд:</h3>
<ul>
    <li><strong>Docker</strong> — для запуска фронтенд-части проекта.</li>
    <li><strong>React</strong> — фронтенд-часть, запускаемая через Docker-контейнер.</li>
</ul>

<h2>Запуск проекта</h2>

<h3>Запуск фронтенда</h3>
<p>Чтобы запустить фронтенд с помощью Docker, выполните следующую команду 
в командной строке (или терминале):
</p>
<pre><code>docker run -p 3000:3000 --rm ghcr.io/dmitry-bizin/front-react-avito:v1.21</code></pre>
<p>После выполнения команды фронтенд будет доступен по адресу: 
<a href="http://localhost:3000">http://localhost:3000</a>.
</p>

<h3>Запуск бэкенда</h3>
<ol>
    <li>Убедитесь, что у вас установлена Java 11 и PostgreSQL.</li>
    <li>Создайте базу данных <code>ads_db</code> в PostgreSQL.</li>
    <li>
Настройте подключение к базе данных в файле <code>application.properties</code>:
<pre><code>spring.datasource.url=jdbc:postgresql://localhost:5432/ads_db
spring.datasource.username=db_editor
spring.datasource.password=rooteditor</code>
</pre>
    </li>
    <li>Запустите проект с помощью Maven:
    <pre><code>mvn spring-boot:run</code></pre>
    </li>
</ol>

<h2>Функционал бэкенда</h2>

<h3>Основные возможности:</h3>
<ol>
    <li><strong>Авторизация и аутентификация пользователей</strong>:
        <ul>
            <li>Регистрация и вход в систему.</li>
            <li>Хранение паролей в зашифрованном виде.</li>
        </ul>
    </li>
    <li><strong>Распределение ролей</strong>:
        <ul>
            <li>Пользователь (USER) — может создавать, редактировать и удалять свои объявления и комментарии.</li>
            <li>Администратор (ADMIN) — может редактировать и удалять любые объявления и комментарии.</li>
        </ul>
    </li>
    <li><strong>CRUD-операции для объявлений</strong>:
        <ul>
            <li>Создание, редактирование, удаление и просмотр объявлений.</li>
            <li>Возможность загрузки изображений для объявлений.</li>
        </ul>
    </li>
    <li><strong>CRUD-операции для комментариев</strong>:
        <ul>
            <li>Пользователи могут оставлять комментарии под объявлениями.</li>
            <li>Редактирование и удаление комментариев.</li>
        </ul>
    </li>
    <li><strong>Управление изображениями</strong>:
        <ul>
            <li>Загрузка и отображение изображений для объявлений.</li>
            <li>Управление аватарками пользователей.</li>
        </ul>
    </li>
    <li><strong>Валидация данных</strong>:
        <ul>
            <li>Проверка корректности вводимых данных (например, email, пароль, длина текста).</li>
        </ul>
    </li>
</ol>

<h2>Структура проекта</h2>

<h3>Контроллеры:</h3>
<ul>
    <li><code>AdsController</code> — управление объявлениями.</li>
    <li><code>CommentsController</code> — управление комментариями.</li>
    <li><code>LoginController</code> — авторизация и аутентификация.</li>
    <li><code>RegisterController</code> — регистрация пользователей.</li>
    <li><code>UsersController</code> — управление пользователями.</li>
</ul>

<h3>Сервисы:</h3>
<ul>
    <li><code>AdsService</code> — логика работы с объявлениями.</li>
    <li><code>CommentsService</code> — логика работы с комментариями.</li>
    <li><code>ImageService</code> — управление изображениями.</li>
    <li><code>LoginService</code> — логика авторизации.</li>
    <li><code>RegisterService</code> — логика регистрации.</li>
    <li><code>UserService</code> — управление пользователями.</li>
</ul>

<h3>Репозитории:</h3>
<ul>
    <li><code>AdsRepository</code> — работа с данными объявлений.</li>
    <li><code>CommentsRepository</code> — работа с данными комментариев.</li>
    <li><code>ImageRepository</code> — работа с изображениями.</li>
    <li><code>UserRepository</code> — работа с данными пользователей.</li>
</ul>

<h3>Сущности:</h3>
<ul>
    <li>
        <code>AdEntity</code>, 
        <code>CommentEntity</code>, 
        <code>ImageEntity</code>, 
        <code>UserEntity</code> — модели данных.
    </li>
</ul>

<h3>DTO:</h3>
<ul>
    <li>
        <code>Ad</code>, 
        <code>Ads</code>, 
        <code>Comment</code>, 
        <code>Comments</code>, 
        <code>CreateOrUpdateAd</code>, 
        <code>CreateOrUpdateComment</code>, 
        <code>ExtendedAd</code>, 
        <code>Login</code>, 
        <code>NewPassword</code>, 
        <code>Register</code>, 
        <code>UpdateUser</code>, 
        <code>User</code> — объекты передачи данных.
    </li>
</ul>

<h2>Дополнительные сведения</h2>
<ul>
    <li><strong>Логирование</strong>: В проекте используется логирование с помощью Lombok 
        (<code>@Slf4j</code>). Уровень логирования для сервисов установлен на <code>DEBUG</code>.</li>
    <li><strong>Тестирование</strong>: Для тестирования используется H2 Database и Spring Boot Test.</li>
</ul>

<h2>Заключение</h2>
<p>Этот проект представляет собой полноценную платформу для перепродажи вещей 
с возможностью взаимодействия с фронтендом. Бэкенд-часть обеспечивает безопасность, 
управление данными и взаимодействие с базой данных, а фронтенд предоставляет 
удобный интерфейс для пользователей.
</p>

<h2>Проблемы и отзывы</h2>
<p>Если вы столкнулись с какой-то проблемой или у вас есть предложения, 
пожалуйста откройте тему на <a href="https://github.com/eduardkamena/skypro-diploma_project-ads_online/issues">GitHub Issues</a>
</p>

<h2>Над проектом работали:</h2>
<ul>
    <li>Эдуард Каменских — <a href="https://github.com/eduardkamena">eduardkamena</a></li>
    <li>Олег Симаков — <a href="https://github.com/gitWestender">gitWestender</a></li>
    <li>Александр Говорин — <a href="https://github.com/Zhizhna">Zhizhna</a></li>
</ul>