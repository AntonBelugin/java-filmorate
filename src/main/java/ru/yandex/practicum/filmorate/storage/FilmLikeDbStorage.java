package ru.yandex.practicum.filmorate.storage;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class FilmLikeDbStorage {
    private final JdbcTemplate jdbc;
    private static final String INSERT_QUERY = "INSERT INTO FILMLIKES (ID_FILM, ID_USER) SELECT ?, ? " +
            "WHERE NOT EXISTS(SELECT * FROM FILMLIKES WHERE ID_FILM = ? AND ID_USER = ?)";
    private static final String DELETE_QUERY = "DELETE FROM FILMLIKES WHERE ID_FILM = ? AND ID_USER = ?";

    public void add(long filmId, long userId) {
        jdbc.update(
                INSERT_QUERY,
                filmId,
                userId,
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
