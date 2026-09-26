package ru.yandex.practicum.filmorate.controller;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Genre;

import java.util.List;

@RestController
@RequestMapping("/genres")
public class GenreController {
    private final JdbcTemplate jdbcTemplate;

    public GenreController(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @GetMapping
    public List<Genre> getAll() {
        return jdbcTemplate.query("SELECT * FROM genres ORDER BY id", (rs, rowNum) -> {
            Genre genre = new Genre();
            genre.setId(rs.getInt("id"));
            genre.setName(rs.getString("name"));
            return genre;
        });
    }

    @GetMapping("/{id}")
    public Genre getById(@PathVariable Integer id) {
        try {
            return jdbcTemplate.queryForObject("SELECT * FROM genres WHERE id = ?", (rs, rowNum) -> {
                Genre genre = new Genre();
                genre.setId(rs.getInt("id"));
                genre.setName(rs.getString("name"));
                return genre;
            }, id);
        } catch (Exception e) {
            throw new NotFoundException("Жанр не найден");
        }
    }
}
