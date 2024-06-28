package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.MpaDbStorage;
import java.util.Collection;
import java.util.Optional;

@Service
public class MpaService {
    private final MpaDbStorage mpaDbStorage;

    @Autowired
    public MpaService(MpaDbStorage mpaDbStorage) {
        this.mpaDbStorage = mpaDbStorage;
    }

    public Collection<Mpa> getAll() {
        return mpaDbStorage.findAll();
    }

    public Mpa getById(int id) {
        Optional<Mpa> optionalMpa = mpaDbStorage.findById(id);
        if (optionalMpa.isPresent()) {
            return optionalMpa.get();
        } else {
            throw new NotFoundException("Рейтинг с id " + id + " не найден");
        }
    }
}