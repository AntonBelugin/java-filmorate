package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.User;
import java.util.*;

public interface UserStorage {

    User save(User user);

    User update(User user);

    Collection<User> findAll();

    Optional<User> findById(long userId);
}
