package ru.yandex.practicum.filmorate.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.*;
import java.time.LocalDate;

@Data
@AllArgsConstructor
public class Film {

    private int id;

    @NotNull(message = "Name cannot be null")
    @NotBlank(message = "Name cannot be empty")
    private String name;

    @NotNull(message = "Description cannot be null")
    @Size(max = 200, message = "Length of the description should not exceed 200 characters")
    private String description;

    @NotNull(message = "Release Date cannot be null")
    private LocalDate releaseDate;

    @NotNull(message = "Duration cannot be null")
    @Positive(message = "Duration should be positive")
    private Integer duration;
}
