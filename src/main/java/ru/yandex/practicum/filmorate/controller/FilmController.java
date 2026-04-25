package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.util.*;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/films")
@Slf4j
public class FilmController {

    private final Map<Integer, Film> films = new HashMap<>();
    private int idCounter = 1;

    private static final LocalDate MIN_DATE = LocalDate.of(1895, 12, 28);

    @PostMapping
    public Film create(@RequestBody Film film) {

        validate(film);

        film.setId(idCounter++);
        films.put(film.getId(), film);

        log.info("Создан фильм: {}", film);
        return film;
    }

    @PutMapping
    public Film update(@RequestBody Film film) {

        if (film.getId() == null || !films.containsKey(film.getId())) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Фильм не найден");
        }

        Film existing = films.get(film.getId());

        if (film.getName() != null) {
            if (film.getName().isBlank()) {
                throw new ValidationException("Название не может быть пустым");
            }
            existing.setName(film.getName());
        }

        if (film.getDescription() != null) {
            if (film.getDescription().length() > 200) {
                throw new ValidationException("Описание слишком длинное");
            }
            existing.setDescription(film.getDescription());
        }

        if (film.getReleaseDate() != null) {
            if (film.getReleaseDate().isBefore(MIN_DATE)) {
                throw new ValidationException("Дата релиза не может быть раньше 28 декабря 1895 года");
            }
            existing.setReleaseDate(film.getReleaseDate());
        }

        if (film.getDuration() != null) {
            if (film.getDuration() <= 0) {
                throw new ValidationException("Длительность должна быть положительной");
            }
            existing.setDuration(film.getDuration());
        }

        log.info("Обновлен фильм: {}", existing);
        return existing;
    }

    @GetMapping
    public Collection<Film> getAll() {
        return films.values();
    }

    private void validate(Film film) {

        if (film.getName() == null || film.getName().isBlank()) {
            throw new ValidationException("Название не может быть пустым");
        }

        if (film.getDescription() != null && film.getDescription().length() > 200) {
            throw new ValidationException("Описание слишком длинное");
        }

        if (film.getReleaseDate() == null || film.getReleaseDate().isBefore(MIN_DATE)) {
            throw new ValidationException("Дата релиза не может быть раньше 28 декабря 1895 года");
        }

        if (film.getDuration() == null || film.getDuration() <= 0) {
            throw new ValidationException("Длительность должна быть положительной");
        }
    }
}
