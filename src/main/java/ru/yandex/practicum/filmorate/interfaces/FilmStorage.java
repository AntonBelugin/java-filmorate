package ru.yandex.practicum.filmorate.interfaces;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface FilmStorage {

    Film add(Film film);

    Film update(Film film);

    Collection<Film> mostLike(int count);

    List<Film> getAll();

    Optional<Film> findById(long filmId);

}
