package com.example.rxjavademo;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import org.reactivestreams.Subscription;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity {
    TextView tv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tv=findViewById(R.id.tv);

        Observable<String> animalsObservable = getAnimalsObservable();

        Observer<String> animalsObserver = getAnimalsObserver();

        animalsObservable
                .observeOn(Schedulers.io())
                .subscribeOn(AndroidSchedulers.mainThread())
                .subscribe(animalsObserver);

    }


    private Observer<String> getAnimalsObserver() {
        return new Observer<String>() {
            @Override
            public void onSubscribe(Disposable d) {
                tv.setText(tv.getText()+"\nonSubscribe");
                Log.d("TAG", "onSubscribe");
            }

            @Override
            public void onNext(String s) {
                tv.setText(tv.getText()+"\nonNext"+" "+s);
                Log.d("TAG", "Name: " + s);
            }

            @Override
            public void onError(Throwable e) {
                tv.setText(tv.getText()+"\nonError");
                Log.e("TAG", "onError: " + e.getMessage());
            }

            @Override
            public void onComplete() {
                tv.setText(tv.getText()+"\nonComplete");
                Log.d("TAG", "All items are emitted!");
            }
        };
    }

    private Observable<String> getAnimalsObservable() {
        return Observable.just("Ant", "Bee", "Cat", "Dog", "Fox");
    }

}
