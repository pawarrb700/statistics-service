package com.n26.application.service;

import com.n26.application.StatisticsRepository;
import com.n26.application.TimeService;
import com.n26.application.exception.StatisticsRepositoryException;
import com.n26.application.exception.StatisticsRepositoryPersistanceException;
import com.n26.application.model.StatisticsData;
import com.n26.application.service.exception.StatisticsServiceException;
import com.n26.application.service.exception.StatisticsServiceValidationException;
import com.n26.application.service.exception.TransactionExpiredException;
import com.n26.application.service.model.RecordTransactionRequest;
import com.n26.application.service.model.TransactionsStatisticsResponse;
import com.n26.application.service.validator.StatisticsServiceValidator;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.sql.Time;
import java.sql.Timestamp;
import java.text.ParseException;

/**
 * Implementation of {@link StatisticsService}
 *
 * Created by rahulpawar.
 */
@Service
public class StatisticsServiceImpl implements StatisticsService {

    private static final Logger logger = LogManager.getLogger(StatisticsServiceImpl.class);

    @Autowired
    private StatisticsRepository statisticsRepository;

    @Override
    public void recordTransactions(RecordTransactionRequest request) throws StatisticsServiceValidationException, TransactionExpiredException, StatisticsServiceException {
        logger.info("Received record transaction request : {}", request);

        StatisticsServiceValidator.validateRecordTransactionRequest(request);
        try {
            long currentUTCTime = TimeService.getCurrentUTCTime();
            logger.info("currentTime : {}, transactionTime : {}", currentUTCTime, request.getTimestamp());
            if(StatisticsServiceValidator.isTransactionExpired(currentUTCTime, request.getTimestamp())) {
                logger.error("Transaction received is expired.");
                throw new TransactionExpiredException("Transaction is expired.");
            }
            getStatisticsRepository().persist(request.getAmount(), new Timestamp(request.getTimestamp()));
            logger.info("Transaction {} recorded successfully.", request);
        } catch(final ParseException | StatisticsRepositoryPersistanceException e) {
            logger.error("Error while recording transaction. Error : {}", e);
            throw new StatisticsServiceException("Error while recording transaction.", e);
        }
    }

    @Override
    public TransactionsStatisticsResponse getStatistics() throws StatisticsServiceException {
        logger.info("Get statistics request received.");

        StatisticsData statisticsData = null;
        try {
            Long currentUTCTime = TimeService.getCurrentUTCTime();
            statisticsData = getStatisticsRepository().getStatisticsBetweenInterval(new Timestamp(currentUTCTime-60000), new Timestamp(currentUTCTime));
        } catch(final ParseException | StatisticsRepositoryException e) {
            logger.error("Error while recording transaction. Error : {}", e);
            throw new StatisticsServiceException("Error while recording transaction.", e);
        }
        return populateTransactionStatisticsResponse(statisticsData);
    }

    private TransactionsStatisticsResponse populateTransactionStatisticsResponse(final StatisticsData statisticsData) {
        TransactionsStatisticsResponse response = new TransactionsStatisticsResponse();
        response.setSum(statisticsData.getSum());
        response.setAvg(statisticsData.getAvg());
        response.setMax(statisticsData.getMax());
        response.setMin(statisticsData.getMin());
        response.setCount(statisticsData.getCount());
        return response;
    }

    public StatisticsRepository getStatisticsRepository() {
        return statisticsRepository;
    }

    public void setStatisticsRepository(StatisticsRepository statisticsRepository) {
        this.statisticsRepository = statisticsRepository;
    }
}
