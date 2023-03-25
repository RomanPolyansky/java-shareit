package ru.practicum.shareit.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.BAD_REQUEST, reason = "Illigal operation")
public class IlligalRequestException extends RuntimeException {
    public IlligalRequestException(String message) {
        super(message);
    }
}
