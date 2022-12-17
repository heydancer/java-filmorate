package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.mpa.MpaStorage;

import java.util.List;

@Service
public class MpaService {

    private final MpaStorage mpaStorage;

    @Autowired
    public MpaService(MpaStorage storage) {
        this.mpaStorage = storage;
    }

    public List<Mpa> getAll() {
        return mpaStorage.getAll();
    }

    public Mpa getById(int mpaId) {
        mpaStorage.checkMpa(mpaId);

        return mpaStorage.getById(mpaId);
    }
}
