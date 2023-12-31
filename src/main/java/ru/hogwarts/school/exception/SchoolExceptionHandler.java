package ru.hogwarts.school.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class SchoolExceptionHandler {
    private static final Logger LOG = LoggerFactory.getLogger(SchoolExceptionHandler.class);

    @ExceptionHandler(
            {
                    AvatarNotFoundException.class,
                    FacultyNotFoundException.class,
                    StudentNotFoundException.class
            }
    )
    public ResponseEntity<?> handleNotFound(RuntimeException e) {
        LOG.error(e.getMessage(), e);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
    }

    @ExceptionHandler(AvatarProcessingException.class)
    public ResponseEntity<?> handleInternalServerError() {
        LOG.error("Exception AvatarProcessingException was thrown");
        return ResponseEntity.internalServerError().build();
    }

}
