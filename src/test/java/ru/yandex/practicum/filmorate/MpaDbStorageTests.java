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
@ContextConfiguration(classes = {MpaDbStorage.class, MpaRowMapper.class/*, UserResultSetExtractor.class,
        UserService.class, UserRowMapper.class, */})


/*@ContextConfiguration(classes = {UserStorage.class, UserDbStorage.class, UserResultSetExtractor.class,
        UserService.class, FilmStorage.class, FilmDbStorage.class, FilmResultSetExtractor.class,
        FilmService.class, UserRowMapper.class, UserService.class, FriendshipDbStorage.class,
MpaDbStorage.class})*/
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class MpaDbStorageTests {
    private final MpaDbStorage mpaStorage;
    //  private final JdbcTemplate jdbc;
   // private User user = new User();
    //   private static final String INSERT_QUERY = "INSERT INTO USERS (LOGIN, NAME, EMAIL, BIRTHDAY)" +
    //      " VALUES (?, ?, ?, ?)";
    //  private static final String DELETE_QUERY = "Delete FROM USERS";

  /*  @BeforeEach
    void setUp() {
        user.setLogin("login");
        user.setName("name");
        user.setEmail("email@email.com");
        user.setBirthday(LocalDate.ofEpochDay(1975-5-12));
    }*/


    @Test
    @Order(1)
    public void testFindAll() {

      //  System.out.println(MpaStorage.findAll());
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
