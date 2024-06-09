package ru.yandex.practicum.filmorate.storage;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.dto.CreateFilmRequest;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import java.util.*;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class FilmDbStorage implements FilmStorage {
    private HashMap<Long, Film> films = new HashMap<>();
    private HashMap<Long, Set<Long>> filmLikes = new HashMap<>();
    private final JdbcTemplate jdbc;
    private static final String INSERT_QUERY = "INSERT INTO films (name, description, releasedate, duration, mpa)" +
            " VALUES (?, ?, ?, ?, ?)";

    @Override
    public void add(CreateFilmRequest request) {
        jdbc.update(INSERT_QUERY,
                request.getName(),
                request.getDescription(),
                request.getReleaseDate(),
                request.getDuration(),
                request.getMpa().getId());
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
        HashMap<Long, Integer> filmsCountLike = new HashMap<>();
        filmLikes.forEach((key, value) -> filmsCountLike.put(key, value.size()));

        Map<Long, Integer> sortedMap = filmsCountLike.entrySet().stream()
                .sorted(Comparator.comparingInt(e -> -e.getValue()))
                .collect(Collectors
                        .toMap(Map.Entry::getKey,
                                Map.Entry::getValue,
                                (e1, e2) -> e1,
                                LinkedHashMap::new));

        List<Long> listId = new ArrayList<>();
        sortedMap.forEach((key, value) -> listId.add(key));

        if (listId.size() <= count) {
            count = listId.size();
        }

        return listId.stream()
                .limit(count)
                .map(id -> films.get(id))
                .toList();
    }

    @Override
    public void testFilm(long filmId) {
        if (!films.containsKey(filmId)) {
            throw new NotFoundException("Фильма с id " + filmId + " не существует");
        }
    }

    public Map<Long, Film> getFilms() {
        return films;
    }

}
