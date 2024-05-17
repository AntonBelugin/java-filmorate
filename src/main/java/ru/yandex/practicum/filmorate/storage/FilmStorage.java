package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.Collection;
import java.util.Map;

public interface FilmStorage {

    void add(Film film);

    void remove(Film film);

    void update(Film film);

    void addLike(long id, long userId);

    void deleteLike(long id, long userId);

    Collection<Film> mostLike(int count);

    Collection<Film> getAll();

    Map<Long, Film> getFilms();
}
