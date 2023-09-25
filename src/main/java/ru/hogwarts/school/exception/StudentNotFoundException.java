package ru.hogwarts.school.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class StudentNotFoundException extends RuntimeException {
    private final long id;

    public StudentNotFoundException(long id) {
        this.id = id;
    }

    @Override
    public String getMessage() {
        return "Student with id = " + id + " not found!";
    }
}
