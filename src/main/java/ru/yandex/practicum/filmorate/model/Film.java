package ru.yandex.practicum.filmorate.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import javax.validation.constraints.Positive;
import java.time.LocalDate;
import java.util.List;
import java.util.Set;

@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Film {

    int id;

    @NotNull(message = "Name cannot be null")
    @NotBlank(message = "Name cannot be empty")
    String name;

    @NotNull(message = "Description cannot be null")
    @Size(max = 200, message = "Length of the description should not exceed 200 characters")
    String description;

    @NotNull(message = "Release Date cannot be null")
    LocalDate releaseDate;

    @NotNull(message = "Duration cannot be null")
    @Positive(message = "Duration should be positive")
    Integer duration;

    Mpa mpa;

    List<Genre> genres;

    @JsonIgnore
    Set<Integer> likes;
}
