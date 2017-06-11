package com.n26.application;

import com.n26.application.exception.StatisticsRepositoryException;
import com.n26.application.exception.StatisticsRepositoryPersistanceException;
import com.n26.application.model.StatisticsData;

import java.sql.Timestamp;

/**
 * Provides api to perform dao operations.
 *
 * Created by rahulpawar.
 */
public interface StatisticsRepository {

    /**
     * Persists transaction in data store.
     *
     * @param amount amount of transaction.
     * @param timestamp timestamp of transaction.
     * @throws StatisticsRepositoryPersistanceException thrown if any issue comes while persisting transaction.
     */
    public void persist(final Double amount, final Timestamp timestamp) throws StatisticsRepositoryPersistanceException;

    /**
     * Gives statistics details like sum, avg, max, min, count of trasactions from given interval.
     *
     * @param fromTime fromTime
     * @param toTime toTime
     * @return {@link StatisticsData}
     * @throws StatisticsRepositoryException thrown in case of any exception while reading data from repository.
     */
    public StatisticsData getStatisticsBetweenInterval(final Timestamp fromTime, final Timestamp toTime) throws StatisticsRepositoryException;
}
