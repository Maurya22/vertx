package com.globalpayex;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class WinnerGreater {
    public static final List<String> students = Arrays.asList(
            "mehul","jane","Rakesh","ranjeet","sunil"
    );

    public static void main(String[] args) throws InterruptedException {
        var random = new Random();
        String winner = students.get(random.nextInt(students.size()));
        System.out.println("And");
        Thread.sleep(1000);
        System.out.println("The");
        Thread.sleep(1000);
        System.out.println("Winner");
        Thread.sleep(1000);
        System.out.println("Is");
        Thread.sleep(5000);
        System.out.println(winner);

    }
}
