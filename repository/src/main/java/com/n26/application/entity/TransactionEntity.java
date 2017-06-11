package com.n26.application.entity;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Timestamp;

/**
 * Entity class for holding transactions.
 *
 * Created by rahulpawar.
 */
@Entity
@NamedQueries({
        @NamedQuery(name = "TransactionEntity.getStatistics", query = "SELECT SUM(TE.amount) as sum, AVG(TE.amount) as avg, MAX(TE.amount) as max, MIN(TE.amount) as min, COUNT(TE.amount) as count " +
        "FROM TransactionEntity TE WHERE TE.timestamp BETWEEN :fromTimestamp AND :toTimestamp")
})
public class TransactionEntity implements Serializable{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private double amount;

    private Timestamp timestamp;

    public double getAmount() {
        return amount;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    /**
     * For JPA
     */
    public TransactionEntity() {
        //default constructor
    }

    public TransactionEntity(final double amount, final Timestamp timestamp) {
        this.amount = amount;
        this.timestamp = timestamp;
    }

    @Override
    public String toString() {
        return ReflectionToStringBuilder.toString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }

}
