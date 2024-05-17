package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.storage.FilmStorage;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/films")
@Slf4j
public class FilmController {
    private FilmService filmService;
    //private Map<Long, Film> films = new HashMap<>();
    private ValidateService validateService;
    private Long currentId = 0L;
    private FilmStorage filmStorage;

    @Autowired
    public FilmController(ValidateService validateService, FilmStorage filmStorage, FilmService filmService) {
        this.validateService = validateService;
        this.filmStorage = filmStorage;
        this.filmService = filmService;
    }

    @PostMapping
    public Film create(@RequestBody Film film) {
        log.info("==> POST /films");
        validateService.validateFilm(film);
        film.setId(getNextId());
        //films.put(film.getId(), film);
        filmStorage.add(film);
        log.info("<== POST /films {}", film);
        return film;
    }

    @PutMapping
    public Film update(@RequestBody Film upFilm) {
        log.info("==> PUT /films");
        validateService.validateUpdateFilm(upFilm);
        if (!filmStorage.getFilms().containsKey(upFilm.getId())) {
            throw new ValidationException("Неверный Id");
        }
        filmStorage.update(upFilm);
        log.info("<== PUT /films {}", upFilm);
        return upFilm;
    }

    @GetMapping
    public Collection<Film> findAll() {
        log.info("<== GET /films {}", filmStorage.getAll());
        return filmStorage.getAll();
    }

    @PutMapping("/films/{id}/like/{userId}")
    public void addLike(@PathVariable long id,  @PathVariable long userId) {
        //log.info("==> PUT /films");
        filmService.addLike(id, userId);
    }

    @DeleteMapping("/films/{id}/like/{userId}")
    public void deleteLike(@PathVariable long id,  @PathVariable long userId) {
        //log.info("==> PUT /films");
        filmService.deleteLike(id, userId);
    }

    @GetMapping("/films/popular?count={count}")
    public Collection<Film> mostLike(@RequestParam(defaultValue = "10") int count) {
        //log.info("==> PUT /films");
        return filmService.mostLike(count);
    }

    private long getNextId() {
        return ++currentId;
    }
}