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

    public UserDto update(UpdateUserRequest request) {
        User updatedUser = userStorage.findById(request.getId());
        User updUser = UserMapper.updateUserFields(updatedUser, request);

         //       .map(user -> UserMapper.updateUserFields(user, request))
         //       .orElseThrow(() -> new NotFoundException("Пользователя с id " + request.getId() + " не существует"));

        System.out.println();
        System.out.println(updUser);
        System.out.println();
        System.out.println("Service");
        //userStorage.findById(upUser.getId());
        System.out.println();
        System.out.println(789);
        userStorage.update(updUser);
        return UserMapper.mapToUserDto(updUser);
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
