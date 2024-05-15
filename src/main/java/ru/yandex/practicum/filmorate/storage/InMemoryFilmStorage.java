package ru.yandex.practicum.filmorate.storage;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Component
public class InMemoryFilmStorage implements FilmStorage {
    private Map<Long, Film> films = new HashMap<>();


    @Override
    public void add(Film film) {
        films.put(film.getId(), film);
    }

    @Override
    public void remove(Film film) {
        films.remove(film.getId());
    }

    @Override
    public void update(Film upFilm) {
        if (!films.containsKey(upFilm.getId())) {
            throw new ValidationException("Неверный Id");
        }
        films.put(upFilm.getId(), upFilm);
    }

    @Override
    public Collection<Film> getAll() {
        return films.values();
    }

    public Map<Long, Film> getFilms() {
        return films;
    }
}