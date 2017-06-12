package com.test.application.service.exception;

/**
 * Exception thrown in case of transaction is older than 60 seconds.
 *
 * Created by rahulpawar.
 */
public class TransactionExpiredException extends Exception {

    /**
     * Constructor accepting message.
     *
     * @param message : descriptive message indicating cause.
     */
    public TransactionExpiredException(String message) {
        super(message);
    }
}
