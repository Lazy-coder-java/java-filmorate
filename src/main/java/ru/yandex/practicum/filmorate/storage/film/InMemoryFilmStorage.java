package ru.yandex.practicum.filmorate.storage.film;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Component
@Slf4j
public class InMemoryFilmStorage implements FilmStorage {

    private final Map<Integer, Film> films = new HashMap<>();
    private int idCounter = 1;
    private static final LocalDate MIN_RELEASE_DATE = LocalDate.of(1895, 12, 28);

    @Override
    public Film create(Film film) {
        film.setId(idCounter++);
        films.put(film.getId(), film);
        log.info("Создан фильм {}", film);
        return film;
    }

    @Override
    public Film update(Film film) {
        if (film.getId() == null || !films.containsKey(film.getId())) {
            throw new NotFoundException("Фильм не найден");
        }

        Film existingFilm = films.get(film.getId());

        if (film.getName() != null) {
            if (film.getName().isBlank()) {
                throw new ValidationException("Название не может быть пустым");
            }
            existingFilm.setName(film.getName());
        }

        if (film.getDescription() != null) {
            if (film.getDescription().length() > 200) {
                throw new ValidationException("Описание слишком длинное");
            }
            existingFilm.setDescription(film.getDescription());
        }

        if (film.getReleaseDate() != null) {
            if (film.getReleaseDate().isBefore(MIN_RELEASE_DATE)) {
                throw new ValidationException("Дата релиза не может быть раньше 28 декабря 1895 года");
            }
            existingFilm.setReleaseDate(film.getReleaseDate());
        }

        if (film.getDuration() != null) {
            if (film.getDuration() <= 0) {
                throw new ValidationException("Длительность должна быть положительной");
            }
            existingFilm.setDuration(film.getDuration());
        }

        log.info("Обновлён фильм {}", existingFilm);
        return existingFilm;
    }

    @Override
    public Collection<Film> getAll() {
        return films.values();
    }

    @Override
    public Film getById(int id) {
        Film film = films.get(id);
        if (film == null) {
            throw new NotFoundException("Фильм не найден");
        }
        return film;
    }
}
