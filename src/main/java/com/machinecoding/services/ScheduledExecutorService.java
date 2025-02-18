//package com.machinecoding.services;
//
//import java.util.concurrent.TimeUnit;
//
//public class ScheduledExecutorService {
//
//    public void scheduleAtFixedRate(Object o, long initialDelay, long period, TimeUnit timeUnit) {
//        java.util.concurrent.ScheduledExecutorService executor = java.util.concurrent.Executors.newScheduledThreadPool(1);
//        executor.scheduleAtFixedRate(() -> {
//            // Task to execute asynchronously
//            System.out.println("Executing scheduled task with object: " + o);
//        }, initialDelay, period, timeUnit);
//    }
//}
