package ru.yandex.practicum.filmorate.storage;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exception.InternalServerException;
import ru.yandex.practicum.filmorate.interfaces.FilmStorage;
import ru.yandex.practicum.filmorate.mappers.FilmResultSetExtractor;
import ru.yandex.practicum.filmorate.model.Film;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.*;

@Repository
@RequiredArgsConstructor
public class FilmDbStorage implements FilmStorage {
    private final JdbcTemplate jdbc;
    private static final String INSERT_QUERY = "INSERT INTO films (name, description, releasedate, duration, mpa_id)" +
            " VALUES (?, ?, ?, ?, ?)";
    private static final String UPDATE_QUERY = "UPDATE films SET name=?, description=?, releasedate=?," +
            " duration=?, mpa_id=? WHERE id = ?";
    private static final String FIND_BY_ID_QUERY = "SELECT * FROM films AS f LEFT JOIN genres g on f.id = g.id_film " +
            "LEFT JOIN genre gn on g.id_genre = gn.id LEFT JOIN mpa m on f.mpa_id = m.id WHERE f.id = ?";
    private static final String FIND_ALL = "SELECT * FROM films AS f LEFT JOIN genres g on f.id = g.id_film " +
            "LEFT JOIN genre gn on g.id_genre = gn.id LEFT JOIN mpa m on f.mpa_id = m.id ORDER BY f.id ";
    private static final String FIND_MOST_LIKE = "SELECT * FROM (SELECT id_film, count(id_film) FROM filmlikes " +
            "GROUP BY id_film ORDER BY count(id_film) desc LIMIT ?) AS fl " +
            "LEFT JOIN films f ON fl.id_film = f.id LEFT JOIN genres g on f.id = g.id_film " +
            "LEFT JOIN genre gn on g.id_genre = gn.id LEFT JOIN mpa m on f.mpa_id = m.id";

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

        Long id = keyHolder.getKeyAs(Long.class);
        if (id != null) {
            film.setId(id);
        } else {
            throw new InternalServerException("Не удалось сохранить данные");
        }
        return film;
    }

    @Override
    public Film update(Film film) {
        jdbc.update(UPDATE_QUERY,
                film.getName(),
                film.getDescription(),
                film.getReleaseDate(),
                film.getDuration(),
                film.getMpa().getId(),
                film.getId()
        );
        return film;
    }

    @Override
    public List<Film> getAll() {
        try {
            return jdbc.query(FIND_ALL, new FilmResultSetExtractor());
        } catch (EmptyResultDataAccessException ignored) {
            return null;
        }
    }

    @Override
    public Collection<Film> mostLike(int count) {
        try {
            return jdbc.query(FIND_MOST_LIKE, new FilmResultSetExtractor(), count);
        } catch (EmptyResultDataAccessException ignored) {
            return null;
        }
    }

    @Override
    public Optional<Film> findById(long filmId) {
        try {
            List<Film> films = jdbc.query(FIND_BY_ID_QUERY, new FilmResultSetExtractor(), (int) filmId);
            if (films != null && !films.isEmpty()) {
                return Optional.of(films.getFirst());
            }
            return Optional.empty();
        } catch (EmptyResultDataAccessException ignored) {
            return Optional.empty();
        }
    }

}
