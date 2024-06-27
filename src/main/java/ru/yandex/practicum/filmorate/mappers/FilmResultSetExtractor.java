package ru.yandex.practicum.filmorate.mappers;

import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;

@Component
public class FilmResultSetExtractor implements ResultSetExtractor<List<Film>> {
    @Override
    public List<Film> extractData(ResultSet rs) throws SQLException {
        LinkedList<Film> filmList = new LinkedList<>();
        if (!rs.isBeforeFirst()) {
            return filmList;
        }
        rs.next();
        long count = 0;
        Film film = new Film();
        long filmId = rs.getLong("id");
        count = filmId;
        film.setId(filmId);
        setFilm(film, rs);
        LinkedHashSet<Genre> genres = new LinkedHashSet<>();
        if (rs.getInt("id_genre") != 0) {
            setGenre(film, genres, rs);
        }
        filmList.add(film);
        while (rs.next()) {
            filmId = rs.getLong("id");
            if (filmId != count) {
                film = new Film();
                count = filmId;
                film.setId(filmId);
                setFilm(film, rs);
                genres = new LinkedHashSet<>();
                if (rs.getInt("id_genre") != 0) {
                    setGenre(film, genres, rs);
                }
                filmList.add(film);
            }
            filmList.remove(film);
            setGenre(film, genres, rs);
            filmList.add(film);
        }
        return filmList;
    }

    private void setFilm(Film film, ResultSet rs) throws SQLException {
        film.setName(rs.getString("name"));
        film.setDescription(rs.getString("description"));
        film.setReleaseDate(rs.getDate("releasedate").toLocalDate());
        film.setDuration(rs.getInt("duration"));
        Mpa mpa = new Mpa();
        mpa.setId(rs.getInt("mpa_id"));
        mpa.setName(rs.getString("mpa"));
        film.setMpa(mpa);
    }

    private void setGenre(Film film, LinkedHashSet<Genre> genres, ResultSet rs) throws SQLException {
        Genre genre = new Genre();
        genre.setId(rs.getInt("id_genre"));
        genre.setName(rs.getString("genre"));
        genres.add(genre);
        film.setGenres(genres);
    }
}