package ru.yandex.practicum.filmorate.storage;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.*;

@Component
@RequiredArgsConstructor
public class UserDbStorage implements UserStorage {
    private Map<Long, User> users = new HashMap<>();
    private HashMap<Long, Set<Long>> userFriendsIds = new HashMap<>();
    private final JdbcTemplate jdbc;
    private final RowMapper<User> mapper;
    private static final String FIND_BY_ID_QUERY = "SELECT * FROM users WHERE id = ?";
    private static final String INSERT_QUERY = "INSERT INTO users (login, name, email, birthday)" +
            " VALUES (?, ?, ?, ?)";
    private static final String UPDATE_QUERY = "UPDATE users SET login=?, name=?, email=?, birthday=? WHERE id=?";
    //private static final String UPDATE_QUERY = "UPDATE films SET name=?, description=?, releasedate=?, duration=?, mpa=? WHERE id = ?";

    @Override
    public void save(User user) {
       jdbc.update(
                INSERT_QUERY,
                user.getLogin(),
                user.getName(),
                user.getEmail(),
                user.getBirthday()
        );
    }


    /* @Override
    public void save(User user) {
        users.put(user.getId(), user);
    }*/

    @Override
    public void delete(User user) {
        users.remove(user.getId());
    }

    @Override
    public User update(User user) {
        System.out.println();
        System.out.println(user);
        System.out.println("update");
        jdbc.update(
                UPDATE_QUERY,
                user.getLogin(),
                user.getName(),
                user.getEmail(),
                user.getBirthday(),
                user.getId()
        );
        System.out.println();
        System.out.println("update ok");
        return user;
    }

    @Override
    public Collection<User> findAll() {
        return users.values();
    }

    @Override
    public void addFriends(long userId, long friendId) {
        Set<Long> uFriends = userFriendsIds.computeIfAbsent(userId, id -> new HashSet<>());
        uFriends.add(friendId);

        Set<Long> fFriends = userFriendsIds.computeIfAbsent(friendId, id -> new HashSet<>());
        fFriends.add(userId);

        userFriendsIds.put(userId, uFriends);
        userFriendsIds.put(friendId, fFriends);
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
        System.out.println("find check");
        try {
            User user = jdbc.queryForObject(FIND_BY_ID_QUERY, mapper, (int) userId);
            System.out.println();
            System.out.println(user);
            System.out.println();
            System.out.println("find ok");
            return Optional.ofNullable(user);
        } catch (EmptyResultDataAccessException ignored) {
            System.out.println("catch");
            return Optional.empty();
          //  System.out.println("catch");
          //  throw new NotFoundException("Пользователя с id " + userId + " не существует");
           // return Optional.empty();
          //  throw new NotFoundException("Пользователя с id " + userId + " не существует");
            }
       /* if (jdbc.queryForObject(FIND_BY_ID_QUERY, Integer.class, userId) == 0) {
            throw new NotFoundException("Пользователя с id " + userId + " не существует");
        }*/
    }

    public Map<Long, User> getUsers() {
        return users;
    }

    public HashMap<Long, Set<Long>> getUserFriendsIds() {
        return userFriendsIds;
    }

}
