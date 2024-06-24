package ru.yandex.practicum.filmorate.storage;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class FilmLikeDbStorage {
    private final JdbcTemplate jdbc;
    private static final String INSERT_QUERY = "INSERT INTO filmlikes (id_film, id_user) " +
            " VALUES (?, ?) ";
    private static final String DELETE_QUERY = "DELETE FROM filmlikes WHERE id_film = ? AND id_user = ?";


    public void add(long filmId, long userId) {
        jdbc.update(
                INSERT_QUERY,
                filmId,
                userId
        );
    }

    public void delete(long filmId, long userId) {
        jdbc.update(
                DELETE_QUERY,
                filmId,
                userId
        );
    }

}
