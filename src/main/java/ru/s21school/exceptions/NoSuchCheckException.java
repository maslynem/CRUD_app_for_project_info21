package ru.s21school.exceptions;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class NoSuchCheckException extends RuntimeException {

    public NoSuchCheckException(String message) {
        super(message);
    }

}
