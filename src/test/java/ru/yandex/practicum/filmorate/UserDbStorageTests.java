package ru.yandex.practicum.filmorate;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ContextConfiguration;
import ru.yandex.practicum.filmorate.interfaces.UserStorage;
import ru.yandex.practicum.filmorate.mappers.UserRowMapper;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.*;

import java.time.LocalDate;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;


@JdbcTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@ContextConfiguration(classes = {UserStorage.class, UserDbStorage.class, UserRowMapper.class})
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class UserDbStorageTests {
    private final UserDbStorage userStorage;
    private final JdbcTemplate jdbc;
    private User user = new User();

    @BeforeEach
    void setUp() {
        user.setLogin("login");
        user.setName("name");
        user.setEmail("email@email.com");
        user.setBirthday(LocalDate.ofEpochDay(1975 - 5 - 12));
    }

    @Test
    @Order(1)
    public void testSave() {
       userStorage.save(user);
        Assertions.assertEquals(jdbc.queryForObject("SELECT name FROM users " +
                "WHERE id = 1", String.class), "name");
    }

    @Test
    @Order(2)
    public void testFindUserById() {
        userStorage.save(user);
        Optional<User> userOptional = userStorage.findById(2);
        assertThat(userOptional)
                .isPresent()
                .hasValueSatisfying(user ->
                        assertThat(user).hasFieldOrPropertyWithValue("id", 2L)
                );
    }

    @Test
    @Order(3)
    public void testUpdate() {
        user = userStorage.save(user);
        user.setLogin("loginUpdate");
        userStorage.update(user);
        Optional<User> userOptional = userStorage.findById(user.getId());
        assertThat(userOptional)
                .isPresent()
                .hasValueSatisfying(user ->
                        assertThat(user).hasFieldOrPropertyWithValue("login", "loginUpdate")
                );
    }

    @Test
    @Order(4)
    public void testFindAll() {
        userStorage.save(user);
        userStorage.save(user);
        userStorage.save(user);
        Assertions.assertEquals(3, userStorage.findAll().size());
    }

}