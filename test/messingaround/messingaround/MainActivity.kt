package com.test.messingaround.messingaround

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.rxkotlin.Observables
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_main.*
import java.util.concurrent.TimeUnit

class MainActivity : AppCompatActivity() {
    private var stringsDisposable: Disposable? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // On click listener for testButton.
        testButton.setOnClickListener {
            val userString = inputText.text

            val delayedStrings = Observables
                .zip(
                    Observable.just("p1 $userString", "p2 $userString"),
                    Observable.interval(0, 1, TimeUnit.SECONDS)
                ) { string, _ -> string }

            stringsDisposable = delayedStrings
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { text ->
                    mainText.text = text
                    mainText.animate().rotationBy(180f)
                    Log.d("dE", "next time")
                }
        }
    }

    override fun onDestroy() {
        stringsDisposable?.dispose()
        super.onDestroy()
    }
}

