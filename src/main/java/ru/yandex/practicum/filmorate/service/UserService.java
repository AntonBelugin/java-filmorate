package ru.yandex.practicum.filmorate.service;

import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
public class UserService {
    private Set<Long> idFriends = new HashSet<>();

    void addFriends() {};

    void removeFriends() {};

    void findFriends() {};

}
