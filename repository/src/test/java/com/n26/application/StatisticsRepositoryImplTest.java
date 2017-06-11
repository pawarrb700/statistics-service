package com.n26.application;

import com.n26.application.model.StatisticsData;
import com.n26.application.entity.TransactionEntity;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.persistence.*;
import java.sql.Timestamp;

/**
 * Test for api's from {@link StatisticsRepository}
 *
 * Created by rahulpawar.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {StatisticsRepositoryImpl.class})
public class StatisticsRepositoryImplTest {

    @Autowired
    private StatisticsRepository statisticsRepository;

    private static EntityManager entityManager;

    private static EntityManagerFactory entityManagerFactory;

    @BeforeClass
    public static void init() {
        entityManagerFactory = Persistence.createEntityManagerFactory("statisticsServiceUnit");
        entityManager = entityManagerFactory.createEntityManager();
    }

    @Before
    public void clearData() throws Exception{
        EntityTransaction updateTransaction = entityManager.getTransaction();
        updateTransaction.begin();
        entityManager.createNativeQuery("TRUNCATE TABLE TransactionEntity").executeUpdate();
        updateTransaction.commit();
    }

    /**
     * Test to check persistence and statistics of transaction data between intervals.
     *
     * @throws Exception
     */
    @Test
    public void testTransactionStatisticsBetweenIntervals() throws Exception {
        clearData();
        statisticsRepository.persist(10.65, new Timestamp(System.currentTimeMillis() - 1000));
        statisticsRepository.persist(4.35, new Timestamp(System.currentTimeMillis() - 1000));
        statisticsRepository.persist(8.50, new Timestamp(System.currentTimeMillis() - 1000));
        statisticsRepository.persist(1.50, new Timestamp(System.currentTimeMillis() - 1000));
        //older than 2 min
        statisticsRepository.persist(3.75, new Timestamp(System.currentTimeMillis() - 120000));

        //Query transactions older than a min
        final StatisticsData statisticsData = statisticsRepository.getStatisticsBetweenInterval(new Timestamp(System.currentTimeMillis() - 60000), new Timestamp(System.currentTimeMillis()));
        Assert.assertNotNull("Statistics data is null.",statisticsData);
        Assert.assertEquals("Expected sum and actual is different.",25l, statisticsData.getSum(), 0);
        Assert.assertEquals("Expected avg and actual is different.",6.25, statisticsData.getAvg(), 0);
        Assert.assertEquals("Expected max amount and actual is different.",10.65, statisticsData.getMax(), 0);
        Assert.assertEquals("Expected min amount and actual is different.",1.50, statisticsData.getMin(), 0);
        Assert.assertEquals("Expected count and actual is different.",4, statisticsData.getCount());
    }
}
