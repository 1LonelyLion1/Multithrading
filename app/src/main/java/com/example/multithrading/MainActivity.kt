package com.example.multithrading


import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity


import androidx.work.*
import java.util.concurrent.TimeUnit


class MainActivity : AppCompatActivity() {

    private var itsRun:Boolean = false
    private var timer:String = "00:00:00"
    private val durationWorker:Int = 5

     private lateinit var tvTimer: TextView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        tvTimer = findViewById(R.id.tvTimer)



        findViewById<Button>(R.id.btnStart).setOnClickListener {

            if(!itsRun) {
                itsRun = true
                val runnable = Runnable {

                    val handler = Handler(Looper.getMainLooper())
                    var time = 0

                    while (itsRun) {
                        Thread.sleep(1000)
                        time++

                        handler.post {
                            run {

                                timer = timer(time)
                                tvTimer.text = timer
                            }
                        }
                    }

                }
                val thread = Thread(runnable)
                thread.start()
            }

        }

    }


    private fun timer(time: Int):String{

        var result = ""

        val hours: Int = time/3600
        val minutes: Int = (time - hours * 3600)/60
        val seconds: Int = time - hours * 3600 - minutes * 60

        result += if (hours < 10) "0$hours:" else "$hours:"
        result += if (minutes < 10) "0$minutes:" else "$minutes:"
        result += if (seconds < 10) "0$seconds" else "$seconds"

        return result
    }

    private fun startWork() {

        val inputData = Data.Builder()
            .putString("KEY", timer)
            .build()

        val request = OneTimeWorkRequest.Builder(PushWorker::class.java)
            .setInputData(inputData)
            .setInitialDelay(durationWorker.toLong(), TimeUnit.SECONDS)
            .build()

        WorkManager.getInstance(this).enqueue(request)
    }

    override fun onDestroy() { //может не вызваться при освобождении памяти системой. Способ ненадежный((
        super.onDestroy()
        itsRun = false
       startWork()
    }

}