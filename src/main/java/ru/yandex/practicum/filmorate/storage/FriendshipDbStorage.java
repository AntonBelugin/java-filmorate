package ru.yandex.practicum.filmorate.storage;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.User;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class FriendshipDbStorage {
    private final JdbcTemplate jdbc;
    private static final String INSERT_QUERY = "INSERT INTO friends (user_id, friend_id)" +
            " VALUES (?, ?)";
    private static final String DELETE_QUERY = "DELETE FROM friends WHERE user_id = ? AND friend_id = ?";
    private static final String FIND_QUERY = "SELECT * FROM users WHERE id IN " +
            "(SELECT friend_id FROM friends WHERE user_id = ?)";
    private static final String FIND_COMMON_QUERY = "SELECT * FROM users WHERE id IN " +
            "(SELECT friend_id FROM friends WHERE user_id = ? AND " +
            "friend_id IN (SELECT friend_id FROM friends WHERE user_id = ?))";

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
            return jdbc.query(FIND_QUERY, new FriendshipDbStorage.UserResultSetExtractor(), userId);
        }
        catch (EmptyResultDataAccessException ignored) {
            return null;
        }

       /* List<Long> friends = jdbc.queryForList(
                FIND_QUERY, Long.class,
                userId
        );
        if (friends.isEmpty()) {
            return null;
        }
        return friends;*/
    }

    public List<User> findCommon(long userId, long otherId) {
        try {
            return jdbc.query(FIND_COMMON_QUERY, new FriendshipDbStorage.UserResultSetExtractor(), userId, otherId);
        }
        catch (EmptyResultDataAccessException ignored) {
            return null;
        }
    }

    public class UserResultSetExtractor implements ResultSetExtractor<List<User>> {
        @Override
        public List<User> extractData(ResultSet rs) throws SQLException {
            LinkedList<User> userList = new LinkedList<>();
            if (!rs.isBeforeFirst()) {
                return userList;
            }
            while (rs.next()) {
                User user = new User();
                user.setId(rs.getLong("id"));
                user.setName(rs.getString("name"));
                user.setEmail(rs.getString("email"));
                user.setLogin(rs.getString("login"));
                user.setBirthday(rs.getDate("birthday").toLocalDate());
                userList.add(user);
            }
            return userList;
        }
    }
}
