package teach.meskills.lastfm

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.gson.Gson
import com.google.gson.JsonElement
import kotlinx.coroutines.*
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody
import java.lang.Exception
import java.security.MessageDigest

class UserViewModel : ViewModel(), ContentRepository {

    private val scope = CoroutineScope(Dispatchers.Main)
    private val okHttpClient = OkHttpClient.Builder().build()
    val userLiveData = MutableLiveData<User>()
    val isSuccessfullyEnter = MutableLiveData<Boolean>()
    val trackLiveData = MutableLiveData<List<AudioEntity>>()
    private val gson = Gson()

//    init {
//        openChart()
//    }

    fun onSignInClick(login: String, password: String) {
        scope.launch {
            try {
                val apiSignature =
                    "api_key" + APIKEY + "methodauth.getMobileSessionpassword" + password +
                            "username" + login + APISIG
                val hexString = StringBuilder()
                val digest: MessageDigest = MessageDigest.getInstance("MD5")
                digest.update(apiSignature.toByteArray(charset("UTF-8")))
                val messageDigest: ByteArray = digest.digest()
                for (aMessageDigest in messageDigest) {
                    var h = Integer.toHexString(0xFF and aMessageDigest.toInt())
                    while (h.length < 2) h = "0$h"
                    hexString.append(h)
                }
                val urlParameter =
                    "method=auth.getMobileSession&api_key=" + APIKEY + "&password=" + password +
                            "&username=" + login + "&api_sig=" + hexString + "&format=json"
                val urlAdress = "https://ws.audioscrobbler.com/2.0/?$urlParameter"

                val response = withContext(Dispatchers.IO) {
                    okHttpClient
                        .newCall(
                            Request.Builder()
                                .url(urlAdress)
                                .post(RequestBody.create(null, ByteArray(0)))
                                .build()
                        ).execute()
                }
                userLiveData.value = User(login, password)
                val jsonString = response.body?.string()
                Log.d("respon", jsonString.toString())
                val json = gson.fromJson(jsonString, JsonElement::class.java)
                Log.d("respon", response.code.toString())
//                isSuccessfullyEnter.value = response.code == 200
                isSuccessfullyEnter.value = response.isSuccessful
            } catch (e: Exception) {
                isSuccessfullyEnter.value = false
            }
        }
    }

    fun openChart() {
        scope.launch {
            try {
                val chart = withContext(Dispatchers.IO) {
                    getMedia()
                }
                Log.d("respon", chart.toString())
                isSuccessfullyEnter.value = true
                trackLiveData.value = chart
            } catch (e: Exception) {
                Log.d("respon", e.toString())
                if (trackLiveData.value.isNullOrEmpty()) {
                    isSuccessfullyEnter.value = false
                }
            }
        }
    }

    override suspend fun getMedia(): List<AudioEntity> {
        val urlParameter = "method=chart.gettoptracks&api_key=$APIKEY&format=json"
        val urlAdress = "https://ws.audioscrobbler.com/2.0/?$urlParameter"
        val response =
            okHttpClient
                .newCall(
                    Request.Builder()
                        .url(urlAdress)
                        .post(RequestBody.create(null, ByteArray(0)))
                        .build()
                ).execute()

        val jsonString = response.body?.string().orEmpty()
        Log.d("respon", jsonString)
        val json = gson.fromJson(jsonString, AudioResponseDTO::class.java)
        Log.d("respon", response.code.toString())
        val trackMap = json.tracks.track?.map {
            AudioEntity(
                artist = it.artist.name,
                name = it.name,
                image = it.image[1].text
            )
        }.orEmpty()
        return trackMap
    }

    override fun onCleared() {
        super.onCleared()
        scope.cancel()
    }

    companion object {
        const val APIKEY: String = "6323fb2dc68af795505f60f05c4323c1"
        const val APISIG: String = "3d788657600fa1a51146673fc38c3207"
    }
}

//2.0/?method=chart.gettoptracks&api_key=YOUR_API_KEY&format=json