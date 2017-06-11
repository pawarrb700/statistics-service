package com.n26.application.rest;

import javax.ws.rs.core.MediaType;

import com.n26.application.service.StatisticsService;
import com.n26.application.service.exception.StatisticsServiceException;
import com.n26.application.service.exception.StatisticsServiceValidationException;
import com.n26.application.service.exception.TransactionExpiredException;
import com.n26.application.service.model.RecordTransactionRequest;
import com.n26.application.service.model.TransactionsStatisticsResponse;
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
@ComponentScan(basePackages = "com.n26.application  ")
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
        try {
            getStatisticsService().recordTransactions(request);
        } catch(TransactionExpiredException e) {
            responseHeader.add(RESPONSE_STATUS, RESPONSE_ERROR);
            return new ResponseEntity<Object>(responseHeader, HttpStatus.NO_CONTENT);
        } catch (StatisticsServiceValidationException e) {
            responseHeader.add(RESPONSE_STATUS, RESPONSE_ERROR);
            return new ResponseEntity<Object>(e.getErrors(), responseHeader, HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (StatisticsServiceException e) {
            responseHeader.add(RESPONSE_STATUS, RESPONSE_ERROR);
            return new ResponseEntity<Object>(responseHeader, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        responseHeader.add(RESPONSE_STATUS, RESPONSE_SUCCESS);
        return new ResponseEntity<Object>(responseHeader, HttpStatus.CREATED);
    }

    @GetMapping(value = GET_STATISTICS, consumes = MediaType.APPLICATION_JSON, produces = MediaType.APPLICATION_JSON)
    public ResponseEntity<Object> getTransactionsStatistics() {
        if(logger.isDebugEnabled()) {
            logger.debug("Get statistics of transactions.");
        }
        MultiValueMap<String, String> responseHeader = new HttpHeaders();
        TransactionsStatisticsResponse response = null;
        try {
            response = getStatisticsService().getStatistics();
        } catch (StatisticsServiceException e) {
            responseHeader.add(RESPONSE_STATUS, RESPONSE_ERROR);
            return new ResponseEntity<Object>(responseHeader, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        responseHeader.add(RESPONSE_STATUS, RESPONSE_SUCCESS);
        return new ResponseEntity<Object>(response, responseHeader, HttpStatus.CREATED);
    }

    public StatisticsService getStatisticsService() {
        return statisticsService;
    }
}
