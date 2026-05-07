package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.controller.UserController;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;
import ru.yandex.practicum.filmorate.storage.user.InMemoryUserStorage;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

public class UserValidationTest {

    private final InMemoryUserStorage userStorage =
            new InMemoryUserStorage();

    private final UserService userService =
            new UserService(userStorage);

    private final UserController controller =
            new UserController(userStorage, userService);

    @Test
    void shouldCreateValidUser() {

        User user = new User();
        user.setEmail("test@mail.com");
        user.setLogin("login");
        user.setBirthday(LocalDate.of(2000, 1, 1));

        User created = controller.create(user);

        assertNotNull(created.getId());
    }

    @Test
    void shouldReplaceEmptyNameWithLogin() {

        User user = new User();
        user.setEmail("test@mail.com");
        user.setLogin("login");
        user.setBirthday(LocalDate.of(2000, 1, 1));

        User created = controller.create(user);

        assertEquals("login", created.getName());
    }
}
