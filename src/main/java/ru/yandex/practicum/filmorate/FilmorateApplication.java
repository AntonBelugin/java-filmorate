package ru.yandex.practicum.filmorate;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import ru.yandex.practicum.filmorate.controller.ValidateService;
import ru.yandex.practicum.filmorate.controller.ValidateServiceImp;

@SpringBootApplication
public class FilmorateApplication {
	public static void main(String[] args) {
		ValidateService validateService = new ValidateServiceImp();
		SpringApplication.run(FilmorateApplication.class, args);
	}

}
