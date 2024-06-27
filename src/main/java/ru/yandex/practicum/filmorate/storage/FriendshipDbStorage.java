package ru.yandex.practicum.filmorate.storage;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.mappers.UserResultSetExtractor;
import ru.yandex.practicum.filmorate.model.User;
import java.util.List;

@Component
@RequiredArgsConstructor
public class FriendshipDbStorage {
    private final JdbcTemplate jdbc;
    private static final String INSERT_QUERY = "INSERT INTO FRIENDS (USER_ID, FRIEND_ID)" +
            " VALUES (?, ?)";
    private static final String DELETE_QUERY = "DELETE FROM FRIENDS WHERE USER_ID = ? AND FRIEND_ID = ?";
    private static final String FIND_QUERY = "SELECT * FROM USERS WHERE ID IN " +
            "(SELECT FRIEND_ID FROM FRIENDS WHERE USER_ID = ?)";
    private static final String FIND_COMMON_QUERY = "SELECT * FROM USERS WHERE ID IN " +
            "(SELECT FRIEND_ID FROM FRIENDS WHERE USER_ID = ? AND " +
            "FRIEND_ID IN (SELECT FRIEND_ID FROM FRIENDS WHERE USER_ID = ?))";

    public void add(long userId, long friendId) {
        jdbc.update(
                INSERT_QUERY,
                userId,
                friendId
        );
    }

    public void delete(long userId, long friendId) {
        jdbc.update(
                DELETE_QUERY,
                userId,
                friendId
        );
    }

    public List<User> find(long userId) {
        try {
            return jdbc.query(FIND_QUERY, new UserResultSetExtractor(), userId);
        }
        catch (EmptyResultDataAccessException ignored) {
            return null;
        }
    }

    public List<User> findCommon(long userId, long otherId) {
        try {
            return jdbc.query(FIND_COMMON_QUERY, new UserResultSetExtractor(), userId, otherId);
        }
        catch (EmptyResultDataAccessException ignored) {
            return null;
        }
    }

}
