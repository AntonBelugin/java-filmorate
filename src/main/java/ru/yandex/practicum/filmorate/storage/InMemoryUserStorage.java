package ru.yandex.practicum.filmorate.storage;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.*;


@Component
public class InMemoryUserStorage implements UserStorage {
    private Map<Long, User> users = new HashMap<>();
    private HashMap<Long, Set<Long>> userFriendsIds = new HashMap<>();

    @Override
    public void save(User user) {
        users.put(user.getId(), user);
    }

    @Override
    public void delete(User user) {
        users.remove(user.getId());
    }

    @Override
    public void update(User upUser) {
        users.put(upUser.getId(), upUser);
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

    public Map<Long, User> getUsers() {
        return users;
    }

    public HashMap<Long, Set<Long>> getUserFriendsIds() {
        return userFriendsIds;
    }
}
