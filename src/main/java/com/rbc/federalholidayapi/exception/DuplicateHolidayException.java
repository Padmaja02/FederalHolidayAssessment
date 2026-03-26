package com.rbc.federalholidayapi.exception;

public class DuplicateHolidayException extends RuntimeException {
    public DuplicateHolidayException(String message) {
        super(message);
    }
}