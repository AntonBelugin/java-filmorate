package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.controller.ValidateService;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.*;
import java.util.Collection;
import java.util.HashSet;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class FilmService {
    private final MpaDbStorage mpaDbStorage;
    private final GenreDbStorage genreDbStorage;
    private final FilmLikeDbStorage filmLikeDbStorage;
    private final FilmStorage filmStorage;
    private final UserStorage userStorage;
    private final ValidateService validateService;

    public Film create(Film film) {
        testFilm(film);
        Film newFilm = filmStorage.add(film);
        if (!film.getGenres().isEmpty()) {
            genreDbStorage.save(film);
        }
        return newFilm;
    }

    public Film update(Film film) {
        checkFilm(film.getId());
        testFilm(film);
        if (!film.getGenres().isEmpty()) {
            genreDbStorage.save(film);
        }
        return filmStorage.update(film);
    }

    public Collection<Film> findAll() {
        return filmStorage.getAll();
    }

    public Film findById(long id) {
        Optional<Film> filmOptional = filmStorage.findById(id);
        if (filmOptional.isPresent()) {
            return filmOptional.get();
        } else {
            throw new NotFoundException("Фильм с id " + id + " не найден");
        }
    }

    public void addLike(long id, long userId) {
        testUser(userId);
        checkFilm(id);
        filmLikeDbStorage.add(id, userId);
    }

    public void deleteLike(long id, long userId) {
        testUser(userId);
        checkFilm(id);
        filmLikeDbStorage.delete(id, userId);
    }

    public Collection<Film> mostLike(int count) {
        return filmStorage.mostLike(count);
    }

    public void checkFilm(long id) {
        if (filmStorage.findById(id).isEmpty()) {
            throw new NotFoundException("Фильма с id " + id + " не существует");
        }
    }

    public void testFilm(Film film) {
        validateService.validateFilm(film);
        testGenre(film.getGenres());
        testMpa(film.getMpa());
    }

    private void testUser(long id) {
        if (userStorage.findById(id).isEmpty()) {
            throw new NotFoundException("Пользователя с id " + id + " не существует");
        }
    }

    private void testGenre(HashSet<Genre> genres) {
        for (Genre genre: genres) {
            if (genre.getId() > genreDbStorage.findAll().size()) {
                throw new ValidationException("Жанра с id " + genre.getId() + " не существует");
            }
        }
    }

    private void testMpa(Mpa mpa) {
        if (mpa.getId() > mpaDbStorage.findAll().size()) {
            System.out.println(mpaDbStorage.findAll().size());
            throw new ValidationException("Рейтинга с id " + mpa.getId() + " не существует");
        }
    }

}
