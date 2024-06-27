package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.*;
import java.util.Collection;
import java.util.HashSet;

@Service
@RequiredArgsConstructor
public class FilmService {
    private final MpaDbStorage mpaDbStorage;
    private final GenreDbStorage genreDbStorage;
    private final FilmLikeDbStorage filmLikeDbStorage;
    private final FilmStorage filmStorage;
    private final UserStorage userStorage;

    public Film create(Film film) {
        testGenre(film.getGenres());
        testMpa(film.getMpa());
        Film newFilm = filmStorage.add(film);
        if (!film.getGenres().isEmpty()) {
            genreDbStorage.save(film);
        }
        return newFilm;
    }

    public Film update(Film film) {
        testFilm(film.getId());
        testGenre(film.getGenres());
        testMpa(film.getMpa());
        if (!film.getGenres().isEmpty()) {
            genreDbStorage.save(film);
        }
        return filmStorage.update(film);
    }

    public Collection<Film> findAll() {
        return filmStorage.getAll();
    }

    public Film findById(long id) {
        testFilm(id);
        if (filmStorage.findById(id).isPresent()) {
            return filmStorage.findById(id).get();
        } else {
            throw new NotFoundException("Фильм с id " + id + " не найден");
        }
    }

    public void addLike(long id, long userId) {
        testUser(userId);
        testFilm(id);
        filmLikeDbStorage.add(id, userId);
    }

    public void deleteLike(long id, long userId) {
        testUser(userId);
        testFilm(id);
        filmLikeDbStorage.delete(id, userId);
    }

    public Collection<Film> mostLike(int count) {
        return filmStorage.mostLike(count);
    }

    public void testFilm(long id) {
        if (filmStorage.findById(id).isEmpty()) {
            throw new NotFoundException("Фильма с id " + id + " не существует");
        }
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
