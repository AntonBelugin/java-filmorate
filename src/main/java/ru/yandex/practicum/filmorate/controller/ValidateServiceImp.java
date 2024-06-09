package ru.yandex.practicum.filmorate.controller;

import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dto.CreateFilmRequest;
import ru.yandex.practicum.filmorate.dto.UpdateUserRequest;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import java.time.LocalDate;

@Service
public class ValidateServiceImp implements ValidateService {
    @Override
    public void validateUser(User user) {
        if (user.getEmail() == null || !user.getEmail().contains("@")) {
            throw new ValidationException("Неправильный Имейл");
        }
        if (user.getLogin() == null || user.getLogin().contains(" ")) {
            throw new ValidationException("Неправильный Логин");
        }
        if (user.getBirthday().isAfter(LocalDate.now())) {
            throw new ValidationException("Неправильно указана дата рождения");
        }
        if (user.getName() == null || user.getName().isEmpty()) {
            user.setName(user.getLogin());
        }
    }

    @Override
    public void validateUpdateUser(UpdateUserRequest request) {
        if (request.getId() == null) {
            throw new ValidationException("Id должен быть указан");
        }
        if (request.getEmail() == null || !request.getEmail().contains("@")) {
            throw new ValidationException("Неправильный Имейл");
        }
        if (request.getLogin() == null || request.getLogin().contains(" ")) {
            throw new ValidationException("Неправильный Логин");
        }
        if (request.getBirthday().isAfter(LocalDate.now())) {
            throw new ValidationException("Неправильно указана дата рождения");
        }
        if (request.getName() == null || request.getName().isEmpty()) {
            request.setName(request.getLogin());
        }

       // validateUser(upUser);
    }

    @Override
    public void validateFilm(CreateFilmRequest request) {
        if (request.getName() == null || request.getName().isBlank()) {
            throw new ValidationException("Не указано название");
        }
        if (request.getDescription().length() > 200) {
            throw new ValidationException("Описание должно быть не более 200 символов");
        }
        if (request.getReleaseDate().isBefore(LocalDate.of(1895, 12, 28))) {
            throw new ValidationException("Неправильно указана дата релиза");
        }
        if (request.getDuration() <= 0) {
            throw new ValidationException("Продолжительность фильма меньше нуля");
        }
    }

    @Override
    public void validateUpdateFilm(Film upFilm) {
        if (upFilm.getId() == null) {
            throw new ValidationException("Id должен быть указан");
        }
        //validateFilm(upFilm);
    }

}
