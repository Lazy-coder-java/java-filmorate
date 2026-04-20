package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.controller.UserController;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

public class UserValidationTest {

    private final UserController controller = new UserController();

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
    void shouldFailOnInvalidEmail() {
        User user = new User();
        user.setEmail("wrongEmail");
        user.setLogin("login");
        user.setBirthday(LocalDate.of(2000, 1, 1));

        ValidationException ex = assertThrows(
                ValidationException.class,
                () -> controller.create(user)
        );

        assertEquals("Некорректный email", ex.getMessage());
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
