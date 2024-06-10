package ru.yandex.practicum.filmorate.storage;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.dto.CreateFilmRequest;
import ru.yandex.practicum.filmorate.exception.InternalServerException;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.sql.PreparedStatement;
import java.sql.Statement;
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
    private static final String INSERT_QUERY_GENRE = "INSERT INTO genres (id_film, id_genre)" +
            " VALUES (?, ?)";
    private static final String UPDATE_QUERY = "UPDATE films SET name=?, description=?, releasedate=?, duration=?, mpa=? WHERE id = ?";


   // private static final String UPDATE_QUERY = "UPDATE users SET email = ?, login = ?, name = ?, birthday = ? WHERE id = ?";

    @Override
    public Film add(Film film) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbc.update(connection -> {
            PreparedStatement ps = connection
                    .prepareStatement(INSERT_QUERY, Statement.RETURN_GENERATED_KEYS);
            ps.setObject(1, film.getName());
            ps.setObject(2, film.getDescription());
            ps.setObject(3, film.getReleaseDate());
            ps.setObject(4, film.getDuration());
            ps.setObject(5, film.getMpa().getId());
            return ps;
        }, keyHolder);
        System.out.println(keyHolder);

        Long id = keyHolder.getKeyAs(Long.class);
        System.out.println();
        System.out.println(id);
        System.out.println();
        if (id != null) {
            film.setId(id);
        } else {
            throw new InternalServerException("Не удалось сохранить данные");
        }

        if (!film.getGenres().isEmpty()) {
            for (int i = 0; i < film.getGenres().size(); i++) {
                System.out.println(i);
                System.out.println(film.getGenres().get(i));
                jdbc.update(INSERT_QUERY_GENRE,
                        film.getId(),
                        film.getGenres().get(i).getId()
                );
            }
        }

        return film;
        /*jdbc.update(INSERT_QUERY,
                film.getName(),
                film.getDescription(),
                film.getReleaseDate(),
                film.getDuration(),
                film.getMpa().getId());*/
    }

    @Override
    public void remove(Film film) {
        films.remove(film.getId());
    }

    @Override
    public void update(Film upFilm) {
       /* if (!films.containsKey(upFilm.getId())) {
            throw new ValidationException("Неверный Id");
        }*/
        //films.put(upFilm.getId(), upFilm);
        jdbc.update(UPDATE_QUERY,
                upFilm.getName(),
                upFilm.getDescription(),
                upFilm.getReleaseDate(),
                upFilm.getDuration(),
                upFilm.getMpa().getId(),
                upFilm.getId()
        );


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
