package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.GenreDbStorage;
import java.util.Collection;

@Service
public class GenreService {
    private final GenreDbStorage genreDbStorage;

    @Autowired
    public GenreService(GenreDbStorage genreDbStorage) {
        this.genreDbStorage = genreDbStorage;
    }

    public Collection<Genre> getAll() {
        return genreDbStorage.findAll();
    }

    public Genre getById(int id) {
        Collection<Genre> genres = genreDbStorage.findAll();
        if (id > genres.size()) {
                throw new NotFoundException("Жанра с id " + id + " не существует");
            }
        if (genreDbStorage.findById(id).isPresent()) {
            return genreDbStorage.findById(id).get();
        } else {
            throw new NotFoundException("Жанр с id " + id + " не найден");
        }
    }
}
