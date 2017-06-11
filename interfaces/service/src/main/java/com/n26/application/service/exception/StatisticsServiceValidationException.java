package com.n26.application.service.exception;

import java.util.List;

/**
 * Exception thrown in case of any service request validation error.
 *
 * Created by rahulpawar.
 */
public class StatisticsServiceValidationException extends Exception {

    private List<String> errors;

    /**
     * Constructor accepting message.
     *
     * @param message : descriptive message indicating cause.
     */
    public StatisticsServiceValidationException(final String message, final List<String> errors) {
        super(message);
        this.errors = errors;
    }

    public List<String> getErrors() {
        return errors;
    }
}
