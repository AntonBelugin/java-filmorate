package ru.yandex.practicum.filmorate.storage;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.mappers.MpaResultSetExtractor;
import ru.yandex.practicum.filmorate.mappers.MpaRowMapper;
import ru.yandex.practicum.filmorate.model.Mpa;
import java.util.Collection;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class MpaDbStorage {
    private final JdbcTemplate jdbc;
    private final MpaRowMapper mapper;
    private static final String FIND_BY_ID_QUERY = "SELECT * FROM MPA WHERE ID = ?";
    private static final String FIND_ALL_QUERY = "SELECT * FROM MPA ORDER BY ID";

    public Collection<Mpa> findAll() {
        try {
            return jdbc.query(FIND_ALL_QUERY, new MpaResultSetExtractor());
        } catch (EmptyResultDataAccessException ignored) {
            return null;
        }
    }

    public Optional<Mpa> findById(int id) {
        try {
            Mpa mpa = jdbc.queryForObject(FIND_BY_ID_QUERY, mapper, id);
            return Optional.ofNullable(mpa);
        } catch (EmptyResultDataAccessException ignored) {
            return Optional.empty();
        }
    }

}
