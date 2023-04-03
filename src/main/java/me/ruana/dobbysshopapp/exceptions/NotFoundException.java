package me.ruana.dobbysshopapp.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.NOT_FOUND, reason = "Объект не найден")
public class NotFoundException extends RuntimeException{
    public NotFoundException(String message) {
        super(message);
    }
}
