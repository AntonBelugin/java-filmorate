package ru.yandex.practicum.filmorate;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ContextConfiguration;
import ru.yandex.practicum.filmorate.interfaces.FilmStorage;
import ru.yandex.practicum.filmorate.interfaces.UserStorage;
import ru.yandex.practicum.filmorate.mappers.FilmResultSetExtractor;
import ru.yandex.practicum.filmorate.mappers.UserRowMapper;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.*;

import java.time.LocalDate;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;


@JdbcTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@ContextConfiguration(classes = {UserStorage.class, UserDbStorage.class, UserRowMapper.class,
        FilmStorage.class, FilmDbStorage.class, FilmResultSetExtractor.class, FilmLikeDbStorage.class})
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class FilmDbStorageTests {
    private final UserDbStorage userStorage;
    private final FilmDbStorage filmDbStorage;
    private final FilmLikeDbStorage filmLikeDbStorage;
    private final JdbcTemplate jdbc;
    private Film film = new Film();
    private User user = new User();

    @BeforeEach
    void setUp() {
        user.setLogin("login");
        user.setName("name");
        user.setEmail("email@email.com");
        user.setBirthday(LocalDate.ofEpochDay(1975-5-12));
        film.setName("name");
        film.setDescription("description");
        film.setDuration(60);
        film.setReleaseDate(LocalDate.ofEpochDay(1990-12-12));
        Mpa mpa = new Mpa();
        mpa.setId(1);
        film.setMpa(mpa);
    }

    @Test
    @Order(1)
    public void testAdd() {
        filmDbStorage.add(film);
        Assertions.assertEquals(jdbc.queryForObject("SELECT name FROM films " +
                "WHERE id = 1", String.class), "name");
    }

    @Test
    @Order(2)
    public void testFindUserById() {
        filmDbStorage.add(film);
        Optional<Film> filmOptional = filmDbStorage.findById(2);
        assertThat(filmOptional)
                .isPresent()
                .hasValueSatisfying(film ->
                        assertThat(film).hasFieldOrPropertyWithValue("id", 2L)
                );
    }

    @Test
    @Order(3)
    public void testUpdate() {
        film = filmDbStorage.add(film);
        film.setName("nameUpdate");
        filmDbStorage.update(film);
        Optional<Film> filmOptional = filmDbStorage.findById(film.getId());
        assertThat(filmOptional)
                .isPresent()
                .hasValueSatisfying(film ->
                        assertThat(film).hasFieldOrPropertyWithValue("name", "nameUpdate")
                );
    }

    @Test
    @Order(4)
    public void testGetAll() {
        filmDbStorage.add(film);
        filmDbStorage.add(film);
        filmDbStorage.add(film);
        Assertions.assertEquals(3, filmDbStorage.getAll().size());
    }

    @Test
    @Order(5)
    public void testMostLike() {
        userStorage.save(user);
        userStorage.save(user);
        filmDbStorage.add(film);
        filmDbStorage.add(film);
        filmDbStorage.add(film);
        System.out.println(filmDbStorage.getAll());
        filmLikeDbStorage.add(7, 1);
        filmLikeDbStorage.add(7, 2);
        filmLikeDbStorage.add(8, 1);
        Assertions.assertEquals(2, filmDbStorage.mostLike(10).size());
        Assertions.assertEquals(7, filmDbStorage.mostLike(10)
                .stream()
                .findFirst().get().getId());
    }

}