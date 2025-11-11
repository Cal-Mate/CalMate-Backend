package com.ateam.calmate.event.command.service.point;

public class InsufficientPointException extends RuntimeException {
    public InsufficientPointException(String msg) { super(msg); }
}
