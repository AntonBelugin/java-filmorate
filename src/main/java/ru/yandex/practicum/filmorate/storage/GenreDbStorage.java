package ru.yandex.practicum.filmorate.storage;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
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
    private static final String FIND_BY_ID_QUERY = "SELECT * FROM GENRE WHERE ID = ?";
    private static final String FIND_ALL_QUERY = "SELECT * FROM GENRE ORDER BY ID";
    private static final String INSERT_QUERY_GENRE = "INSERT INTO GENRES (ID_FILM, ID_GENRE) SELECT ?, ? " +
            "WHERE NOT EXISTS(SELECT * FROM GENRES WHERE ID_FILM = ? AND ID_GENRE = ?)";

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
        for (Genre genre: film.getGenres()) {
            jdbc.update(INSERT_QUERY_GENRE,
                    film.getId(),
                    genre.getId(),
                    film.getId(),
                    genre.getId()
            );
        }
    }

}
