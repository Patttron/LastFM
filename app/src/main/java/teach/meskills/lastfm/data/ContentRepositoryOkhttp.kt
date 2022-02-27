package teach.meskills.lastfm.data

import android.util.Log
import com.google.gson.Gson
import com.google.gson.JsonElement
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody
import teach.meskills.lastfm.chartTracks.AudioResponseDTO
import java.lang.Exception
import java.security.MessageDigest

class ContentRepositoryOkhttp(
    private val appDatabase: AppDatabase
) : ContentRepository {
    private val gson = Gson()
    private val okHttpClient = OkHttpClient.Builder().build()

    override suspend fun signIn(login: String, password: String): Boolean {
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
            Log.d("re", urlAdress)
            val response =
                okHttpClient
                    .newCall(
                        Request.Builder()
                            .url(urlAdress)
                            .post(RequestBody.create(null, ByteArray(0)))
                            .build()
                    ).execute()
            val jsonString = response.body?.string()
            Log.d("respon", jsonString.toString())
            gson.fromJson(jsonString, JsonElement::class.java)
            Log.d("respon", response.code.toString())
            return response.isSuccessful
        } catch (e: Exception) {
            return false
        }
    }

    override suspend fun getMedia(): List<AudioEntity> {
        return try {
            val urlParameter =
                "method=chart.gettoptracks&api_key=$APIKEY&format=json"
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
            appDatabase.audioDao().saveAudio(trackMap)
            trackMap
        } catch (e: Exception) {
            appDatabase.audioDao().getAudio()
        }
    }

    companion object {
        const val APIKEY: String = "6323fb2dc68af795505f60f05c4323c1"
        const val APISIG: String = "3d788657600fa1a51146673fc38c3207"
    }
}
