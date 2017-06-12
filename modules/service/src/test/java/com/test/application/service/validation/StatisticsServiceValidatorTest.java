package com.test.application.service.validation;

import com.test.application.service.exception.StatisticsServiceValidationException;
import com.test.application.service.model.RecordTransactionRequest;
import com.test.application.service.validator.StatisticsServiceValidator;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Test class for {@link StatisticsServiceValidator}
 *
 * Created by rahulpawar on 6/11/17.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {StatisticsServiceValidator.class})
public class StatisticsServiceValidatorTest {

    @Test
    public void testValidateRecordTransactionRequest() throws Exception {
        RecordTransactionRequest recordTransactionRequest = new RecordTransactionRequest();
        recordTransactionRequest.setTimestamp(1497187929000L);
        //Scenario 1 : amount null and timestamp non null
        try {
            StatisticsServiceValidator.validateRecordTransactionRequest(recordTransactionRequest);
            Assert.fail("Validation should failed for null amount.");
        } catch(StatisticsServiceValidationException e) {
            Assert.assertEquals(1, e.getErrors().size());
            Assert.assertTrue(e.getErrors().contains("Amount cannot be zero or null."));
        }

        recordTransactionRequest.setAmount(10.50);
        recordTransactionRequest.setTimestamp(null);
        //Scenario 2 : amount non null and timestamp null
        try {
            StatisticsServiceValidator.validateRecordTransactionRequest(recordTransactionRequest);
            Assert.fail("Validation should failed for null timestamp.");
        } catch(StatisticsServiceValidationException e) {
            Assert.assertEquals(1, e.getErrors().size());
            Assert.assertTrue(e.getErrors().contains("Timestamp cannot be null."));
        }

       recordTransactionRequest = new RecordTransactionRequest();
        //Scenario 3 : both amount and timestamp null
        try {
            StatisticsServiceValidator.validateRecordTransactionRequest(recordTransactionRequest);
            Assert.fail("Validation should failed for null amount and timestamp.");
        } catch(StatisticsServiceValidationException e) {
            Assert.assertEquals(2, e.getErrors().size());
            Assert.assertTrue(e.getErrors().contains("Amount cannot be zero or null."));
            Assert.assertTrue(e.getErrors().contains("Timestamp cannot be null."));
        }

        recordTransactionRequest = new RecordTransactionRequest();
        recordTransactionRequest.setAmount(10.50);
        recordTransactionRequest.setTimestamp(1497187929000L);
        //Scenario 4 : Happy path
        try {
            StatisticsServiceValidator.validateRecordTransactionRequest(recordTransactionRequest);
        } catch(StatisticsServiceValidationException e) {
            Assert.fail("Validation should not failed.");
        }
    }

    @Test
    public void testIsTransactionExpired() throws Exception {
        //transaction time greater than current time
        Assert.assertTrue(StatisticsServiceValidator.isTransactionExpired(1497187929000L, 1497187930000L));

        //transaction older than 60 seconds
        Assert.assertTrue(StatisticsServiceValidator.isTransactionExpired(1497187929000L, 1497187865000L));

        //transaction valid
        Assert.assertFalse(StatisticsServiceValidator.isTransactionExpired(1497187929000L, 1497187900000L));
    }
}
