package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dto.UpdateUserRequest;
import ru.yandex.practicum.filmorate.dto.UserDto;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.mappers.UserMapper;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;
import java.util.Collection;


@Service
public class UserService {
    private UserStorage userStorage;

    @Autowired
    public UserService(UserStorage userStorage) {
        this.userStorage = userStorage;
    }

    public void create(User user) {
        userStorage.save(user);
    }

    public User update(User user) {
       if (userStorage.findById(user.getId()).isEmpty()) {
           System.out.println("не существует");
           throw new NotFoundException("Пользователя с id " + user.getId() + " не существует");
       }
       // User updUser = UserMapper.updateUserFields(updatedUser, user);

         //       .map(user -> UserMapper.updateUserFields(user, request))
         //       .orElseThrow(() -> new NotFoundException("Пользователя с id " + request.getId() + " не существует"));

        System.out.println("optional");
        //System.out.println(updUser);
        //User userNew = userStorage.findById(user.getId()).get();
       // System.out.println(userNew + "userNew");
        System.out.println();
        System.out.println("отправка на обновление");
        //userStorage.findById(upUser.getId());
        System.out.println();

        //userStorage.update(user);
        return userStorage.update(user);
    }

    public Collection<User> findAll() {
        return userStorage.findAll();
    }

    public void addFriends(long userId, long friendId) {
        userStorage.findById(userId);
        userStorage.findById(friendId);
        userStorage.addFriends(userId, friendId);
    }

    public void deleteFriends(long userId, long friendId) {
        userStorage.findById(userId);
        userStorage.findById(friendId);
        if (userStorage.getUserFriendsIds().containsKey(userId) &&
                userStorage.getUserFriendsIds().get(userId).contains(friendId)) {
            userStorage.deleteFriends(userId, friendId);
        }
    }

    public Collection<User> findFriends(long userId) {
        userStorage.findById(userId);
        return userStorage.findFriends(userId);
    }

    public Collection<User> findCommonFriends(long userId, long otherId) {
        userStorage.findById(userId);
        userStorage.findById(otherId);
        return userStorage.findCommonFriends(userId, otherId);
    }

}
