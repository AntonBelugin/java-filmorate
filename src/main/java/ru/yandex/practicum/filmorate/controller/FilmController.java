package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;
import java.util.Collection;

@RestController
@RequestMapping("/films")
@Slf4j
@RequiredArgsConstructor
@Component
public class FilmController {
    private final FilmService filmService;
    private final ValidateService validateService;
    private Long currentId = 0L;


    @PostMapping
    public Film create(@RequestBody Film film) {
        log.info("==> POST /films");
        validateService.validateFilm(film);
        film.setId(getNextId());
        filmService.create(film);
        log.info("<== POST /films {}", film);
        return film;
    }

    @PutMapping
    public Film update(@RequestBody Film upFilm) {
        log.info("==> PUT /films");
        validateService.validateUpdateFilm(upFilm);
        filmService.update(upFilm);
        log.info("<== PUT /films {}", upFilm);
        return upFilm;
    }

    @GetMapping
    public Collection<Film> findAll() {
        log.info("<== GET /films {}", filmService.findAll());
        return filmService.findAll();
    }

    @PutMapping("/{id}/like/{userId}")
    public void addLike(@PathVariable long id,  @PathVariable long userId) {
        log.info("==> PUT /films/{id}/like/{userId}");
        filmService.addLike(id, userId);
    }

    @DeleteMapping("/{id}/like/{userId}")
    public void deleteLike(@PathVariable long id,  @PathVariable long userId) {
        log.info("==> DELETE /films/{id}/like/{userId}");
        filmService.deleteLike(id, userId);
    }

    @GetMapping("/popular")
    public Collection<Film> mostLike(@RequestParam(defaultValue = "10") int count) {
        log.info("==> GET /films/popular?count=");
        return filmService.mostLike(count);
    }

    private long getNextId() {
        return ++currentId;
    }

}