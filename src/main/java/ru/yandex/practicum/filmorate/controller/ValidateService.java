package ru.yandex.practicum.filmorate.controller;

import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dto.UpdateUserRequest;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;

@Service
public interface ValidateService {
    void validateUser(User user);

    void validateUpdateUser(UpdateUserRequest request);

    void validateFilm(Film film);

    void validateUpdateFilm(Film film);
}
