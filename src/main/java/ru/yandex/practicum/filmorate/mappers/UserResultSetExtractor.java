package ru.yandex.practicum.filmorate.mappers;

import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.User;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

@Component
public class UserResultSetExtractor implements ResultSetExtractor<List<User>> {
    @Override
    public List<User> extractData(ResultSet rs) throws SQLException {
        LinkedList<User> userList = new LinkedList<>();
        if (!rs.isBeforeFirst()) {
            return userList;
        }
        while (rs.next()) {
            User user = new User();
            user.setId(rs.getLong("id"));
            user.setName(rs.getString("name"));
            user.setEmail(rs.getString("email"));
            user.setLogin(rs.getString("login"));
            user.setBirthday(rs.getDate("birthday").toLocalDate());
            userList.add(user);
        }
        return userList;
    }
}
