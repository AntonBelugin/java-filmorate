package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.GenreDbStorage;
import ru.yandex.practicum.filmorate.storage.MpaDbStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;
import java.util.Collection;
import java.util.List;

@Service
public class FilmService {
    private final MpaDbStorage mpaDbStorage;
    private final GenreDbStorage genreDbStorage;
    private FilmStorage filmStorage;
    private UserStorage userStorage;

    @Autowired
    public FilmService(FilmStorage filmStorage, UserStorage userStorage, MpaDbStorage mpaDbStorage, GenreDbStorage genreDbStorage) {
        this.filmStorage = filmStorage;
        this.userStorage = userStorage;
        this.mpaDbStorage = mpaDbStorage;
        this.genreDbStorage = genreDbStorage;
    }

    public Film create(Film film) {
        List<String> genres = genreDbStorage.findAll();
       // System.out.println(genres);
        for (Genre genre: film.getGenres()) {
            if (genre.getId() > genres.size()) {
                throw new ValidationException("Жанра с id " + genre.getId() + " не существует");
            }
        }
        if (mpaDbStorage.findById(film.getMpa().getId()).isEmpty()) {
            throw new ValidationException("Рейтинга с id " + film.getMpa().getId() + " не существует");
        }
        Film newFilm = filmStorage.add(film);
        if (!film.getGenres().isEmpty()) {
            genreDbStorage.save(film);
        }
        return newFilm;
    }

    public Film update(Film film) {
        if (filmStorage.findById(film.getId()).isEmpty()) {
            throw new NotFoundException("Фильма с id " + film.getId() + " не существует");
        }
        List<String> genres = genreDbStorage.findAll();
        for (Genre genre: film.getGenres()) {
            if (genre.getId() > genres.size()) {
                throw new ValidationException("Жанра с id " + genre.getId() + " не существует");
            }
        }
        if (mpaDbStorage.findById(film.getMpa().getId()).isEmpty()) {
            throw new ValidationException("Рейтинга с id " + film.getMpa().getId() + " не существует");
        }
        // update Genre TODO
        return filmStorage.update(film);
    }

    public Collection<Film> findAll() {
        return filmStorage.getAll();
    }

    public void addLike(long id, long userId) {
        userStorage.findById(userId);
        filmStorage.findById(id);
        filmStorage.addLike(id, userId);
    }

    public void deleteLike(long id, long userId) {
        userStorage.findById(userId);
        filmStorage.findById(id);
        filmStorage.deleteLike(id, userId);
    }

    public Collection<Film> mostLike(int count) {
        return filmStorage.mostLike(count);
    }

}
