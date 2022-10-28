package ru.yandex.practicum.filmorate.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.*;
import java.time.LocalDate;

@Data
@AllArgsConstructor
public class User {
    private static final String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@" +
            "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

    private int id;

    @NotNull(message = "Email cannot be null")
    @NotBlank(message = "Email cannot be empty")
    @Email(message = "Email is not valid", regexp = EMAIL_PATTERN)
    private String email;

    @NotNull(message = "Login cannot be null")
    @NotBlank(message = "Login cannot be empty")
    private String login;

    private String name;

    @NotNull(message = "Birthday cannot be null")
    @PastOrPresent(message = "Birthday cannot be in future")
    private LocalDate birthday;
}
