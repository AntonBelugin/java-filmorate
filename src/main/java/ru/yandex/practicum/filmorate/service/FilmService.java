package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.*;

import java.util.Collection;
import java.util.List;

@Service
public class FilmService {
    private final MpaDbStorage mpaDbStorage;
    private final GenreDbStorage genreDbStorage;
    private final FilmLikeDbStorage filmLikeDbStorage;
    private FilmStorage filmStorage;
    private UserStorage userStorage;

    @Autowired
    public FilmService(FilmStorage filmStorage, UserStorage userStorage, MpaDbStorage mpaDbStorage, GenreDbStorage genreDbStorage, FilmLikeDbStorage filmLikeDbStorage) {
        this.filmStorage = filmStorage;
        this.userStorage = userStorage;
        this.mpaDbStorage = mpaDbStorage;
        this.genreDbStorage = genreDbStorage;
        this.filmLikeDbStorage = filmLikeDbStorage;
    }

    public Film create(Film film) {
        List<String> genres = genreDbStorage.findAll();

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
        test(film.getId());

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
        testUser(userId);
        test(id);
        filmLikeDbStorage.add(id, userId);
    }

    public void deleteLike(long id, long userId) {
        testUser(userId);
        test(id);
        filmLikeDbStorage.delete(id, userId);
    }

    public Collection<Film> mostLike(int count) {
        return filmStorage.mostLike(count);
    }

    public void test(long id) {
        if (filmStorage.findById(id).isEmpty()) {
            throw new NotFoundException("Фильма с id " + id + " не существует");
        }
    }

    private void testUser(long id) {
        if (userStorage.findById(id).isEmpty()) {
            throw new NotFoundException("Пользователя с id " + id + " не существует");
        }
    }

}
