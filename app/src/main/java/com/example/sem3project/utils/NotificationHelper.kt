package com.example.sem3project.utils

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.sem3project.model.BookModel
import android.content.Context
import com.example.sem3project.R
import com.example.sem3project.view.UserDashboard

object NotificationHelper {

    private const val CHANNEL_ID = "book_notifications"
    private const val CHANNEL_NAME = "Book Updates"
    private const val CHANNEL_DESCRIPTION = "Notifications for new books added"

    // Create notification channel
    fun createNotificationChannel(context: Context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(CHANNEL_ID, CHANNEL_NAME, importance).apply {
                description = CHANNEL_DESCRIPTION
            }

            val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    // Show notification when new book is added
    fun showBookAddedNotification(context: Context, book: BookModel) {
        // Create intent to open app when notification is clicked
        val intent = Intent(context, UserDashboard::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        val pendingIntent = PendingIntent.getActivity(
            context,
            0,
            intent,
            PendingIntent.FLAG_IMMUTABLE
        )

        // Build the notification
        val notification = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_launcher_foreground) // Change this to your app icon
            .setContentTitle("New Book Added! 📚")
            .setContentText("${book.bookName} by ${book.author}")
            .setStyle(
                NotificationCompat.BigTextStyle()
                    .bigText("${book.bookName} by ${book.author} has been added to the library!")
            )
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true) // Dismiss notification when clicked
            .build()

        // Show the notification
        val notificationManager = NotificationManagerCompat.from(context)
        try {
            notificationManager.notify(System.currentTimeMillis().toInt(), notification)
        } catch (e: SecurityException) {
            // Handle case where notification permission is denied
            e.printStackTrace()
        }
    }
}