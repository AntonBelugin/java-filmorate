package ru.yandex.practicum.filmorate.storage;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exception.InternalServerException;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.model.User;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.*;

@Component
@RequiredArgsConstructor
public class UserDbStorage implements UserStorage {
    private Map<Long, User> users = new HashMap<>();
    private HashMap<Long, Set<Long>> userFriendsIds = new HashMap<>();
    private final JdbcTemplate jdbc;
    private final RowMapper<User> mapper;
    private static final String FIND_BY_ID_QUERY = "SELECT * FROM users WHERE id = ?";
    private static final String FIND_ALL_QUERY = "SELECT * FROM users";
    private static final String INSERT_QUERY = "INSERT INTO users (login, name, email, birthday)" +
            " VALUES (?, ?, ?, ?)";
    private static final String UPDATE_QUERY = "UPDATE users SET login=?, name=?, email=?, birthday=? WHERE id=?";
    //private static final String UPDATE_QUERY = "UPDATE films SET name=?, description=?, releasedate=?, duration=?, mpa=? WHERE id = ?";

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
    public void delete(User user) {
        users.remove(user.getId());
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
            return jdbc.query(FIND_ALL_QUERY, new UserDbStorage.UserResultSetExtractor());
        }
        catch (EmptyResultDataAccessException ignored) {
                return null;
            }
    }

    @Override
    public void addFriends(long userId, long friendId) {
       /* Set<Long> uFriends = userFriendsIds.computeIfAbsent(userId, id -> new HashSet<>());
        uFriends.add(friendId);

        Set<Long> fFriends = userFriendsIds.computeIfAbsent(friendId, id -> new HashSet<>());
        fFriends.add(userId);

        userFriendsIds.put(userId, uFriends);
        userFriendsIds.put(friendId, fFriends);*/
    }

    @Override
    public void deleteFriends(long userId, long friendId) {
        userFriendsIds.get(userId).remove(friendId);
        userFriendsIds.get(friendId).remove(userId);
    }

    @Override
    public Collection<User> findFriends(long userId) {
        List<User> friends = new ArrayList<>();
        if (userFriendsIds.containsKey(userId)) {
            for (Long id : userFriendsIds.get(userId)) {
                friends.add(users.get(id));
            }
        }
        return friends;
    }

    @Override
    public Collection<User> findCommonFriends(long userId, long otherId) {
        List<User> friends = new ArrayList<>();
        Set<Long> uFriends = userFriendsIds.get(userId);
        Set<Long> fFriends = userFriendsIds.get(otherId);
        for (Long id : uFriends) {
            if (fFriends.contains(id)) {
                friends.add(users.get(id));
            }
        }
        return friends;
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


    public Map<Long, User> getUsers() {
        return users;
    }

    public HashMap<Long, Set<Long>> getUserFriendsIds() {
        return userFriendsIds;
    }

}
