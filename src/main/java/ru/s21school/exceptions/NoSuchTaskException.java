package ru.s21school.exceptions;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class NoSuchTaskException extends RuntimeException {

    public NoSuchTaskException(String message) {
        super(message);
    }

}