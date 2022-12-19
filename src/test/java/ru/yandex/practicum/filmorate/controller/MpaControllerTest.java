package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.util.List;

import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(MpaController.class)
class MpaControllerTest {

    @Autowired
    private MockMvc mvc;
    @MockBean
    private MpaController service;

    @Test
    public void shouldReturnGetById() throws Exception {
        Mpa mpa = Mpa.builder()
                .id(1)
                .name("PG")
                .build();

        when(service.getById(1)).thenReturn(mpa);
        mvc.perform(get("/mpa/{id}", 1))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("PG"));
    }

    @Test
    public void shouldReturnAllMpa() throws Exception {
        Mpa mpa = Mpa.builder()
                .id(1)
                .name("PG")
                .build();

        Mpa mpa2 = Mpa.builder()
                .id(2)
                .name("PG-13")
                .build();


        when(service.getAllMpa()).thenReturn(List.of(mpa, mpa2));
        mvc.perform(get("/mpa"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[*].id", containsInAnyOrder(1, 2)))
                .andExpect(jsonPath("$[*].name", containsInAnyOrder("PG", "PG-13")));
    }
}
