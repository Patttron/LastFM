package teach.meskills.lastfm.data

import android.app.*
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.work.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import teach.meskills.lastfm.MainActivity
import teach.meskills.lastfm.R
import java.lang.Exception

class DownloadWorker(context: Context, params: WorkerParameters) : Worker(context, params) {

    private val appDatabase = AppDatabase.build(context)
    private val scope = CoroutineScope(Dispatchers.IO)

    override fun doWork(): Result {
        scope.launch {
            val loading = ContentRepositoryOkhttp(appDatabase)
            try {
                loading.getMedia()
                with(NotificationManagerCompat.from(applicationContext)) {
                    notify(
                        NOTIFICATION_ID,
                        createNotification(applicationContext)
                    )
                }
            } catch (e: Exception) {
                e.stackTraceToString()
                Result.failure()
            }
        }
            return Result.success()
    }

    private fun createChannel(context: Context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            // Create the NotificationChannel
            val name = "Loading channel"
            val descriptionText = "Loading channel"
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val mChannel = NotificationChannel(CHANNEL_ID, name, importance)
            mChannel.description = descriptionText
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            val notificationManager =
                context.getSystemService(Service.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(mChannel)
        }
    }

    fun createNotification(context: Context): Notification {
        val pIntent = PendingIntent.getActivity(
            context,
            CONTENT_REQUEST_CODE,
            Intent(context, MainActivity::class.java),
            PendingIntent.FLAG_IMMUTABLE
        )
        val notification = NotificationCompat.Builder(context, CHANNEL_ID)
            .setContentTitle("Loading...")
            .setContentText("Updating tracks")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setSmallIcon(R.drawable.ic_baseline_arrow_download)
            .setAutoCancel(true)
            .setContentIntent(pIntent)
            .build()
        createChannel(context)
        return notification
    }

    companion object {
        const val NOTIFICATION_ID = 100
        const val CHANNEL_ID = "channel_id"
        const val CONTENT_REQUEST_CODE = 1
        const val TAG = "worker"
    }
}