package teach.meskills.lastfm

import android.app.Application
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import teach.meskills.lastfm.di.*

class LastFmApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@LastFmApplication)
            modules(
                appDatabase,
                contentRepository,
                loginModule,
                chartModule,
                okHttpClientModule
            )
        }
    }
}