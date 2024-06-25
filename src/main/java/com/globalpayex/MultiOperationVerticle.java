package com.globalpayex;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.DeploymentOptions;
import io.vertx.core.Vertx;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MultiOperationVerticle extends AbstractVerticle {
    private static final Logger logger = LoggerFactory.getLogger(MultiOperationVerticle.class);

    private int n1 =4;
    private int n2 =5;
    @Override
    public void start() throws Exception {
        vertx.setTimer(5000, id -> {
            int result = n1+n2;
            logger.info("addition is :"+ result);
            vertx.setTimer(3000, id2->{
                int mulResult = (n1*n2)+result;
                logger.info("multiplication is : "+ mulResult);
            });
        });

    }



    public static void main(String[] args) {
        Vertx vertx = Vertx.vertx();
//        vertx.deployVerticle(new WinnerGreaterVerticle()); // deploy 1 instance of the verticle
//        DeploymentOptions options = new DeploymentOptions()
//                .setInstances(2);
//        vertx.deployVerticle("com.globalpayex.MultiOperationVerticle", options);
        vertx.deployVerticle(new MultiOperationVerticle());
    }
}
