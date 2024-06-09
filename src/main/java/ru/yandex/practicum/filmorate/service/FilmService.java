package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dto.CreateFilmRequest;
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

    public void create(CreateFilmRequest request) {
        filmStorage.add(request);
    }

    public void update(Film upFilm) {
        filmStorage.testFilm(upFilm.getId());
        filmStorage.update(upFilm);
    }

    public Collection<Film> findAll() {
        return filmStorage.getAll();
    }

    public void addLike(long id, long userId) {
        userStorage.findById(userId);
        filmStorage.testFilm(id);
        filmStorage.addLike(id, userId);
    }

    public void deleteLike(long id, long userId) {
        userStorage.findById(userId);
        filmStorage.testFilm(id);
        filmStorage.deleteLike(id, userId);
    }

    public Collection<Film> mostLike(int count) {
        return filmStorage.mostLike(count);
    }

}
