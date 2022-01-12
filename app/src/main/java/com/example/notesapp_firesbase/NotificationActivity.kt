package com.example.notesapp_firesbase

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.annotation.RequiresApi
import java.time.LocalDateTime

class NotificationActivity : AppCompatActivity() {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_notification)
        val btStart = findViewById<Button>(R.id.btStart)
        val date = LocalDateTime.now()
        val tvDate = findViewById<TextView>(R.id.tvDate)
        tvDate.text = date.toString()

        //track notifications
        val channelId = "mynoteapp.notifications"
        val description = "NotePad"
        var builder = Notification.Builder(this)
        val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        val intent = Intent(this, MainActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_IMMUTABLE)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            var notificationChannel =
                NotificationChannel(channelId, description, NotificationManager.IMPORTANCE_HIGH)
            notificationManager.createNotificationChannel(notificationChannel)
            builder = Notification.Builder(this, channelId)
                .setSmallIcon(R.drawable.ic_baseline_mail_24)
                .setContentIntent(pendingIntent)
                .setContentTitle("\uD83D\uDC8C Good day or bad day, it is always worth taking note of")
                .setContentText("write that moment down")
        } else {
            builder = Notification.Builder(this)
                .setSmallIcon(R.drawable.ic_baseline_mail_24)
                .setContentIntent(pendingIntent)
                .setContentTitle("\uD83D\uDC8CGood day or bad day, it is always worth taking note of")
                .setContentText(("write that moment down"))
        }
        notificationManager.notify(245633, builder.build())

        //button interactions
        btStart.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            finish()
            startActivity(intent)
        }
    }


}