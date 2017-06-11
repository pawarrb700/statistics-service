package com.n26.application.service;

import com.n26.application.service.exception.*;
import com.n26.application.service.model.*;

/**
 * Interface to expose statistics services.
 *
 * Created by rahulpawar.
 */
public interface StatisticsService {

    /**
     * API used to record all the transactions.<br>
     *
     * @param request : {@link RecordTransactionRequest} : request containing following parameters <br>
     *        <ul>
     *        <p> {@link RecordTransactionRequest#getAmount()} : transaction amount. </p>
     *        <p> {@link RecordTransactionRequest#getTimestamp()} : time when transaction was performed.</p>
     *        </ul>
     * @throws StatisticsServiceValidationException thrown when request fails validations.
     * @throws TransactionExpiredException thrown when transaction being recored is older than 60 seconds.
     * @throws StatisticsServiceException thrown if any error comes in while recording transactions.
     */
    public void recordTransactions(final RecordTransactionRequest request) throws StatisticsServiceValidationException, TransactionExpiredException, StatisticsServiceException;

    /**
     * API for getting statistics of transactions performed in last 60 seconds.
     *
     * @return {@link TransactionsStatisticsResponse} : It has following parameters <br>
     *        <ul>
     *        <p> {@link TransactionsStatisticsResponse#getSum()} : Sum of balances of all transactions.</p>
     *        <p> {@link TransactionsStatisticsResponse#getAvg()} : Average of transaction balance.</p>
     *        <p> {@link TransactionsStatisticsResponse#getMax()} : Maximum balance from transaction.</p>
     *        <p> {@link TransactionsStatisticsResponse#getMin()} : Minimum balance from transaction.</p>
     *        <p> {@link TransactionsStatisticsResponse#getCount()} : Count of all transactions.</p>
     *        </ul>
     * @throws StatisticsServiceException thrown if any errors comes while getting statistics.
     */
    public TransactionsStatisticsResponse getStatistics() throws StatisticsServiceException;

}
