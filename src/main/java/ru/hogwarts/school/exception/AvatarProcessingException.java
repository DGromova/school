package ru.hogwarts.school.exception;

public class AvatarProcessingException extends RuntimeException {
    public AvatarProcessingException() {
    }

    public AvatarProcessingException(String message) {
        super(message);
    }

    public AvatarProcessingException(String message, Throwable cause) {
        super(message, cause);
    }

    public AvatarProcessingException(Throwable cause) {
        super(cause);
    }

    public AvatarProcessingException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
