package com.n26.application.service.validator;

import com.n26.application.service.exception.StatisticsServiceValidationException;
import com.n26.application.service.model.RecordTransactionRequest;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.n26.application.service.StatisticsService;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * Validator class to validate api's served by {@link StatisticsService}
 *
 * Created by rahulpawar.
 */
public class StatisticsServiceValidator {

    private static final Logger logger = LogManager.getLogger(StatisticsServiceValidator.class);

    /**
     * API to validate {@link com.n26.application.service.model.RecordTransactionRequest}
     *
     * @throws StatisticsServiceValidationException
     */
    public static void validateRecordTransactionRequest(final RecordTransactionRequest request) throws StatisticsServiceValidationException {
        List<String> errors = new ArrayList<>();
        if(request.getAmount()==null || request.getAmount().compareTo(new Double(0))==0) {
            errors.add("Amount cannot be zero or null.");
        }
        if(request.getTimestamp()==null) {
            errors.add("Timestamp cannot be null.");
        }

        if(!errors.isEmpty()) {
            logger.error("RecordTransactionRequest has failed validation. Error : {}", errors);
            throw new StatisticsServiceValidationException("RecordTransactionRequest has failed validation.", errors);
        }
    }

    public static boolean isTransactionExpired(final Long currentTime, final Long transactionTime) {
        if(transactionTime > currentTime) {
            return true;
        } else if((currentTime/1000 - transactionTime/1000) > 60) {
            return true;
        }
        return false;
    }
}
