package ru.yandex.practicum.filmorate;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.test.context.ContextConfiguration;
import ru.yandex.practicum.filmorate.mappers.MpaRowMapper;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.*;
import java.util.Optional;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@JdbcTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@ContextConfiguration(classes = {MpaDbStorage.class, MpaRowMapper.class})
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class MpaDbStorageTests {
    private final MpaDbStorage mpaStorage;

    @Test
    @Order(1)
    public void testFindAll() {
        Assertions.assertEquals(mpaStorage.findAll().size(), 5);
    }

    @Test
    @Order(2)
    public void testFindById() {
        Optional<Mpa> mpaOptional = mpaStorage.findById(1);
        assertThat(mpaOptional)
                .isPresent()
                .hasValueSatisfying(user ->
                        assertThat(user).hasFieldOrPropertyWithValue("name", "G")
                );
    }

}
