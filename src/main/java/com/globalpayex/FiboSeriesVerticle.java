package com.globalpayex;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.DeploymentOptions;
import io.vertx.core.ThreadingModel;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FiboSeriesVerticle extends AbstractVerticle {
    private static final Logger logger = LoggerFactory.getLogger(FiboSeriesVerticle.class);

    private int fibonacci(int n){
        if(n==0){
            return 0;
        }
        else if(n==1){
            return 1;
        }
        else {
            return fibonacci(n-1)+fibonacci(n-2);
        }
    }

    private void fib(int n){
        for(int i = 0 ; i<n ; i++){
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            logger.info("Fibonacci series {} where n = {}",fibonacci(i),n);
        }
    }

    @Override
    public void start() throws Exception {
        JsonObject config = config();
        int n1 = config.getInteger("n1");
        int n2 = config.getInteger("n2");
        vertx.setTimer(2000,id -> fib(n1));
        vertx.setTimer(2000,id -> fib(n2));

    }

    public static void main(String[] args) {
        Vertx vertx = Vertx.vertx();
        JsonObject config = new JsonObject()
                .put("n1", 20)
                .put("n2",30);
        DeploymentOptions options = new DeploymentOptions()
                .setConfig(config)
                        .setThreadingModel(ThreadingModel.WORKER);
        vertx.deployVerticle(new FiboSeriesVerticle(), options);
    }
}
