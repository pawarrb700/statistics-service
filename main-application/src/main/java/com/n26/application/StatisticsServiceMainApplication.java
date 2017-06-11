package com.n26.application;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.SpringApplication;
import org.springframework.context.annotation.ComponentScan;

/**
 * Main class for sprint boot.
 * 
 * Created by rahulpawar.
 */
@SpringBootApplication
@ComponentScan({"com.n26.application.rest"})
public class StatisticsServiceMainApplication {

    public static void main(String[] args) {
        SpringApplication.run(StatisticsServiceMainApplication.class, args);
    }
}
