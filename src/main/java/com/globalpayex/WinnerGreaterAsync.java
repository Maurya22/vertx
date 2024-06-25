package com.globalpayex;

import io.vertx.core.Vertx;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class WinnerGreaterAsync {

    private static final Logger logger = LoggerFactory.getLogger(WinnerGreaterAsync.class);
    public static final List<String> students = Arrays.asList(
            "mehul","jane","Rakesh","ranjeet","sunil"
    );

    public static void main(String[] args) {
        Vertx vertx = Vertx.vertx();

        vertx.setTimer(1000, id -> logger.info("And"));
        vertx.setTimer(2000, id -> logger.info("The"));
        vertx.setTimer(3000, id -> logger.info("Winner"));
        vertx.setTimer(4000, id -> logger.info("IS"));
        vertx.setTimer(9000, WinnerGreaterAsync :: randomwinner);
        logger.info("All timer schedule");
    }

    private static void randomwinner(Long r) {
        var random = new Random();
        String winner = students.get(random.nextInt(students.size()));
        logger.info(winner);

    }
}
