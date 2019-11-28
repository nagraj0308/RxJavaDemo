package com.example.rxjavademo;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;
import io.reactivex.functions.Predicate;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity {
    TextView tv;
    private CompositeDisposable disposable = new CompositeDisposable();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tv=findViewById(R.id.tv);



        disposable.add(getNotesObservable()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .map(note -> {
                    // Making the note to all uppercase
                    note.setText(note.getText().toUpperCase());
                    return note;
                })
                .subscribeWith(getNotesObserver()));
    }





    private DisposableObserver<Note> getNotesObserver() {
        return new DisposableObserver<Note>() {

            @Override
            public void onNext(Note note) {
                tv.setText(tv.getText()+"\n"+note.getText());
                Log.d("TAG", "Note: " + note.getText());
            }

            @Override
            public void onError(Throwable e) {
                Log.e("TAG", "onError: " + e.getMessage());
            }

            @Override
            public void onComplete() {
                Log.d("TAG", "All notes are emitted!");
            }
        };
    }

    private Observable<Note> getNotesObservable() {
        final List<Note> notes = prepareNotes();

        return Observable.create(new ObservableOnSubscribe<Note>() {
            @Override
            public void subscribe(ObservableEmitter<Note> emitter) throws Exception {
                for (Note note : notes) {
                    if (!emitter.isDisposed()) {
                        emitter.onNext(note);
                    }
                }

                if (!emitter.isDisposed()) {
                    emitter.onComplete();
                }
            }
        });
    }


    private List<Note> prepareNotes() {
        List<Note> notes = new ArrayList<>();
        notes.add(new Note(1, "buy tooth paste!"));
        notes.add(new Note(2, "call brother!"));
        notes.add(new Note(3, "watch narcos tonight!"));
        notes.add(new Note(4, "pay power bill!"));

        return notes;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        // don't send events once the activity is destroyed
        disposable.clear();
    }
}
