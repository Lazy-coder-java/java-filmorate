package ru.yandex.practicum.filmorate.service;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.List;

@Service
public class FilmService {
    private final FilmStorage filmStorage;
    private final UserStorage userStorage;
    private final JdbcTemplate jdbcTemplate;

    public FilmService(FilmStorage filmStorage, UserStorage userStorage, JdbcTemplate jdbcTemplate) {
        this.filmStorage = filmStorage;
        this.userStorage = userStorage;
        this.jdbcTemplate = jdbcTemplate;
    }

    public Film create(Film film) {
        return filmStorage.create(film);
    }

    public Film update(Film film) {
        return filmStorage.update(film);
    }

    public List<Film> getAll() {
        return filmStorage.getAll();
    }

    public Film getById(Integer id) {
        return filmStorage.getById(id);
    }

    public void addLike(Integer filmId, Integer userId) {
        filmStorage.getById(filmId);
        userStorage.getById(userId);
        jdbcTemplate.update("INSERT INTO likes (film_id, user_id) VALUES (?, ?)", filmId, userId);
    }

    public void removeLike(Integer filmId, Integer userId) {
        filmStorage.getById(filmId);
        userStorage.getById(userId);
        jdbcTemplate.update("DELETE FROM likes WHERE film_id = ? AND user_id = ?", filmId, userId);
    }

    public List<Film> getPopular(int count) {
        String sql = "SELECT f.id FROM films f LEFT JOIN likes l ON f.id = l.film_id " +
                "GROUP BY f.id ORDER BY COUNT(l.user_id) DESC LIMIT ?";

        List<Integer> popularIds = jdbcTemplate.queryForList(sql, Integer.class, count);
        List<Film> allFilms = filmStorage.getAll();

        List<Film> result = new java.util.ArrayList<>();
        for (Integer id : popularIds) {
            for (Film f : allFilms) {
                if (f.getId().equals(id)) {
                    result.add(f);
                }
            }
        }
        return result;
    }
}