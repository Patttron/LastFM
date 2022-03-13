package teach.meskills.lastfm.login

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.*
import teach.meskills.lastfm.data.ContentRepository
import teach.meskills.lastfm.data.User
import java.lang.Exception

class UserViewModel(
    private val contentRepository: ContentRepository,
    private val bgDispatcher: CoroutineDispatcher,
    private val mainDispatcher: CoroutineDispatcher
) : ViewModel() {

    private val scope = CoroutineScope(mainDispatcher)
    val userLiveData = MutableLiveData<User>()
    val isSuccessfullyEnter = MutableLiveData<Boolean>()
    val errorMessage = MutableLiveData<Boolean>()

    fun onSignInClick(login: String, password: String) {
        Dispatchers.Main
        scope.launch {
            try {
                val response = withContext(bgDispatcher) {
                    contentRepository.signIn(login, password)
                }
                isSuccessfullyEnter.value = response
                Log.d("respon", response.toString())
                if (response) {
                    userLiveData.value = User(login, password)
                } else {
                    errorMessage.value = true
                    isSuccessfullyEnter.value = false
                }
                Log.d("relivedata", isSuccessfullyEnter.value.toString())
            } catch (e: Exception) {
                e.stackTraceToString()
                isSuccessfullyEnter.value = false
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        scope.cancel()
    }
}
