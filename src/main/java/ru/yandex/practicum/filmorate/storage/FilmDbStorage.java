package ru.yandex.practicum.filmorate.storage;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exception.InternalServerException;
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
    private static final String UPDATE_QUERY = "UPDATE films SET name=?, DESCRIPTION=?, RELEASEDATE=?," +
            " DURATION=?, MPA_ID=? WHERE ID = ?";
    private static final String FIND_BY_ID_QUERY = "SELECT * FROM films AS F LEFT JOIN GENRES G on F.ID = G.ID_FILM " +
            "LEFT JOIN GENRE GN on G.ID_GENRE = GN.ID LEFT JOIN MPA M on F.MPA_ID = M.ID WHERE F.ID = ?";
    private static final String FIND_ALL = "SELECT * FROM films AS F LEFT JOIN GENRES G on F.ID = G.ID_FILM " +
            "LEFT JOIN GENRE GN on G.ID_GENRE = GN.ID LEFT JOIN MPA M on F.MPA_ID = M.ID ORDER BY F.ID ";
    private static final String FIND_MOST_LIKE = "SELECT * FROM (SELECT ID_FILM, count(ID_FILM) FROM FILMLIKES " +
            "GROUP BY ID_FILM ORDER BY count(ID_FILM) desc LIMIT ?) AS FL " +
            "LEFT JOIN films F ON FL.ID_FILM = F.ID LEFT JOIN GENRES G on F.ID = G.ID_FILM " +
            "LEFT JOIN GENRE GN on G.ID_GENRE = GN.ID LEFT JOIN MPA M on F.MPA_ID = M.ID";

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
