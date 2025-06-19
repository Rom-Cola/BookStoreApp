package com.loievroman.bookstoreapp.exception;

public class OrderCreateException extends RuntimeException {
    public OrderCreateException(String message) {
        super(message);
    }
}
