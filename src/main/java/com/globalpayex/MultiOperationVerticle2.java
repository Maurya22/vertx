package com.globalpayex;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.Promise;
import io.vertx.core.Vertx;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MultiOperationVerticle2 extends AbstractVerticle {

    private static final Logger logger = LoggerFactory.getLogger(MultiOperationVerticle2.class);

    private int n1 =4;
    private int n2 =5;

     private Future<Integer> performAddition(){
         Promise<Integer> promise = Promise.promise();
         vertx.setTimer(5000, id ->{
             int result = n1 +n2;
             promise.complete(result);

         });
         return promise.future();
     }

     private Future<Integer> performMultiplication(int result){
         Promise<Integer> promise = Promise.promise();
         vertx.setTimer(3000, id -> {
             int r = (n1*n2)+result;
             promise.complete(r);
         });

         return promise.future();
     }

    @Override
    public void start() throws Exception {
         Future<Integer> additionFuture = performAddition();
//         additionFuture.onSuccess(additionResult -> logger.info("addition is {}",additionResult));
      Future<Integer>  multiplicationFuture = additionFuture.compose(additionResult -> {
            logger.info("addition is {}",additionResult);
            return performMultiplication(additionResult);
        });
      multiplicationFuture.onSuccess(multiplicationResult -> logger.info("multiplication is {}",multiplicationResult));


//
        logger.info("start initialized");

    }


    public static void main(String[] args) {
        Vertx vertx = Vertx.vertx();
        vertx.deployVerticle(new MultiOperationVerticle2());
    }
}
