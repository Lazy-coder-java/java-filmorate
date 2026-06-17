package ru.yandex.practicum.filmorate.storage.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Component
@Slf4j
public class InMemoryUserStorage implements UserStorage {

    private final Map<Integer, User> users = new HashMap<>();
    private int idCounter = 1;

    @Override
    public User create(User user) {
        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
        user.setId(idCounter++);
        users.put(user.getId(), user);
        log.info("Создан пользователь {}", user);
        return user;
    }

    @Override
    public User update(User user) {
        if (user.getId() == null || !users.containsKey(user.getId())) {
            throw new NotFoundException("Пользователь не найден");
        }

        User existingUser = users.get(user.getId());

        if (user.getEmail() != null) {
            if (user.getEmail().isBlank() || !user.getEmail().contains("@")) {
                throw new ValidationException("Некорректный email");
            }
            existingUser.setEmail(user.getEmail());
        }

        if (user.getLogin() != null) {
            if (user.getLogin().isBlank() || user.getLogin().contains(" ")) {
                throw new ValidationException("Логин не может быть пустым и не должен содержать пробелы");
            }
            existingUser.setLogin(user.getLogin());
        }

        if (user.getName() != null) {
            if (user.getName().isBlank()) {
                existingUser.setName(existingUser.getLogin());
            } else {
                existingUser.setName(user.getName());
            }
        }

        if (user.getBirthday() != null) {
            if (user.getBirthday().isAfter(LocalDate.now())) {
                throw new ValidationException("Дата рождения не может быть в будущем");
            }
            existingUser.setBirthday(user.getBirthday());
        }

        log.info("Обновлён пользователь {}", existingUser);
        return existingUser;
    }

    @Override
    public Collection<User> getAll() {
        return users.values();
    }

    @Override
    public User getById(int id) {
        User user = users.get(id);
        if (user == null) {
            throw new NotFoundException("Пользователь не найден");
        }
        return user;
    }
}
