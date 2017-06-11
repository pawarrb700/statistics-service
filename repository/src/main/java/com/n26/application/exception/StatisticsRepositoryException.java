package com.n26.application.exception;

/**
 * Repository exception for mapping all general repository read exceptions.
 *
 * Created by rahulpawar.
 */
public class StatisticsRepositoryException extends Exception {

    /**
     * Constructor with message and cause of exception.
     *
     * @param message descriptive message.
     * @param cause cause of exception.
     */
    public StatisticsRepositoryException(final String message, final Throwable cause) {
        super(message, cause);
    }
}
