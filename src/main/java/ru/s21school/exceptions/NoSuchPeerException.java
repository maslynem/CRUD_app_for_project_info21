package ru.s21school.exceptions;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class NoSuchPeerException extends RuntimeException {

    public NoSuchPeerException(String message) {
        super(message);
    }

}
