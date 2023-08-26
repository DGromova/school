package ru.hogwarts.school.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class FacultyNotFoundException extends RuntimeException {

    private final long id;

    public FacultyNotFoundException(long id) {
        this.id = id;
    }

    @Override
    public String getMessage() {
        return "Факультет с id = " + id + " не найден!";
    }

}
