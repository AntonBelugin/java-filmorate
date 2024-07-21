package ru.yandex.practicum.filmorate.storage;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.mappers.GenreResultSetExtractor;
import ru.yandex.practicum.filmorate.mappers.GenreRowMapper;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;

import java.util.*;

@Repository
@RequiredArgsConstructor
public class GenreDbStorage {
    private final JdbcTemplate jdbc;
    private final GenreRowMapper mapper;
    private static final String FIND_BY_ID_QUERY = "SELECT * FROM genre WHERE id = ?";
    private static final String FIND_ALL_QUERY = "SELECT * FROM genre ORDER BY id";
    private static final String INSERT_QUERY_GENRE = "INSERT INTO genres (id_film, id_genre) SELECT ?, ? " +
            " WHERE NOT EXISTS(SELECT * FROM genres WHERE id_film = ? AND id_genre = ?)";

    public Collection<Genre> findAll() {
        try {
            return jdbc.query(FIND_ALL_QUERY, new GenreResultSetExtractor());
        } catch (EmptyResultDataAccessException ignored) {
            return null;
        }
    }

    public Optional<Genre> findById(int id) {
        try {
            Genre genre = jdbc.queryForObject(FIND_BY_ID_QUERY, mapper, id);
            return Optional.ofNullable(genre);
        } catch (EmptyResultDataAccessException ignored) {
            return Optional.empty();
        }
    }

    public void save(Film film) {
        List<Object[]> batch = new ArrayList<>();
        for (Genre genre : film.getGenres()) {
            Object[] values = new Object[] {
                    film.getId(), genre.getId(), film.getId(), genre.getId()};
            batch.add(values);
        }
        jdbc.batchUpdate(INSERT_QUERY_GENRE, batch);
    }

}
