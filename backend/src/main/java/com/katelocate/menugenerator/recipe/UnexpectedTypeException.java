package com.katelocate.menugenerator.recipe;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;


@ResponseStatus(HttpStatus.NOT_FOUND)
public class UnexpectedTypeException extends RuntimeException {
    public UnexpectedTypeException() {
        super("Type not found. Try again.");
    }
}
