package com.example.rxjavademo.complementeryclasses;

import android.util.Log;

import io.reactivex.disposables.Disposable;

public class DisposableClass implements Disposable {
    @Override
    public void dispose() {
        Log.d("TAG", "Msg Disposed");
    }

    @Override
    public boolean isDisposed() {
        return false;
    }
}
