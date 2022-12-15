package ru.yandex.practicum.filmorate.storage.mpa;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
public class MpaDbStorage implements MpaStorage {
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public MpaDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Mpa getById(int mpaId) {
        try {
            return jdbcTemplate.queryForObject(MpaSqlQueries.GET_MPA, this::mapRow, mpaId);
        } catch (DataAccessException exception) {
            return null;
        }
    }

    @Override
    public List<Mpa> getAll() {
        return jdbcTemplate.query(MpaSqlQueries.GET_ALL_MPA, this::mapRow);
    }

    @Override
    public void checkMpaInDb(int mpaId) {
        if (getById(mpaId) == null) {
            throw new NotFoundException("MPA with ID: " + mpaId + " not found");
        }
    }

    private Mpa mapRow(ResultSet rs, int rowNum) throws SQLException {
        return Mpa.builder()
                .id(rs.getInt("mpa_id"))
                .name(rs.getString("mpa_name"))
                .build();
    }
}
