package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;
import java.util.Collection;

@Service
public class FilmService {
    private FilmStorage filmStorage;
    private UserStorage userStorage;

    @Autowired
    public FilmService(FilmStorage filmStorage, UserStorage userStorage) {
        this.filmStorage = filmStorage;
        this.userStorage = userStorage;
    }

    public void addLike(long id, long userId) {
        if (!userStorage.getUsers().containsKey(userId)) {
            throw new NotFoundException("Неверный Id пользователя");
        }
        if (!filmStorage.getFilms().containsKey(id)) {
            throw new NotFoundException("Неверный Id фильма");
        }
        filmStorage.addLike(id, userId);
    }

    public void deleteLike(long id, long userId) {
        if (!userStorage.getUsers().containsKey(userId)) {
            throw new NotFoundException("Неверный Id пользователя");
        }
        if (!filmStorage.getFilms().containsKey(id)) {
            throw new NotFoundException("Неверный Id фильма");
        }
        filmStorage.deleteLike(id, userId);
    }

    public Collection<Film> mostLike(int count) {
        return filmStorage.mostLike(count);
    }
}
