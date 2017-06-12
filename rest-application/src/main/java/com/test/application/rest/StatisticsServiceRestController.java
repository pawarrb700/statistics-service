package com.test.application.rest;

import javax.ws.rs.core.MediaType;

import com.test.application.service.StatisticsService;
import com.test.application.service.exception.StatisticsServiceException;
import com.test.application.service.exception.StatisticsServiceValidationException;
import com.test.application.service.exception.TransactionExpiredException;
import com.test.application.service.model.RecordTransactionRequest;
import com.test.application.service.model.TransactionsStatisticsResponse;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;

/**
 * Controller for statistics service.
 *
 * Created by rahulpawar.
 */
@RestController
@ComponentScan(basePackages = "com.test.application  ")
public class StatisticsServiceRestController {

    private static final Logger logger = LogManager.getLogger(StatisticsServiceRestController.class);

    @Autowired
    private StatisticsService statisticsService;

    private static final String RECORD_TRANSACTIONS = "/transactions";

    private static final String GET_STATISTICS = "/statistics";

    private static final String RESPONSE_STATUS = "STATUS";

    private static final String RESPONSE_SUCCESS = "SUCCESS";

    private static final String RESPONSE_ERROR = "ERROR";

    @PostMapping(value = RECORD_TRANSACTIONS, consumes = MediaType.APPLICATION_JSON)
    public ResponseEntity<Object> recordTransactions(@RequestBody final RecordTransactionRequest request) {
        if(logger.isDebugEnabled()) {
            logger.debug("Record transaction request : {}", request);
        }
        MultiValueMap<String, String> responseHeader = new HttpHeaders();
        HttpStatus status = HttpStatus.CREATED;
        try {
            getStatisticsService().recordTransactions(request);
        } catch(TransactionExpiredException e) {
            responseHeader.add(RESPONSE_STATUS, RESPONSE_ERROR);
            status = HttpStatus.NO_CONTENT;
        } catch (StatisticsServiceValidationException e) {
            responseHeader.add(RESPONSE_STATUS, RESPONSE_ERROR);
            status = HttpStatus.BAD_REQUEST;
        } catch (StatisticsServiceException e) {
            responseHeader.add(RESPONSE_STATUS, RESPONSE_ERROR);
            status = HttpStatus.INTERNAL_SERVER_ERROR;
        }
        responseHeader.add(RESPONSE_STATUS, RESPONSE_SUCCESS);
        return new ResponseEntity<Object>(responseHeader, status);
    }

    @GetMapping(value = GET_STATISTICS, consumes = MediaType.APPLICATION_JSON, produces = MediaType.APPLICATION_JSON)
    public ResponseEntity<Object> getTransactionsStatistics() {
        if(logger.isDebugEnabled()) {
            logger.debug("Get statistics of transactions.");
        }
        MultiValueMap<String, String> responseHeader = new HttpHeaders();
        TransactionsStatisticsResponse response = null;
        HttpStatus status = HttpStatus.CREATED;
        try {
            response = getStatisticsService().getStatistics();
        } catch (StatisticsServiceException e) {
            responseHeader.add(RESPONSE_STATUS, RESPONSE_ERROR);
            status = HttpStatus.INTERNAL_SERVER_ERROR;
        }
        responseHeader.add(RESPONSE_STATUS, RESPONSE_SUCCESS);
        return new ResponseEntity<Object>(response, responseHeader, status);
    }

    public StatisticsService getStatisticsService() {
        return statisticsService;
    }
}
