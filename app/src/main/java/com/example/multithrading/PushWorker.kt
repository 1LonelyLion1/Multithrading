package com.example.multithrading


import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.telephony.AvailableNetworkInfo.PRIORITY_HIGH
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.work.Worker
import androidx.work.WorkerParameters


class PushWorker(
    appContext: Context,
    workerParams: WorkerParameters
):Worker(appContext, workerParams){


    private val channelId = "CHANNEL_ID"


    override fun doWork(): Result {



        val timer = inputData.getString("KEY")
        Log.d("WORK", "$timer")

        val  notificationManager:NotificationManager = applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val intent = Intent(applicationContext, PushWorker::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        val pendingintent = PendingIntent.getActivity(applicationContext, 0 , intent, PendingIntent.FLAG_UPDATE_CURRENT)

        val notificationBuilder: NotificationCompat.Builder = NotificationCompat.Builder(applicationContext, channelId)
            .setAutoCancel(false)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setWhen(System.currentTimeMillis())
            .setContentIntent(pendingintent)
            .setContentTitle("Timer")
            .setContentText(timer)
            .setPriority(PRIORITY_HIGH) //не разобрался пока с нотификаторами android q
        createChannelIfNeed(notificationManager)
        notificationManager.notify(1, notificationBuilder.build())

        return Result.success()
    }

private fun createChannelIfNeed(manager: NotificationManager){
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
        val notificationChannel = NotificationChannel(channelId, channelId, NotificationManager.IMPORTANCE_DEFAULT)
        manager.createNotificationChannel(notificationChannel)
    }
}


}