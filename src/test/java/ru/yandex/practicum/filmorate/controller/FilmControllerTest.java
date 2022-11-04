package ru.yandex.practicum.filmorate.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;

import java.time.LocalDate;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.containsInAnyOrder;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

@RunWith(SpringRunner.class)
@WebMvcTest(FilmController.class)
class FilmControllerTest {
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private MockMvc mvc;
    @MockBean
    private FilmService service;

    @Test
    public void shouldReturnAllMovies() throws Exception {
        Film film1 = new Film(1, "Film1", "Description1",
                LocalDate.of(1998, 9, 18), 98);
        Film film2 = new Film(2, "Film2", "Description2",
                LocalDate.of(1988, 7, 12), 133);

        when(service.getAll()).thenReturn(List.of(film1, film2));

        mvc.perform(get("/films"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[*].id", containsInAnyOrder(1, 2)))
                .andExpect(jsonPath("$[*].name", containsInAnyOrder("Film1", "Film2")));
    }

    @Test
    void shouldReturnEmptyFilms() throws Exception {
        mvc.perform(get("/films"))
                .andExpect(status().isOk())
                .andExpect(content().json("[]"));
    }

    @Test
    public void shouldCheckEmptyName() throws Exception {
        Film film = new Film(1, "", "Description",
                LocalDate.of(1998, 9, 18), 98);

        String body = objectMapper.writeValueAsString(film);

        mvc.perform(post("/films").contentType(MediaType.APPLICATION_JSON).content(body))
                .andExpect(status().isBadRequest());
        mvc.perform(put("/films").contentType(MediaType.APPLICATION_JSON).content(body))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void shouldCheckLengthOfTheDescription() throws Exception {
        Film film = new Film(1, "Film", "Description".repeat(200),
                LocalDate.of(1998, 9, 18), 98);

        String body = objectMapper.writeValueAsString(film);

        mvc.perform(post("/films").contentType(MediaType.APPLICATION_JSON).content(body))
                .andExpect(status().isBadRequest());
        mvc.perform(put("/films").contentType(MediaType.APPLICATION_JSON).content(body))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void shouldCheckDateOfRelease() throws Exception {
        Film film = new Film(1, "Film", "Description",
                LocalDate.of(1000, 1, 1), 1);

        String body = objectMapper.writeValueAsString(film);

        when(service.validate(film)).thenThrow(ValidationException.class);

        mvc.perform(post("/films").content(body).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
        mvc.perform(post("/films").content(body).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void shouldCheckDuration() throws Exception {
        Film film = new Film(1, "Film", "Description",
                LocalDate.of(1998, 9, 18), -9999);

        String body = objectMapper.writeValueAsString(film);

        mvc.perform(post("/films").content(body).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
        mvc.perform(post("/films").content(body).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }
}