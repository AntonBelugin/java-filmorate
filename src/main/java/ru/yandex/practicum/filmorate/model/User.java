package ru.yandex.practicum.filmorate.model;

import lombok.*;

import java.time.LocalDate;
import java.util.Date;
import java.util.Locale;
import java.util.Set;

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
    //private Set<Integer> friends;
   // private Set<Integer> requestFriends;
}
