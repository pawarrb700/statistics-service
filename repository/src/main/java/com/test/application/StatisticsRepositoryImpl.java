package com.test.application;

import com.test.application.exception.StatisticsRepositoryException;
import com.test.application.exception.StatisticsRepositoryPersistanceException;
import com.test.application.entity.TransactionEntity;
import com.test.application.model.StatisticsData;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

import javax.persistence.*;
import java.sql.Timestamp;

/**
 * Implementation class for {@link StatisticsRepository}
 *
 * Created by rahulpawar.
 */
@Service
public class StatisticsRepositoryImpl implements StatisticsRepository {

    private static final Logger logger = LogManager.getLogger(StatisticsRepositoryImpl.class);

    private EntityManager entityManager;

    private EntityManagerFactory entityManagerFactory;

    public StatisticsRepositoryImpl() {
        entityManagerFactory = Persistence.createEntityManagerFactory("statisticsServiceUnit");
        entityManager = entityManagerFactory.createEntityManager();
    }

    public void persist(final Double amount, final Timestamp timestamp) throws StatisticsRepositoryPersistanceException {
        TransactionEntity transactionEntity = new TransactionEntity(amount, timestamp);
        logger.info("Persisting transaction entity : {}", transactionEntity);
        EntityTransaction transaction = getEntityManager().getTransaction();
        try {
            transaction.begin();
            getEntityManager().persist(transactionEntity);
            transaction.commit();
        } catch(Exception e) {
            logger.error("Error while persisiting transaction entity : {}", transactionEntity);
            throw new StatisticsRepositoryPersistanceException("Error while persisiting transaction entity.", e);
        }
    }

    public StatisticsData getStatisticsBetweenInterval(final Timestamp fromTime, final Timestamp toTime) throws StatisticsRepositoryException {
        logger.info("Getting transactions statistics between {} and {} interval.", fromTime, toTime);
        EntityTransaction transaction = getEntityManager().getTransaction();
        try{
            transaction.begin();
            Object[] result = (Object[])getEntityManager().createNamedQuery("TransactionEntity.getStatistics").
                    setParameter("fromTimestamp", fromTime).setParameter("toTimestamp", toTime).getSingleResult();
            transaction.commit();
            StatisticsData statisticsData = new StatisticsData();
            //Count ot transactions greater than zero then only populate statistics data
            if((long)result[4]!=0) {
                statisticsData.setSum((double)result[0]);
                statisticsData.setAvg((double)result[1]);
                statisticsData.setMax((double)result[2]);
                statisticsData.setMin((double)result[3]);
                statisticsData.setCount((long)result[4]);
            }
            return statisticsData;
        } catch(Exception e) {
            logger.error("Error while querying transactions between {} and {} interval. Error : {}", fromTime, toTime, e.getMessage());
            throw new StatisticsRepositoryException("Error while querying transactions.", e);
        }

    }

    public EntityManager getEntityManager() {
        return entityManager;
    }

    public EntityManagerFactory getEntityManagerFactory() {
        return entityManagerFactory;
    }
}
