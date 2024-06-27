package ru.yandex.practicum.filmorate.model;

import lombok.*;
import java.time.LocalDate;

@Data
@Getter
@Setter
@ToString
public class User {
    private Long id;
    private String login;
    private String name;
    private String email;
    private LocalDate birthday;
}
