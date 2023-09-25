package ru.hogwarts.school.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class AvatarNotFoundException extends RuntimeException {
    private final long id;

    public AvatarNotFoundException(long id) {
        this.id = id;
    }

    @Override
    public String getMessage() {
        return "Avatar by id = " + id + " not found!";
    }
}
