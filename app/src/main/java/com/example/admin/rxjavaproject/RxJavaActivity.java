package com.example.admin.rxjavaproject;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.jakewharton.rxbinding3.view.RxView;
import com.jakewharton.rxbinding3.widget.RxTextView;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

public class RxJavaActivity extends AppCompatActivity {

    private CompositeDisposable compositeDisposable;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        AutoCompleteTextView emailEditText = findViewById(R.id.email);
        EditText passwordEditText = findViewById(R.id.password);
        Button buttonSubmit = findViewById(R.id.email_sign_in_button);
        buttonSubmit.setEnabled(false);
        compositeDisposable = new CompositeDisposable();

        compositeDisposable.add(Observable.combineLatest(
            RxTextView.textChanges(emailEditText),
            RxTextView.textChanges(passwordEditText),
            (email, password) -> email.length() > 0 && password.length() > 0
        ).subscribe(buttonSubmit::setEnabled));

        compositeDisposable.add(
                RxView.clicks(buttonSubmit)
                        .subscribeOn(Schedulers.io())
                        .map(aVoid -> {
                            Log.d("ASDA","Начинаем запрос!"+aVoid.toString());
                            Toast.makeText(this, emailEditText.getText().toString() + " "
                                + passwordEditText.getText().toString(), Toast.LENGTH_SHORT).show();
                            TimeUnit.SECONDS.sleep(5);
                            return aVoid;
                        })
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(item -> buttonSubmit.setEnabled(false))
        );
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        compositeDisposable.clear();
    }
}
