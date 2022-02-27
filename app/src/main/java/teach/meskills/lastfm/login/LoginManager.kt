package teach.meskills.lastfm.login

import androidx.lifecycle.MutableLiveData
import teach.meskills.lastfm.data.User

class LoginManager(private val prefs: CustomPreference) {
    val isLoggedInLiveData = MutableLiveData<Boolean?>()

    init {
        isLoggedInLiveData.value = prefs.login != "" && prefs.password != ""
    }

    var isLoggedIn: Boolean? = null
        get() = isLoggedInLiveData.value ?: false

    fun logIn(user: User) {
        prefs.login = user.userName
        prefs.password = user.password
        isLoggedInLiveData.value = true
    }

    fun logOut() {
        prefs.login = ""
        prefs.password = ""
        isLoggedInLiveData.value = false
    }
}