package ru.yandex.practicum.filmorate.storage.film;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Component
@Slf4j
public class InMemoryFilmStorage implements FilmStorage {

    private final Map<Integer, Film> films = new HashMap<>();
    private int idCounter = 1;

    @Override
    public Film create(Film film) {

        film.setId(idCounter++);
        films.put(film.getId(), film);

        log.info("Создан фильм {}", film);

        return film;
    }

    @Override
    public Film update(Film film) {

        if (film.getId() == null ||
                !films.containsKey(film.getId())) {

            throw new NotFoundException("Фильм не найден");
        }

        films.put(film.getId(), film);

        log.info("Обновлен фильм {}", film);

        return film;
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
