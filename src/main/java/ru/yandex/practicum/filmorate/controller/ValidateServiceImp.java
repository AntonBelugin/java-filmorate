package ru.yandex.practicum.filmorate.controller;

import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import java.time.LocalDate;

@Service
public class ValidateServiceImp implements ValidateService {
    @Override
    public void validateUser(User user) {
        if (user.getEmail() == null || !user.getEmail().contains("@")) {
            System.out.println("Неправильный Имейл");
            throw new ValidationException("Неправильный Имейл");
        }
        if (user.getLogin() == null || user.getLogin().contains(" ")) {
            System.out.println("Неправильный Логин");
            throw new ValidationException("Неправильный Логин");
        }
        if (user.getBirthday().isAfter(LocalDate.now())) {
            System.out.println("Неправильно указана дата рождения");
            throw new ValidationException("Неправильно указана дата рождения");
        }
        if (user.getName() == null || user.getName().isEmpty()) {
            user.setName(user.getLogin());
        }
    }

    @Override
    public void validateUpdateUser(User user) {
        if (user.getId() == null) {
            throw new ValidationException("Id должен быть указан");
        }
       validateUser(user);
    }

    @Override
    public void validateFilm(Film film) {
        if (film.getName() == null || film.getName().isBlank()) {
            throw new ValidationException("Не указано название");
        }
        if (film.getDescription().length() > 200) {
            throw new ValidationException("Описание должно быть не более 200 символов");
        }
        if (film.getReleaseDate().isBefore(LocalDate.of(1895, 12, 28))) {
            throw new ValidationException("Неправильно указана дата релиза");
        }
        if (film.getDuration() <= 0) {
            throw new ValidationException("Продолжительность фильма меньше нуля");
        }
    }

    @Override
    public void validateUpdateFilm(Film upFilm) {
        if (upFilm.getId() == null) {
            throw new ValidationException("Id должен быть указан");
        }
        validateFilm(upFilm);
    }

}
