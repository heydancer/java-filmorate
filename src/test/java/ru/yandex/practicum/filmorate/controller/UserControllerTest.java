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
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;

import java.time.LocalDate;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;


@RunWith(SpringRunner.class)
@WebMvcTest(UserController.class)
class UserControllerTest {
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private MockMvc mvc;
    @MockBean
    private UserService service;

    @Test
    public void shouldReturnAllUsers() throws Exception {
        User user1 = new User(1, "user1@yandex.ru", "User1", "Sasha",
                LocalDate.of(1994, 4, 16));
        User user2 = new User(2, "user2@yandex.ru", "User2", "Dima",
                LocalDate.of(1990, 1, 10));

        when(service.getAll()).thenReturn(List.of(user1, user2));

        mvc.perform(get("/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[*].id", containsInAnyOrder(1, 2)))
                .andExpect(jsonPath("$[*].name", containsInAnyOrder("Sasha", "Dima")))
                .andExpect(jsonPath("$[*].email", containsInAnyOrder("user1@yandex.ru", "user2@yandex.ru")));
    }

    @Test
    void shouldReturnEmptyUsers() throws Exception {
        mvc.perform(get("/users"))
                .andExpect(status().isOk())
                .andExpect(content().json("[]"));
    }

    @Test
    void shouldCheckEmptyEmail() throws Exception {
        User user = new User(1, "", "User1", "Sasha",
                LocalDate.of(1994, 4, 16));

        String body = objectMapper.writeValueAsString(user);

        mvc.perform(post("/users").contentType(MediaType.APPLICATION_JSON).content(body))
                .andExpect(status().isBadRequest());
        mvc.perform(put("/users").contentType(MediaType.APPLICATION_JSON).content(body))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldCheckInvalidEmail() throws Exception {
        User user = new User(1, "invalid email", "User1", "Sasha",
                LocalDate.of(1994, 4, 16));

        String body = objectMapper.writeValueAsString(user);

        mvc.perform(post("/users").contentType(MediaType.APPLICATION_JSON).content(body))
                .andExpect(status().isBadRequest());
        mvc.perform(put("/users").contentType(MediaType.APPLICATION_JSON).content(body))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldCheckEmptyLogin() throws Exception {
        User user = new User(1, "user1@yandex.ru", "", "Sasha",
                LocalDate.of(1994, 4, 16));

        String body = objectMapper.writeValueAsString(user);

        mvc.perform(post("/users").contentType(MediaType.APPLICATION_JSON).content(body))
                .andExpect(status().isBadRequest());
        mvc.perform(put("/users").contentType(MediaType.APPLICATION_JSON).content(body))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldCheckEmptyName() throws Exception {
        User user = new User(1, "user1@yandex.ru", "user1", "",
                LocalDate.of(1994, 4, 16));

        String body = objectMapper.writeValueAsString(user);

        mvc.perform(post("/users").contentType(MediaType.APPLICATION_JSON).content(body))
                .andExpect(status().isOk());
        mvc.perform(put("/users").contentType(MediaType.APPLICATION_JSON).content(body))
                .andExpect(status().isOk());

    }

    @Test
    void shouldCheckBirthday() throws Exception {
        User user = new User(1, "user1@yandex.ru", "user1", "Sasha",
                LocalDate.of(3333, 3, 3));

        String body = objectMapper.writeValueAsString(user);

        mvc.perform(post("/users").contentType(MediaType.APPLICATION_JSON).content(body))
                .andExpect(status().isBadRequest());
        mvc.perform(put("/users").contentType(MediaType.APPLICATION_JSON).content(body))
                .andExpect(status().isBadRequest());
    }
}