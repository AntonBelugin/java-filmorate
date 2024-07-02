package ru.yandex.practicum.filmorate;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.shadow.com.univocity.parsers.annotations.FixedWidth;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ContextConfiguration;
import ru.yandex.practicum.filmorate.mappers.UserRowMapper;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.*;

import java.time.LocalDate;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;


@JdbcTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@ContextConfiguration(classes = {UserStorage.class, UserDbStorage.class, UserRowMapper.class/*, UserResultSetExtractor.class,
        UserService.class, UserRowMapper.class, */})


/*@ContextConfiguration(classes = {UserStorage.class, UserDbStorage.class, UserResultSetExtractor.class,
        UserService.class, FilmStorage.class, FilmDbStorage.class, FilmResultSetExtractor.class,
        FilmService.class, UserRowMapper.class, UserService.class, FriendshipDbStorage.class,
MpaDbStorage.class})*/
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class FilmoRateApplicationTests {
    private final UserDbStorage userStorage;
  //  private final JdbcTemplate jdbc;
    private User user = new User();
 //   private static final String INSERT_QUERY = "INSERT INTO USERS (LOGIN, NAME, EMAIL, BIRTHDAY)" +
      //      " VALUES (?, ?, ?, ?)";
  //  private static final String DELETE_QUERY = "Delete FROM USERS";

    @BeforeEach
    void setUp() {
        user.setLogin("login");
        user.setName("name");
        user.setEmail("email@email.com");
        user.setBirthday(LocalDate.ofEpochDay(1975-5-12));
    }


    @Test
    @Order(1)
    public void testSave() {

        User newUser = userStorage.save(user);
        Assertions.assertEquals(userStorage.findAll().size(), newUser.getId());

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