package com.globalpayex;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.DeploymentOptions;
import io.vertx.core.Vertx;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.List;
import java.util.Random;


public class WinnerGreaterVerticle extends AbstractVerticle {
    private static final Logger logger = LoggerFactory.getLogger(WinnerGreaterVerticle.class);

    public static final List<String> students = Arrays.asList(
            "mehul","jane","Rakesh","ranjeet","sunil"
    );

    @Override
    public void start() throws Exception {
        logger.info("verticle start");
        vertx.setTimer(1000, id -> logger.info("And"));
        vertx.setTimer(2000, id -> logger.info("The"));
        vertx.setTimer(3000, id -> logger.info("Winner"));
        vertx.setTimer(4000, id -> logger.info("IS"));
        vertx.setTimer(9000, this:: randomwinner);
        logger.info("timers initialized in the verticle");
    }
    private void randomwinner(Long r) {
        var random = new Random();
        String winner = students.get(random.nextInt(students.size()));
        logger.info(winner);
    }

    public static void main(String[] args) {
        Vertx vertx = Vertx.vertx();
//        vertx.deployVerticle(new WinnerGreaterVerticle()); // deploy 1 instance of the verticle
        DeploymentOptions options = new DeploymentOptions()
                .setInstances(2);
        vertx.deployVerticle("com.globalpayex.WinnerGreaterVerticle", options);
    }

}
