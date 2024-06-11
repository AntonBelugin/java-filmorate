package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface FilmStorage {

    Film add(Film film);

    void remove(Film film);

    Film update(Film film);

    void addLike(long id, long userId);

    void deleteLike(long id, long userId);

    Collection<Film> mostLike(int count);

    List<Film> getAll();

   // Map<Long, Film> getFilms();

    Optional<Film> findById(long filmId);
}
