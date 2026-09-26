package ru.yandex.practicum.filmorate.service;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.List;

@Service
public class UserService {
    private final UserStorage userStorage;
    private final JdbcTemplate jdbcTemplate;

    public UserService(UserStorage userStorage, JdbcTemplate jdbcTemplate) {
        this.userStorage = userStorage;
        this.jdbcTemplate = jdbcTemplate;
    }

    public User create(User user) {
        return userStorage.create(user);
    }

    public User update(User user) {
        return userStorage.update(user);
    }

    public List<User> getAll() {
        return userStorage.getAll();
    }

    public User getById(Integer id) {
        return userStorage.getById(id);
    }

    public void addFriend(Integer userId, Integer friendId) {
        userStorage.getById(userId);
        userStorage.getById(friendId);

        jdbcTemplate.update("INSERT INTO friends (user_id, friend_id) VALUES (?, ?)", userId, friendId);
    }

    public void removeFriend(Integer userId, Integer friendId) {
        userStorage.getById(userId);
        userStorage.getById(friendId);

        jdbcTemplate.update("DELETE FROM friends WHERE user_id = ? AND friend_id = ?", userId, friendId);
    }

    public List<User> getFriends(Integer userId) {
        userStorage.getById(userId);
        String sql = "SELECT * FROM users WHERE id IN (SELECT friend_id FROM friends WHERE user_id = ?)";
        return jdbcTemplate.query(sql, (rs, rowNum) -> {
            User user = new User();
            user.setId(rs.getInt("id"));
            user.setEmail(rs.getString("email"));
            user.setLogin(rs.getString("login"));
            user.setName(rs.getString("name"));
            user.setBirthday(rs.getDate("birthday").toLocalDate());
            return user;
        }, userId);
    }

    public List<User> getCommonFriends(Integer id, Integer otherId) {
        String sql = "SELECT * FROM users WHERE id IN " +
                "(SELECT friend_id FROM friends WHERE user_id = ?) AND id IN " +
                "(SELECT friend_id FROM friends WHERE user_id = ?)";
        return jdbcTemplate.query(sql, (rs, rowNum) -> {
            User u = new User();
            u.setId(rs.getInt("id"));
            u.setEmail(rs.getString("email"));
            u.setLogin(rs.getString("login"));
            u.setName(rs.getString("name"));
            u.setBirthday(rs.getDate("birthday").toLocalDate());
            return u;
        }, id, otherId);
    }
}
