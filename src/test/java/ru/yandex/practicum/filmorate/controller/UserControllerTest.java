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
        User user1 = User.builder()
                .id(1)
                .email("user1@yandex.ru")
                .login("hey dancer")
                .name("Sasha")
                .birthday(LocalDate.of(1994, 4, 16))
                .build();
        User user2 = User.builder()
                .id(2)
                .email("user2@yandex.ru")
                .login("User2")
                .name("Dima")
                .birthday(LocalDate.of(1990, 1, 10))
                .build();

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
        User user = User.builder()
                .id(1)
                .email("")
                .login("User1")
                .name("Sasha")
                .birthday(LocalDate.of(1994, 4, 16))
                .build();

        String body = objectMapper.writeValueAsString(user);

        mvc.perform(post("/users").contentType(MediaType.APPLICATION_JSON).content(body))
                .andExpect(status().isBadRequest());
        mvc.perform(put("/users").contentType(MediaType.APPLICATION_JSON).content(body))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldCheckInvalidEmail() throws Exception {
        User user = User.builder()
                .id(1)
                .email("Invalid email")
                .login("User1")
                .name("Sasha")
                .birthday(LocalDate.of(1994, 4, 16))
                .build();

        String body = objectMapper.writeValueAsString(user);

        mvc.perform(post("/users").contentType(MediaType.APPLICATION_JSON).content(body))
                .andExpect(status().isBadRequest());
        mvc.perform(put("/users").contentType(MediaType.APPLICATION_JSON).content(body))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldCheckEmptyLogin() throws Exception {
        User user = User.builder()
                .id(1)
                .email("user@yandex.ru")
                .login("")
                .name("Sasha")
                .birthday(LocalDate.of(1994, 4, 16))
                .build();

        String body = objectMapper.writeValueAsString(user);

        mvc.perform(post("/users").contentType(MediaType.APPLICATION_JSON).content(body))
                .andExpect(status().isBadRequest());
        mvc.perform(put("/users").contentType(MediaType.APPLICATION_JSON).content(body))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldCheckEmptyName() throws Exception {
        User user = User.builder()
                .id(1)
                .email("user@yandex.ru")
                .login("hey dancer")
                .name("")
                .birthday(LocalDate.of(1994, 4, 16))
                .build();

        String body = objectMapper.writeValueAsString(user);

        mvc.perform(post("/users").contentType(MediaType.APPLICATION_JSON).content(body))
                .andExpect(status().isOk());
        mvc.perform(put("/users").contentType(MediaType.APPLICATION_JSON).content(body))
                .andExpect(status().isOk());

    }

    @Test
    void shouldCheckBirthday() throws Exception {
        User user = User.builder()
                .id(1)
                .email("user@yandex.ru")
                .login("hey dancer")
                .name("Sasha")
                .birthday(LocalDate.of(3333, 3, 3))
                .build();

        String body = objectMapper.writeValueAsString(user);

        mvc.perform(post("/users").contentType(MediaType.APPLICATION_JSON).content(body))
                .andExpect(status().isBadRequest());
        mvc.perform(put("/users").contentType(MediaType.APPLICATION_JSON).content(body))
                .andExpect(status().isBadRequest());
    }
}