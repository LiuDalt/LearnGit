package com.example.javatest;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Predicate;
import io.reactivex.schedulers.Schedulers;

public class TestRx {
    public static void main(String args[]){
        String it = "aaaa";
        String bbb = "bbbbb";
        Observable.interval(100, TimeUnit.MICROSECONDS).take(3).subscribe(new Observer<Long>() {
            @Override
            public void onSubscribe(Disposable d) {
                System.out.println("start");
            }

            @Override
            public void onNext(Long value) {
                System.out.println(value);
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onComplete() {
                System.out.println("complete");
            }
        });
        Observable.interval(100, TimeUnit.MICROSECONDS).takeUntil(new Predicate<Long>() {
            @Override
            public boolean test(Long value) throws Exception {
                System.out.println("test=" + value);
                return value == 4;
            }
        }).subscribe(new Observer<Long>() {
            @Override
            public void onSubscribe(Disposable d) {
                System.out.println("start");
            }

            @Override
            public void onNext(Long value) {
                System.out.println(value);
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onComplete() {
                System.out.println("complete");
            }
        });
    }
}
