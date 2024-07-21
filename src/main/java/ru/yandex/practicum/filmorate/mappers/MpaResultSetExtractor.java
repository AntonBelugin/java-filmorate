package ru.yandex.practicum.filmorate.mappers;

import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

@Component
public class MpaResultSetExtractor implements ResultSetExtractor<List<Mpa>> {
    public List<Mpa> extractData(ResultSet rs) throws SQLException {
        LinkedList<Mpa> mpaList = new LinkedList<>();
        if (!rs.isBeforeFirst()) {
            return mpaList;
        }
        while (rs.next()) {
            Mpa mpa = new Mpa();
            mpa.setId(rs.getInt("id"));
            mpa.setName(rs.getString("mpa"));
            mpaList.add(mpa);

        }
        return mpaList;
    }
}