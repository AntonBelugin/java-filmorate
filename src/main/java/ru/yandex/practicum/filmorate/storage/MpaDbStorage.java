package ru.yandex.practicum.filmorate.storage;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class MpaDbStorage {
    private final JdbcTemplate jdbc;
    private static final String FIND_BY_ID_QUERY = "SELECT mpa FROM mpa WHERE id = ?";

    public Optional<String> findById(long mpaId) {
        try {
            String nameMpa = jdbc.queryForObject(FIND_BY_ID_QUERY, String.class, (int) mpaId);
            System.out.println(nameMpa);
            return Optional.ofNullable(nameMpa);
        } catch (EmptyResultDataAccessException ignored) {
            return Optional.empty();
        }
    }
}
