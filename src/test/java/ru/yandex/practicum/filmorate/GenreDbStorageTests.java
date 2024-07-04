package ru.yandex.practicum.filmorate;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.test.context.ContextConfiguration;
import ru.yandex.practicum.filmorate.mappers.GenreRowMapper;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.*;
import java.util.Optional;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@JdbcTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@ContextConfiguration(classes = {GenreDbStorage.class, GenreRowMapper.class})
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class GenreDbStorageTests {
    private final GenreDbStorage genreStorage;

    @Test
    @Order(1)
    public void testFindAll() {
        Assertions.assertEquals(genreStorage.findAll().size(), 6);

    }

    @Test
    @Order(2)
    public void testFindById() {
        Optional<Genre> genreOptional = genreStorage.findById(1);
        assertThat(genreOptional)
                .isPresent()
                .hasValueSatisfying(user ->
                        assertThat(user).hasFieldOrPropertyWithValue("name", "Комедия")
                );
    }

}