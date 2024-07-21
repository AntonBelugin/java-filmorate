/*package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.controller.FilmController;
import ru.yandex.practicum.filmorate.controller.UserController;
import ru.yandex.practicum.filmorate.controller.ValidateService;
import ru.yandex.practicum.filmorate.controller.ValidateServiceImp;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.service.UserService;
import ru.yandex.practicum.filmorate.interfaces.FilmStorage;
import ru.yandex.practicum.filmorate.storage.InMemoryFilmStorage;
import ru.yandex.practicum.filmorate.storage.UserDbStorage;
import ru.yandex.practicum.filmorate.interfaces.UserStorage;
import java.time.LocalDate;

public class ValidateServiceTest {
    private ValidateService validateService;
    private FilmController filmController;
    private UserController userController;
    private FilmStorage filmStorage;
    private FilmService filmService;
    private UserStorage userStorage;
    private UserService userService;
    private User user;
    private Film film;
    private String failDescription = "12345678901234567890" +
            "12345678901234567890123456789012345678901234567890123456789012345678901234567890" +
            "12345678901234567890123456789012345678901234567890123456789012345678901234567890" +
            "123456789012345678901";
    private String failDateBirthday = "2285-12-12";
    private String failReleaseDate = "1785-12-12";

    @BeforeEach
    void beforeEach() {
        validateService = new ValidateServiceImp();
        filmStorage = new InMemoryFilmStorage();
        userStorage = new UserDbStorage();
        filmService = new FilmService(filmStorage, userStorage);
        userService = new UserService(userStorage);
        filmController = new FilmController(filmService, validateService);
       // userController = new UserController(userService, validateService);
    }

    void setUser() {
        user = new User();
        user.setEmail("email@email.ru");
        user.setName("name");
        user.setLogin("login");
        user.setBirthday(LocalDate.parse("1985-12-12"));
    }

    void setFilm() {
        film = new Film();
        film.setName("name");
        film.setDescription("description");
        film.setReleaseDate(LocalDate.parse("1985-12-12"));
        film.setDuration(600);
    }

    @Test
    void testCreateUser() {
        setUser();
        userService.create(user);
        Assertions.assertEquals(user, userStorage.getUsers().get(user.getId()));
    }

    @Test
    void testCreateUserFailEmail() {
        setUser();
        user.setEmail("email");
        ValidationException exception = Assertions.assertThrows(ValidationException.class, () -> {
            userController.create(user);
        });
        Assertions.assertEquals("Неправильный Имейл", exception.getMessage());

        user.setEmail(null);
        ValidationException exception2 = Assertions.assertThrows(ValidationException.class, () -> {
            userController.create(user);
        });
        Assertions.assertEquals("Неправильный Имейл", exception2.getMessage());
    }

    @Test
    void testCreateUserFailLogin() {
        setUser();
        user.setLogin(null);
        ValidationException exception = Assertions.assertThrows(ValidationException.class, () -> {
            userController.create(user);
        });
        Assertions.assertEquals("Неправильный Логин", exception.getMessage());

        user.setLogin(" ");
        ValidationException exception2 = Assertions.assertThrows(ValidationException.class, () -> {
            userController.create(user);
        });
        Assertions.assertEquals("Неправильный Логин", exception2.getMessage());
    }

    @Test
    void testCreateUserFailDateBirthday() {
        setUser();
        user.setBirthday(LocalDate.parse(failDateBirthday));
        ValidationException exception = Assertions.assertThrows(ValidationException.class, () -> {
            userController.create(user);
        });

        Assertions.assertEquals("Неправильно указана дата рождения", exception.getMessage());
    }

    @Test
    void testCreateUserFailName() {
        setUser();
        user.setName(null);
        userController.create(user);
        Assertions.assertEquals("login", user.getName());
    }

    @Test
    void testUpdateUser() {
        setUser();
        User newUser = userController.create(user);
        newUser.setLogin("login2");
        User newUser2 = userController.update(newUser);
        Assertions.assertEquals(newUser, newUser2);
    }

    @Test
    void testUpdateUserUserFailId() {
        setUser();
        userController.create(user);

        user.setId(2L);
        NotFoundException exception = Assertions.assertThrows(NotFoundException.class, () -> {
            userController.update(user);
        });
        Assertions.assertEquals("Пользователя с id 2 не существует", exception.getMessage());

        user.setId(null);
        ValidationException exception2 = Assertions.assertThrows(ValidationException.class, () -> {
            userController.update(user);
        });

        Assertions.assertEquals("Id должен быть указан", exception2.getMessage());
    }

    @Test
    void testCreateFilm() {
        setFilm();
        Film newFilm = filmController.create(film);
        Assertions.assertEquals(film, newFilm);
    }

    @Test
    void testCreateFilmFailName() {
        setFilm();
        film.setName(null);
        ValidationException exception = Assertions.assertThrows(ValidationException.class, () -> {
            filmController.create(film);
        });
        Assertions.assertEquals("Не указано название", exception.getMessage());

        film.setName(" ");
        ValidationException exception2 = Assertions.assertThrows(ValidationException.class, () -> {
            filmController.create(film);
        });
        Assertions.assertEquals("Не указано название", exception2.getMessage());
    }

    @Test
    void testCreateFilmFailDescription() {
        setFilm();
        film.setDescription(failDescription);
        ValidationException exception = Assertions.assertThrows(ValidationException.class, () -> {
            filmController.create(film);
        });
        Assertions.assertEquals("Описание должно быть не более 200 символов", exception.getMessage());
    }

    @Test
    void testCreateFilmFailReleaseDate() {
        setFilm();
        film.setReleaseDate(LocalDate.parse(failReleaseDate));
        film.setDuration(600);
        ValidationException exception = Assertions.assertThrows(ValidationException.class, () -> {
            filmController.create(film);
        });
        Assertions.assertEquals("Неправильно указана дата релиза", exception.getMessage());
    }

    @Test
    void testCreateFilmFailDuration() {
        setFilm();
        film.setDuration(0);
        ValidationException exception = Assertions.assertThrows(ValidationException.class, () -> {
            filmController.create(film);
        });
        Assertions.assertEquals("Продолжительность фильма меньше нуля", exception.getMessage());
    }

    @Test
    void testUpdateFilm() {
        setFilm();
        Film newFilm = filmController.create(film);
        newFilm.setDuration(700);
        Assertions.assertEquals(newFilm, filmController.update(newFilm));
    }

    @Test
    void testUpdateFilmFailId() {
        setFilm();
        filmController.create(film);

        film.setId(2L);
        NotFoundException exception = Assertions.assertThrows(NotFoundException.class, () -> {
            filmController.update(film);
        });
        Assertions.assertEquals("Фильма с id 2 не существует", exception.getMessage());

        film.setId(null);
        ValidationException exception2 = Assertions.assertThrows(ValidationException.class, () -> {
            filmController.update(film);
        });
        Assertions.assertEquals("Id должен быть указан", exception2.getMessage());
    }

}*/
