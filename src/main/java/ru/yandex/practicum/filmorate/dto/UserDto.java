package ru.yandex.practicum.filmorate.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Data
@Getter
@Setter
public class UserDto {
  //  @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private long id;
    private String email;
    private String login;
    private String name;
    private LocalDate birthday;
}