package com.example.javatest;

import android.util.Log;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class TestThread {
    public static void main(String[] args){
        ExecutorService service = Executors.newSingleThreadExecutor();
        service.submit(new Callable<Object>() {
            @Override
            public Object call() throws Exception {
                Thread.sleep(200);
                System.out.println("callable1");
                return null;
            }
        });
        service.submit(new Callable<Object>() {
            @Override
            public Object call() throws Exception {
                Thread.sleep(200);
                System.out.println("callable2");
                return null;
            }
        });
        service.submit(new Callable<Object>() {
            @Override
            public Object call() throws Exception {
                Thread.sleep(200);
                System.out.println("callable3");
                return null;
            }
        });
        service.submit(new Callable<Object>() {
            @Override
            public Object call() throws Exception {
                Thread.sleep(200);
                System.out.println("callable4");
                return null;
            }
        });
        service.submit(new Callable<Object>() {
            @Override
            public Object call() throws Exception {
                Thread.sleep(200);
                System.out.println("callable5");
                return null;
            }
        });
        service.submit(new Callable<Object>() {
            @Override
            public Object call() throws Exception {
                Thread.sleep(200);
                System.out.println("callable6");
                return null;
            }
        });        service.submit(new Callable<Object>() {
            @Override
            public Object call() throws Exception {
                Thread.sleep(200);
                System.out.println("callable7");
                return null;
            }
        });

    }
}
