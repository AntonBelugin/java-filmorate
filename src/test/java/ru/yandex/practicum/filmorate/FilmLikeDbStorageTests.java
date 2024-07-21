package ru.yandex.practicum.filmorate;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ContextConfiguration;
import ru.yandex.practicum.filmorate.interfaces.FilmStorage;
import ru.yandex.practicum.filmorate.interfaces.UserStorage;
import ru.yandex.practicum.filmorate.mappers.UserRowMapper;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.*;

import java.time.LocalDate;

@JdbcTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@ContextConfiguration(classes = {FriendshipDbStorage.class, UserStorage.class,
        UserDbStorage.class, UserRowMapper.class, FilmStorage.class, FilmDbStorage.class,
        FilmLikeDbStorage.class})
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class FilmLikeDbStorageTests {
    private final FilmLikeDbStorage filmLikeDbStorage;
    private final UserStorage userDbStorage;
    private final FilmStorage filmDbStorage;
    private final JdbcTemplate jdbc;
    private User user = new User();
    private Film film = new Film();

    @BeforeEach
    void setUp() {
        user.setLogin("login");
        user.setName("name");
        user.setEmail("email@email.com");
        user.setBirthday(LocalDate.ofEpochDay(1975 - 5 - 12));
        userDbStorage.save(user);
        film.setName("name");
        film.setDescription("description");
        film.setDuration(60);
        film.setReleaseDate(LocalDate.ofEpochDay(1990 - 12 - 12));
        Mpa mpa = new Mpa();
        mpa.setId(1);
        film.setMpa(mpa);
        filmDbStorage.add(film);
    }

    @Test
    @Order(1)
    public void testAdd() {
        filmLikeDbStorage.add(1, 1);
        Assertions.assertEquals(jdbc.queryForObject("SELECT id_user FROM filmlikes " +
                "WHERE id_film = 1", Integer.class), 1);
    }

    @Test
    @Order(2)
    public void testDelete() {
        filmLikeDbStorage.add(2, 2);
        filmLikeDbStorage.delete(2,2);
       EmptyResultDataAccessException thrown = Assertions.assertThrows(EmptyResultDataAccessException.class, () -> {
            jdbc.queryForObject("SELECT id_user FROM filmlikes " +
                    "WHERE id_film = 2", Integer.class);
        });
        Assertions.assertNotNull(thrown.getMessage());
    }

}