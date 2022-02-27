package teach.meskills.lastfm.chartTracks

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.*
import teach.meskills.lastfm.data.AudioEntity
import teach.meskills.lastfm.data.ContentRepository
import java.lang.Exception

class ChartViewModel(
    private val contentRepository: ContentRepository
) : ViewModel() {

    val trackLiveData = MutableLiveData<List<AudioEntity>>()
    private val isRefreshing = MutableLiveData<Boolean>()
    private val errorMessage = MutableLiveData<Boolean>()
    private val scope = CoroutineScope(Dispatchers.Main)

    init {
        openChart()
    }

    fun openChart() {
        scope.launch {
            isRefreshing.value = true
            errorMessage.value = false
            try {
                val chart = withContext(Dispatchers.IO) {
                    contentRepository.getMedia()
                }
                Log.d("res", chart.toString())
                trackLiveData.value = chart
            } catch (e: Exception) {
                if (trackLiveData.value.isNullOrEmpty()) {
                    errorMessage.value = true
                    Log.d("res", e.toString())
                }
            }
            isRefreshing.value = false
        }
    }

    override fun onCleared() {
        super.onCleared()
        scope.cancel()
    }
}
