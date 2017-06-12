package com.test.application.service.validator;

import com.test.application.service.exception.StatisticsServiceValidationException;
import com.test.application.service.model.RecordTransactionRequest;
import com.test.application.service.StatisticsService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

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
     * API to validate {@link RecordTransactionRequest}
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
