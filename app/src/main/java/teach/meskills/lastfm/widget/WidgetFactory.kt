package teach.meskills.lastfm.widget

import android.content.Context
import android.content.Intent
import android.widget.RemoteViews
import android.widget.RemoteViewsService.RemoteViewsFactory
import teach.meskills.lastfm.R
import teach.meskills.lastfm.data.AppDatabase
import teach.meskills.lastfm.data.AudioEntity

class WidgetFactory(var context: Context, intent: Intent) : RemoteViewsFactory {

    lateinit var appDatabase: AppDatabase
    private lateinit var audio: List<AudioEntity>

    override fun onCreate() {
        audio = emptyList()
        appDatabase = AppDatabase.build(context)
    }

    override fun getCount(): Int = audio.size

    override fun getItemId(position: Int): Long = position.toLong()

    override fun getLoadingView(): RemoteViews? = null

    override fun getViewAt(position: Int): RemoteViews {
        val item = audio[position]
        val remoteViews = RemoteViews(
            context.packageName,
            R.layout.widget_item
        )
        remoteViews.setTextViewText(R.id.nameWidget,"Song: ${item.name}")
        remoteViews.setTextViewText(R.id.artistWidget, "Artist: ${item.artist}")
        return remoteViews
    }

    override fun getViewTypeCount(): Int = 1

    override fun hasStableIds(): Boolean = true

    override fun onDataSetChanged() {
        audio = appDatabase.audioDao().getAudio().take(3)
    }

    override fun onDestroy() {}
}
