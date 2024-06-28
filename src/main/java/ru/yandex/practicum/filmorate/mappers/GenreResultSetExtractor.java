package ru.yandex.practicum.filmorate.mappers;

import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Genre;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

@Component
public class GenreResultSetExtractor implements ResultSetExtractor<List<Genre>> {
    public List<Genre> extractData(ResultSet rs) throws SQLException {
        LinkedList<Genre> genreList = new LinkedList<>();
        if (!rs.isBeforeFirst()) {
            return genreList;
        }
        while (rs.next()) {
            Genre genre = new Genre();
            genre.setId(rs.getInt("id"));
            genre.setName(rs.getString("genre"));
            genreList.add(genre);

        }
        return genreList;
    }
}