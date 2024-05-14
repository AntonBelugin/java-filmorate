package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.Collection;
import java.util.Map;

public interface FilmStorage {

    void addFilm(Film film);

    void removeFilm(Film film);

    void updateFilm(Film film);

    Collection<Film> getAllFilms();

    Map<Long, Film> getFilms();
}
