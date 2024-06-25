package com.globalpayex;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.DeploymentOptions;
import io.vertx.core.Future;
import io.vertx.core.Vertx;
import io.vertx.core.file.AsyncFile;
import io.vertx.core.file.OpenOptions;
import io.vertx.core.json.JsonObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MultiOperationVerticle4 extends AbstractVerticle {
    private static final Logger logger = LoggerFactory.getLogger(MultiOperationVerticle4.class);
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

    private int fib(int n){
        for(int i = 0 ; i<n ; i++){
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            logger.info("Fibonacci series {} where n = {}",fibonacci(i),n);
        }
        return 0;
    }

    private void readFile(String filePath){
//        non Blocking I/O
        OpenOptions options = new OpenOptions()
                .setCreate(false)
                .setRead(true);
        Future<AsyncFile> readFileFuture = vertx.fileSystem().open(filePath,options);
        readFileFuture.onSuccess(asyncFile -> {
            asyncFile.handler(System.out::println)
                    .exceptionHandler(exception -> logger.info("error while openning file {}", exception.fillInStackTrace()));
        });
        readFileFuture.onFailure(exception -> logger.info("error while openning file {}", exception.fillInStackTrace()));

    }

    @Override
    public void start() throws Exception {
        int a = config().getInteger("a");
        int b = config().getInteger("b");
//        schedule an event loop thread

        vertx.setTimer(1000, id -> this.readFile("build.gradle"));
        vertx.setTimer(1000, id -> this.performAddition(a,b));
        vertx.setTimer(2000, id -> this.performMultiplication(a,b));

//        vertx.setTimer(1000, id -> this.readFile("build.gradle"));
//        schedule blocking operation on working thread
//        vertx.executeBlocking(()->{
//            this.fib(a);
//            return 0;
//        });
//        vertx.executeBlocking(()->
//        {
//            this.fib(b);
//            return 0;
//        });
        vertx.executeBlocking(()->this.fib(a),
                ar -> {
                   if(ar.succeeded()){
                       int r = ar.result();
                       logger.info("blocking operation result is {}",r);
                   }
                }
                );

        vertx.executeBlocking(()->this.fib(b),
                ar -> {
//             ar -> AsyncResult
                    if(ar.succeeded()){
                        int r = ar.result();
                        logger.info("blocking operation result is {}",r);
                    }
                }
        );




    }

    private void performMultiplication(int a, int b) {
        logger.info("addition is {}", a+b);
    }

    private void performAddition(int a, int b) {
        logger.info("multiplication is {}", a*b);
    }

    public static void main(String[] args) {
       Vertx vertx =Vertx.vertx();
        DeploymentOptions options = new DeploymentOptions()
                .setConfig(new JsonObject().put("a",10).put("b",6));

        vertx.deployVerticle(new MultiOperationVerticle4(), options);
    }
}
