package ru.yandex.practicum.filmorate.storage;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class GenreDbStorage {
    private final JdbcTemplate jdbc;
    private static final String FIND_BY_ID_QUERY = "SELECT genre FROM genre";

    public List<String> findAll() {
            List<String> genres = jdbc.queryForList(FIND_BY_ID_QUERY, String.class);
            return genres;
    }

   /* public List<String> findById(long genreId) {
        List<String> genres = jdbc.queryForList(FIND_BY_ID_QUERY, String.class, (int) genreId);
        System.out.println(genres);
        return genres;
    }*/
}
