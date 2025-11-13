package com.ateam.calmate.event.command.adapter;

/**
 * 포인트가 부족할 때 발생하는 예외
 */
public class InsufficientPointsException extends RuntimeException {
    public InsufficientPointsException(String message) {
        super(message);
    }
}