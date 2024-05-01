package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.controller.FilmController;
import ru.yandex.practicum.filmorate.controller.UserController;
import ru.yandex.practicum.filmorate.controller.ValidateService;
import ru.yandex.practicum.filmorate.controller.ValidateServiceImp;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;

class ValidateServiceTest {
    ValidateService validateService = new ValidateServiceImp();
    FilmController filmController = new FilmController(validateService);
    UserController userController = new UserController(validateService);
    User user = new User();
    Film film = new Film();

    @Test
    void testCreateUser() {
        user.setEmail("email@email.ru");
        user.setName("name");
        user.setLogin("login");
        user.setBirthday(LocalDate.parse("1985-12-12"));
        User newUser = userController.create(user);
        Assertions.assertEquals(user, newUser);
    }

    @Test
    void testCreateUserFailEmail() {
        user.setEmail("email");
        user.setName("name");
        user.setLogin("login");
        user.setBirthday(LocalDate.parse("1985-12-12"));
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
        user.setEmail("email@email.ru");
        user.setName("name");
        user.setLogin(null);
        user.setBirthday(LocalDate.parse("1985-12-12"));
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
    void testCreateUserFailDataBirthday() {
        user.setEmail("email@email.ru");
        user.setName("name");
        user.setLogin("login");
        user.setBirthday(LocalDate.parse("2285-12-12"));
        ValidationException exception = Assertions.assertThrows(ValidationException.class, () -> {
            userController.create(user);
        });

        Assertions.assertEquals("Неправильно указана дата рождения", exception.getMessage());
    }

    @Test
    void testCreateUserFailName() {
        user.setEmail("email@email.ru");
        user.setName(null);
        user.setLogin("login");
        user.setBirthday(LocalDate.parse("1985-12-12"));
        userController.create(user);
        Assertions.assertEquals("login", user.getName());
    }

    @Test
    void testUpdateUser() {
        user.setEmail("email@email.ru");
        user.setName("name");
        user.setLogin("login");
        user.setBirthday(LocalDate.parse("1985-12-12"));
        User newUser = userController.create(user);
        newUser.setLogin("login2");
        User newUser2 = userController.update(newUser);
        Assertions.assertEquals(newUser, newUser2);
    }

    @Test
    void testUpdateUserUserFailId() {
        user.setEmail("email@email.ru");
        user.setName("name");
        user.setLogin("login");
        user.setBirthday(LocalDate.parse("1985-12-12"));
        userController.create(user);

        user.setId(2L);
        ValidationException exception = Assertions.assertThrows(ValidationException.class, () -> {
            userController.update(user);
        });
        Assertions.assertEquals("Неверный Id", exception.getMessage());


        user.setId(null);
        ValidationException exception2 = Assertions.assertThrows(ValidationException.class, () -> {
            userController.update(user);
        });

        Assertions.assertEquals("Id должен быть указан", exception2.getMessage());
    }

    @Test
    void testCreateFilm() {
        film.setName("name");
        film.setDescription("description");
        film.setReleaseDate(LocalDate.parse("1985-12-12"));
        film.setDuration(600);
        Film newFilm = filmController.create(film);
        Assertions.assertEquals(film, newFilm);
    }

    @Test
    void testCreateFilmFailName() {
        film.setName(null);
        film.setDescription("description");
        film.setReleaseDate(LocalDate.parse("1985-12-12"));
        film.setDuration(600);
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
        film.setName("name");
        film.setDescription("12345678901234567890" +
                "12345678901234567890123456789012345678901234567890123456789012345678901234567890" +
                "12345678901234567890123456789012345678901234567890123456789012345678901234567890" +
                "123456789012345678901");
        film.setReleaseDate(LocalDate.parse("1985-12-12"));
        film.setDuration(600);
        ValidationException exception = Assertions.assertThrows(ValidationException.class, () -> {
            filmController.create(film);
        });
        Assertions.assertEquals("Описание должно быть не более 200 символов", exception.getMessage());
    }

    @Test
    void testCreateFilmFailReleaseDate() {
        film.setName("name");
        film.setDescription("description");
        film.setReleaseDate(LocalDate.parse("1785-12-12"));
        film.setDuration(600);
        ValidationException exception = Assertions.assertThrows(ValidationException.class, () -> {
            filmController.create(film);
        });
        Assertions.assertEquals("Неправильно указана дата релиза", exception.getMessage());
    }

    @Test
    void testCreateFilmFailDuration() {
        film.setName("name");
        film.setDescription("description");
        film.setReleaseDate(LocalDate.parse("1985-12-12"));
        film.setDuration(0);
        ValidationException exception = Assertions.assertThrows(ValidationException.class, () -> {
            filmController.create(film);
        });
        Assertions.assertEquals("Продолжительность фильма меньше нуля", exception.getMessage());
    }

    @Test
    void testUpdateFilm() {
        film.setName("name");
        film.setDescription("description");
        film.setReleaseDate(LocalDate.parse("1985-12-12"));
        film.setDuration(600);
        Film newFilm = filmController.create(film);
        newFilm.setDuration(700);
        Assertions.assertEquals(newFilm, filmController.update(newFilm));
    }

    @Test
    void testUpdateFilmFailId() {
        film.setName("name");
        film.setDescription("description");
        film.setReleaseDate(LocalDate.parse("1985-12-12"));
        film.setDuration(600);
        filmController.create(film);

        film.setId(2L);
        ValidationException exception = Assertions.assertThrows(ValidationException.class, () -> {
            filmController.update(film);
        });
        Assertions.assertEquals("Неверный Id", exception.getMessage());

        film.setId(null);
        ValidationException exception2 = Assertions.assertThrows(ValidationException.class, () -> {
            filmController.update(film);
        });
        Assertions.assertEquals("Id должен быть указан", exception2.getMessage());
    }

}
