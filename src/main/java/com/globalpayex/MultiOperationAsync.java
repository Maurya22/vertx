package com.globalpayex;

import io.vertx.core.Vertx;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;

import java.util.Scanner;

public class MultiOperationAsync {
    private static final Logger logger = LoggerFactory.getLogger(WinnerGreaterAsync.class);
    public static void main(String[] args) {
        Vertx vertx = Vertx.vertx();
        Scanner sc = new Scanner(System.in);
        while(true) {
            logger.info("Enter first number : ");
            int n1 = sc.nextInt();
            logger.info("Enter second number : ");
            int n2 = sc.nextInt();
            vertx.setTimer(3000, id->perfromAddition(n1,n2));
            vertx.setTimer(4000, id->perfromMultiplication(n1,n2));
        }

    }

    private static void perfromMultiplication(int n1, int n2) {
        logger.info("Addition is : " + n1+n2);
    }

    private static void perfromAddition(int n1, int n2) {
        logger.info("Multiplication is : " +n1*n2);
    }


}
