package com.test.application.service;

import com.test.application.StatisticsRepositoryImpl;
import com.test.application.TimeService;
import com.test.application.service.exception.TransactionExpiredException;
import com.test.application.service.model.RecordTransactionRequest;
import com.test.application.service.model.TransactionsStatisticsResponse;
import com.test.application.service.validator.StatisticsServiceValidator;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Test class for {@link StatisticsServiceImpl}
 *
 * Created by rahulpawar on 6/11/17.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {StatisticsServiceImpl.class, StatisticsRepositoryImpl.class, TimeService.class, StatisticsServiceValidator.class})
public class StatisticsServiceImplTest {

    @Autowired
    private StatisticsService statisticsService;

    @Test
    public void testRecordTransactionHappyPath() throws Exception {
        Long currentTime = TimeService.getCurrentUTCTime();

        RecordTransactionRequest request = new RecordTransactionRequest();
        request.setAmount(5.50);
        //58 second older transaction
        request.setTimestamp(currentTime - 55000);
        try {
            statisticsService.recordTransactions(request);
        } catch(final Exception e) {
            Assert.fail("Should not get any exception.");
        }
    }

    @Test
    public void testRecordTransactionWithExpiredTransaction() throws Exception {
        Long currentTime = TimeService.getCurrentUTCTime();

        RecordTransactionRequest request = new RecordTransactionRequest();
        request.setAmount(5.50);
        //65 second older transaction
        request.setTimestamp(currentTime - 65000);
        try {
            statisticsService.recordTransactions(request);
            Assert.fail("Record transaction should fail for expired transaction.");
        } catch(final TransactionExpiredException e) {
            Assert.assertNotNull(e.getMessage());
            Assert.assertTrue(e.getMessage().equals("Transaction is expired."));
        }
    }

    @Test
    public void testGetStatisticsWithNoTransactionsRecorded() throws Exception {
        TransactionsStatisticsResponse response = statisticsService.getStatistics();
        Assert.assertNotNull(response);
        Assert.assertEquals(0, response.getSum(), 0);
        Assert.assertEquals(0, response.getAvg(), 0);
        Assert.assertEquals(0, response.getMax(), 0);
        Assert.assertEquals(0, response.getMin(), 0);
        Assert.assertEquals(0, response.getCount());
    }

    @Test
    public void testGetStatisticsWithTransactionsRecorded() throws Exception {
        Long currentTime = TimeService.getCurrentUTCTime();
        //record 30 seconds older
        RecordTransactionRequest request = new RecordTransactionRequest();
        request.setAmount(5.50);
        request.setTimestamp(currentTime - 30000);
        statisticsService.recordTransactions(request);

        //record 15 seconds older
        request = new RecordTransactionRequest();
        request.setAmount(8.50);
        request.setTimestamp(currentTime - 15000);
        statisticsService.recordTransactions(request);

        TransactionsStatisticsResponse response = statisticsService.getStatistics();
        Assert.assertNotNull(response);
        Assert.assertEquals(14d, response.getSum(), 0);
        Assert.assertEquals(7d, response.getAvg(), 0);
        Assert.assertEquals(8.50, response.getMax(), 0);
        Assert.assertEquals(5.50, response.getMin(), 0);
        Assert.assertEquals(2, response.getCount());
    }
}
