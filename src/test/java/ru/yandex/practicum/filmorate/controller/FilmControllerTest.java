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
        Film film1 = Film.builder()
                .id(1)
                .name("Film1")
                .description("Description1")
                .releaseDate(LocalDate.of(1998, 9, 18))
                .duration(98)
                .build();

        Film film2 = Film.builder()
                .id(2)
                .name("Film2")
                .description("Decription2")
                .releaseDate(LocalDate.of(1988, 7, 12))
                .duration(133)
                .build();


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
        Film film = Film.builder()
                .id(1)
                .name("")
                .description("Description")
                .releaseDate(LocalDate.of(1998, 9, 18))
                .duration(98)
                .build();

        String body = objectMapper.writeValueAsString(film);

        mvc.perform(post("/films").contentType(MediaType.APPLICATION_JSON).content(body))
                .andExpect(status().isBadRequest());
        mvc.perform(put("/films").contentType(MediaType.APPLICATION_JSON).content(body))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void shouldCheckLengthOfTheDescription() throws Exception {
        Film film = Film.builder()
                .id(1)
                .name("Film")
                .description("Description".repeat(200))
                .releaseDate(LocalDate.of(1998, 9, 18))
                .duration(98)
                .build();

        String body = objectMapper.writeValueAsString(film);

        mvc.perform(post("/films").contentType(MediaType.APPLICATION_JSON).content(body))
                .andExpect(status().isBadRequest());
        mvc.perform(put("/films").contentType(MediaType.APPLICATION_JSON).content(body))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void shouldCheckDateOfRelease() throws Exception {
        Film film = Film.builder()
                .id(1)
                .name("Film")
                .description("Description")
                .releaseDate(LocalDate.of(1000, 1, 1))
                .duration(98)
                .build();

        String body = objectMapper.writeValueAsString(film);

        when(service.validate(film)).thenThrow(ValidationException.class);

        mvc.perform(post("/films").content(body).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isInternalServerError());
        mvc.perform(post("/films").content(body).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isInternalServerError());
    }

    @Test
    public void shouldCheckDuration() throws Exception {
        Film film = Film.builder()
                .id(1)
                .name("Film")
                .description("Description")
                .releaseDate(LocalDate.of(1998, 9, 18))
                .duration(-9999)
                .build();

        String body = objectMapper.writeValueAsString(film);

        mvc.perform(post("/films").content(body).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
        mvc.perform(post("/films").content(body).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }
}