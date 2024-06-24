package ru.yandex.practicum.filmorate.storage;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.dto.CreateFilmRequest;
import ru.yandex.practicum.filmorate.exception.InternalServerException;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.model.User;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.*;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class FilmDbStorage implements FilmStorage {
    private HashMap<Long, Film> films = new HashMap<>();
    private HashMap<Long, Set<Long>> filmLikes = new HashMap<>();
    private final JdbcTemplate jdbc;
    private final RowMapper<Film> mapper;
    private static final String INSERT_QUERY = "INSERT INTO films (name, description, releasedate, duration, mpa)" +
            " VALUES (?, ?, ?, ?, ?)";

    private static final String UPDATE_QUERY = "UPDATE films SET name=?, description=?, releasedate=?," +
            " duration=?, mpa=? WHERE id = ?";
    private static final String FIND_BY_ID_QUERY = "SELECT * FROM films WHERE id = ?";
    private static final String FIND_ALL = "SELECT * FROM films AS f JOIN GENRES G on f.ID = G.ID_FILM";
    private static final String FIND_MOST_LIKE = "SELECT * FROM films AS f JOIN GENRES G on f.ID = G.ID_FILM " +
            "WHERE id IN (SELECT id_film FROM " +
            "(SELECT id_film, COUNT(id_film) FROM filmlikes GROUP BY id_film ORDER BY COUNT(ID_FILM) desc LIMIT ?));";

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
    public void remove(Film film) {
        films.remove(film.getId());
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
        try {
            return jdbc.query(FIND_MOST_LIKE, new FilmResultSetExtractor(), count);
        } catch (EmptyResultDataAccessException ignored) {
            return null;
        }


       /* HashMap<Long, Integer> filmsCountLike = new HashMap<>();
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
                .toList();*/
    }

    @Override
    public Optional<Film> findById(long filmId) {
        try {
            Film film = jdbc.queryForObject(FIND_BY_ID_QUERY, mapper, (int) filmId);

            return Optional.ofNullable(film);
        } catch (EmptyResultDataAccessException ignored) {

            return Optional.empty();
        }
    }

    public class FilmResultSetExtractor implements ResultSetExtractor<List<Film>> {
        @Override
        public List<Film> extractData(ResultSet rs) throws SQLException {
            LinkedList<Film> filmList = new LinkedList<>();
            if (!rs.isBeforeFirst() ) {
                return filmList;
            }
            rs.next();
            long count = 0;
            Film film = new Film();
            long filmId = rs.getLong("id");
            film.setId(filmId);
            film.setName(rs.getString("name"));
            film.setDescription(rs.getString("description"));
            film.setReleaseDate(rs.getDate("releasedate").toLocalDate());
            film.setDuration(rs.getInt("duration"));
            Mpa mpa = new Mpa();
            mpa.setId(rs.getInt("mpa"));
            film.setMpa(mpa);
            LinkedHashSet<Genre> genres = new LinkedHashSet<>();
            Genre genre = new Genre();
            genre.setId(rs.getInt("id_genre"));
            genres.add(genre);
            film.setGenres(genres);
            filmList.add(film);
            while (rs.next()) {
                filmId = rs.getLong("id");
                if (filmId > count) {
                    film = new Film();
                    count = filmId;
                    film.setId(filmId);
                    film.setName(rs.getString("name"));
                    film.setDescription(rs.getString("description"));
                    film.setReleaseDate(rs.getDate("releasedate").toLocalDate());
                    film.setDuration(rs.getInt("duration"));
                    mpa = new Mpa();
                    mpa.setId(rs.getInt("mpa"));
                    film.setMpa(mpa);
                    genres = new LinkedHashSet<>();
                    genre = new Genre();
                    genre.setId(rs.getInt("id_genre"));
                    genres.add(genre);
                    film.setGenres(genres);
                    filmList.add(film);
                }
                genre = new Genre();
                genre.setId(rs.getInt("id_genre"));
                film.getGenres().add(genre);

            }
            return filmList;
        }

    }

}
