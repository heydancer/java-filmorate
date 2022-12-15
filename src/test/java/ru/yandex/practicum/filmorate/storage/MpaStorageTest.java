package ru.yandex.practicum.filmorate.storage;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.storage.mpa.MpaDbStorage;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class MpaStorageTest {

    private final MpaDbStorage mpaDbStorage;

    @Test
    public void shouldReturnMpaById() {
        assertThat(mpaDbStorage.getById(1).getName()).isEqualTo("G");
        assertThat(mpaDbStorage.getById(2).getName()).isEqualTo("PG");
        assertThat(mpaDbStorage.getById(3).getName()).isEqualTo("PG-13");
    }

    @Test
    public void shouldReturnAllMpa() {
        assertThat(mpaDbStorage.getAll()).isNotNull();
        assertThat(mpaDbStorage.getAll()).hasSize(5);
    }
}
