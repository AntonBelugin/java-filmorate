package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.GenreDbStorage;
import java.util.Collection;
import java.util.Optional;

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
        Optional<Genre> genre = genreDbStorage.findById(id);
        if (genre.isPresent()) {
            return genre.get();
        } else {
            throw new NotFoundException("Жанр с id " + id + " не найден");
        }
    }
}
