package teach.meskills.lastfm.di

import kotlinx.coroutines.Dispatchers
import okhttp3.OkHttpClient
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import teach.meskills.lastfm.chart_tracks.ChartViewModel
import teach.meskills.lastfm.data.AppDatabase
import teach.meskills.lastfm.data.ContentRepository
import teach.meskills.lastfm.data.ContentRepositoryOkhttp
import teach.meskills.lastfm.login.UserViewModel

val appDatabase = module {
    single<AppDatabase> { AppDatabase.build(context = get()) }
}

val contentRepository = module {
    single<AppDatabase> { AppDatabase.build(context = get()) }
    single<ContentRepository> { ContentRepositoryOkhttp(appDatabase = get()) }
}

val okHttpClientModule = module {
    single<OkHttpClient> { OkHttpClient.Builder().build() }
}

val loginModule = module {
    viewModel {
        UserViewModel(
            contentRepository = get(),
            bgDispatcher = Dispatchers.IO,
            mainDispatcher = Dispatchers.Main
        )
    }
}

val chartModule = module {
    viewModel {
        ChartViewModel(get())
    }
}
