package ru.yandex.practicum.filmorate.controller;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.util.List;

@RestController
@RequestMapping("/mpa")
public class MpaController {
    private final JdbcTemplate jdbcTemplate;

    public MpaController(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @GetMapping
    public List<Mpa> getAll() {
        return jdbcTemplate.query("SELECT * FROM mpa_ratings ORDER BY id", (rs, rowNum) -> {
            Mpa mpa = new Mpa();
            mpa.setId(rs.getInt("id"));
            mpa.setName(rs.getString("name"));
            return mpa;
        });
    }

    @GetMapping("/{id}")
    public Mpa getById(@PathVariable Integer id) {
        try {
            return jdbcTemplate.queryForObject("SELECT * FROM mpa_ratings WHERE id = ?", (rs, rowNum) -> {
                Mpa mpa = new Mpa();
                mpa.setId(rs.getInt("id"));
                mpa.setName(rs.getString("name"));
                return mpa;
            }, id);
        } catch (Exception e) {
            throw new NotFoundException("Рейтинг не найден");
        }
    }
}