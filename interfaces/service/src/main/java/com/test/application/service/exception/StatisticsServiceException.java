package com.test.application.service.exception;

/**
 * Service exception for statistics service.
 *
 * Created by rahulpawar.
 */
public class StatisticsServiceException extends Exception {

    /**
     * Constructor with descriptive message and cause of exception.
     *
     * @param message descriptive message.
     * @param cause cause.
     */
    public StatisticsServiceException(final String message, final Throwable cause) {
        super(message, cause);
    }
}
