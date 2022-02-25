package teach.meskills.lastfm.widget

import android.content.Context
import android.content.Intent
import android.widget.RemoteViewsService

class WidgetService : RemoteViewsService() {
    override fun onGetViewFactory(intent: Intent): RemoteViewsFactory {
        return WidgetFactory(
            applicationContext, intent
        )
    }

    companion object {
        fun getIntent(context: Context): Intent {
            return Intent(context, WidgetService::class.java)
        }
    }
}