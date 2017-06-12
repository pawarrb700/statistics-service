package com.test.application.exception;

/**
 * Thrown if any errors comes in while persisting transactions.
 *
 * Created by rahulpawar.
 */
public class StatisticsRepositoryPersistanceException extends Exception {

    /**
     * Constructor with message and cause of exception.
     *
     * @param message descriptive message.
     * @param cause cause of exception.
     */
    public StatisticsRepositoryPersistanceException(final String message, final Throwable cause) {
        super(message, cause);
    }
}
