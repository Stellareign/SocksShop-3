package me.ruana.dobbysshopapp.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.BAD_REQUEST, reason = "Неверный запрос")
public class InvalidResponseStatusException extends RuntimeException {

    public InvalidResponseStatusException(String message) {
        super(message);
    }
}
