package com.example.administrator.myapplication;

import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.LinearLayout;

import com.example.administrator.myapplication.databinding.ItemLayoutBinding;

import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;

public class MainActivity extends AppCompatActivity {

    private LinearLayout mRootLy;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        mRootLy = findViewById(R.id.rootly);
//        dataBinding();
        try {
            mRootLy.getBaseline();
        }catch (Exception e){
            Log.i("testtest", "testlog", new Throwable());
        }

//        new LeakThread().start();

        testRx();
    }

    private void testRx() {
        Observable<String> observable = Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(ObservableEmitter<String> emitter) throws Exception {
                emitter.onNext("121212");
                emitter.onComplete();
            }
        });

        Observer<String> observer = new Observer<String>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(String s) {
                Log.i("testRx", "onEvent observer = " + s);
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onComplete() {
                Log.i("testRx", "observer oncomplete");
            }
        };

        Subscriber<String> subscriber = new Subscriber<String>() {
            @Override
            public void onSubscribe(Subscription s) {

            }

            @Override
            public void onNext(String o) {
                Log.i("testRx", "onEvent subscriber = " + o);
            }

            @Override
            public void onError(Throwable t) {

            }

            @Override
            public void onComplete() {
                Log.i("testRx", " subscriber complete ");
            }
        };


        Consumer<String> consumer = new Consumer<String>() {
            @Override
            public void accept(String s) throws Exception {
                Log.i("testRx", "onEvent consumer accpet = " + s);
            }
        };
        observable.subscribe(observer);
//        observable.subscribe(subscriber);
        observable.subscribe(consumer);
        observable.subscribe();

        Observable ob = Observable.fromArray("abaadasd");
        ob.subscribe(observer);
        ob.subscribe(consumer);
        Action action = new Action() {
            @Override
            public void run() throws Exception {
                Log.i("testRx", " Action complete ");
            }
        };
        Consumer<String> onNext = new Consumer<String>() {
            @Override
            public void accept(String o) throws Exception {
                Log.i("testRx", "onNext = " + o);
            }
        };
        Consumer onError = new Consumer() {
            @Override
            public void accept(Object o) throws Exception {
                Log.i("testRx", "onError = " + o);
            }
        };
        Consumer onSubcribe = new Consumer() {
            @Override
            public void accept(Object o) throws Exception {
                Log.i("testRx", "onSubcribe = " + o);
            }
        } ;
        ob.subscribe(onNext, onError, action, onSubcribe);
    }

    private void dataBinding() {
        ItemLayoutBinding binding = DataBindingUtil.inflate(getLayoutInflater(), R.layout.item_layout, mRootLy, true);
        binding.tv2.setText("branch1");
        binding.tv1.setText("0000000");
        Log.d("aaaa", "11111");
        Log.i("inputcomponent--", "-----------------");
    }
    class LeakThread extends Thread {
        @Override
        public void run() {
            try {
                Thread.sleep(6 * 60 * 1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}