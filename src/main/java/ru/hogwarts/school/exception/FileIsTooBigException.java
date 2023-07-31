package ru.hogwarts.school.exception;

public class FileIsTooBigException extends RuntimeException {
    public FileIsTooBigException() {
    }

    public FileIsTooBigException(String message) {
        super(message);
    }

    public FileIsTooBigException(String message, Throwable cause) {
        super(message, cause);
    }

    public FileIsTooBigException(Throwable cause) {
        super(cause);
    }

    public FileIsTooBigException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
