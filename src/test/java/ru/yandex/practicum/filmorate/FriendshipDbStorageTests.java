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

@JdbcTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@ContextConfiguration(classes = {FriendshipDbStorage.class, UserStorage.class,
        UserDbStorage.class, UserRowMapper.class})
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class FriendshipDbStorageTests {
    private final FriendshipDbStorage friendshipDbStorage;
    private final UserStorage userDbStorage;
    private final JdbcTemplate jdbc;
    private User user = new User();

  @BeforeEach
    void setUp() {
        user.setLogin("login");
        user.setName("name");
        user.setEmail("email@email.com");
        user.setBirthday(LocalDate.ofEpochDay(1975-5-12));
        userDbStorage.save(user);
        userDbStorage.save(user);
    }


    @Test
    @Order(1)
    public void testAdd() {
        friendshipDbStorage.add(1, 2);
        Assertions.assertEquals(jdbc.queryForObject("SELECT friend_id FROM friends " +
                "WHERE user_id = 1", Integer.class), 2);
    }

    @Test
    @Order(2)
    public void testFind() {
        friendshipDbStorage.add(3, 4);
        Assertions.assertEquals(friendshipDbStorage.find(3).getFirst().getId(), 4);
    }

    @Test
    @Order(3)
    public void testDelete() {
        friendshipDbStorage.add(5, 6);
        friendshipDbStorage.delete(5,6);
        Assertions.assertTrue(friendshipDbStorage.find(5).isEmpty());
    }

    @Test
    @Order(4)
    public void testFindCommon() {
        userDbStorage.save(user);
        friendshipDbStorage.add(7, 9);
        friendshipDbStorage.add(8, 9);
        Assertions.assertEquals(friendshipDbStorage.findCommon(7, 8).getFirst().getId(), 9);
    }

}