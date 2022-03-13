package teach.meskills.lastfm.data

interface ContentRepository {
    suspend fun getMedia(): List<AudioEntity>
    suspend fun signIn(login: String, password: String): Boolean
}