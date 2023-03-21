package ru.practicum.shareit.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.FORBIDDEN, reason = "No authorized to element")
public class NoAccessException extends RuntimeException {
    public NoAccessException(String message) {
        super(message);
    }
}
