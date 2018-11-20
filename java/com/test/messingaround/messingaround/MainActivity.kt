package com.test.messingaround.messingaround

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Async method. Will stall thread it runs on until true.
        fun isHalfDone() : Boolean {
            while (mainText.rotation != 180f) {
                Thread.sleep(100) // Wait 100ms before checking again, even though on its own thread.
            }
            return true
        }

        var state = 0 // Button output has 2 states. Just for testing stuff.

        // On click listener for testButton.
        testButton.setOnClickListener { v ->
            mainText.rotation = 0f // Set initial rotation to 0f.
            mainText.animate().rotationBy(180f)
            Log.d("dE", mainText.rotation.toString())
            var userString = inputText.text
            var newStringContent = ""
            if (state == 0) {
                newStringContent = "p1 $userString"
                state = 1
            }
            else if (state == 1) {
                newStringContent = "p2 $userString"
                state = 0
            }
            Observable.create<Boolean> { emitter ->
                emitter.onNext(isHalfDone())
            }
                .filter { result -> result == true}
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                //.debounce(1000, TimeUnit.MILLISECONDS)
                .subscribe{onNext -> mainText.text = newStringContent; mainText.animate().rotationBy(180f); Log.d("dE", "next time")}
        }



    }
}

