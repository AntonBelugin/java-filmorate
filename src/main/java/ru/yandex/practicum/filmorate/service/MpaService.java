package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.MpaDbStorage;
import java.util.Collection;

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
        Collection<Mpa> mpa = mpaDbStorage.findAll();
        if (id > mpa.size()) {
            throw new NotFoundException("Рейтинга с id " + id + " не существует");
        }
        if (mpaDbStorage.findById(id).isPresent()) {
            return mpaDbStorage.findById(id).get();
        } else {
            throw new NotFoundException("Рейтинг с id " + id + " не найден");
        }
    }
}