package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;
import java.util.Map;

public interface UserStorage {

    void saveUser(User user);

    void deleteUser(User user);

    void updateUser(User user);

    Collection<User> findAllUsers();

    Map<Long, User> getUsers();
}
