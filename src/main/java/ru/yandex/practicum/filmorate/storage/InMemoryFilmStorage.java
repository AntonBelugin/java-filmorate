package ru.yandex.practicum.filmorate.storage;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.*;
import java.util.stream.Collectors;

@Component
public class InMemoryFilmStorage implements FilmStorage {
    private Map<Long, Film> films = new HashMap<>();
    private HashMap<Long, Set<Long>> filmLikes = new HashMap<>();


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

    @Override
    public void addLike(long id, long userId) {
        Set<Long> fLikes = filmLikes.computeIfAbsent(id, nId -> new HashSet<>());
        fLikes.add(userId);
        filmLikes.put(id, fLikes);
    }

    @Override
    public void deleteLike(long id, long userId) {
        filmLikes.get(id).remove(userId);
    }

    @Override
    public Collection<Film> mostLike(int count) {
        LinkedHashMap<Long, Integer> map = new LinkedHashMap<>();
        filmLikes.forEach((key, value) -> map.put(key, value.size()));
       /* LinkedHashMap<Long, Integer> sortedMap = new LinkedHashMap<>();
        map.entrySet().stream().sorted(Map.Entry.comparingByValue())
                .forEach((key) -> sortedMap.put(key, value));*/

        Map<Long, Integer> sortedMap = map.entrySet()
                .stream()
                .sorted(Map.Entry.comparingByValue())
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue,
                        (oldValue, newValue) -> oldValue, LinkedHashMap::new));

        Collection<Film> result = new ArrayList<>();



        return map;
        filmLikes.entrySet().stream()
                        .sorted(userLike )
        filmLikes.get(id).remove(userId);
    }

    public Map<Long, Film> getFilms() {
        return films;
    }
}
