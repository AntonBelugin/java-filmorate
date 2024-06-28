package ru.yandex.practicum.filmorate.storage;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exception.InternalServerException;
import ru.yandex.practicum.filmorate.mappers.UserResultSetExtractor;
import ru.yandex.practicum.filmorate.model.User;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.*;

@Repository
@RequiredArgsConstructor
public class UserDbStorage implements UserStorage {
    private final JdbcTemplate jdbc;
    private final RowMapper<User> mapper;
    private static final String FIND_BY_ID_QUERY = "SELECT * FROM USERS WHERE ID = ?";
    private static final String FIND_ALL_QUERY = "SELECT * FROM USERS";
    private static final String INSERT_QUERY = "INSERT INTO USERS (LOGIN, NAME, EMAIL, BIRTHDAY)" +
            " VALUES (?, ?, ?, ?)";
    private static final String UPDATE_QUERY = "UPDATE USERS SET LOGIN=?, NAME=?, EMAIL=?, BIRTHDAY=? WHERE ID=?";

    @Override
    public User save(User user) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbc.update(connection -> {
            PreparedStatement ps = connection
                    .prepareStatement(INSERT_QUERY, Statement.RETURN_GENERATED_KEYS);
            ps.setObject(1, user.getLogin());
            ps.setObject(2, user.getName());
            ps.setObject(3, user.getEmail());
            ps.setObject(4, user.getBirthday());
            return ps;
        }, keyHolder);
        Long id = keyHolder.getKeyAs(Long.class);
        if (id != null) {
            user.setId(id);
        } else {
            throw new InternalServerException("Не удалось сохранить данные");
        }
        return user;
    }

    @Override
    public User update(User user) {
        jdbc.update(
                UPDATE_QUERY,
                user.getLogin(),
                user.getName(),
                user.getEmail(),
                user.getBirthday(),
                user.getId()
        );
        return user;
    }

    @Override
    public Collection<User> findAll() {
        try {
            return jdbc.query(FIND_ALL_QUERY, new UserResultSetExtractor());
        }
        catch (EmptyResultDataAccessException ignored) {
                return null;
            }
    }

    @Override
    public Optional<User> findById(long userId) {
        try {
            User user = jdbc.queryForObject(FIND_BY_ID_QUERY, mapper, (int) userId);
            return Optional.ofNullable(user);
        } catch (EmptyResultDataAccessException ignored) {
            return Optional.empty();
            }
    }
}