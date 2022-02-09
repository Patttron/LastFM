package teach.meskills.lastfm

interface ContentRepository {
    suspend fun getMedia(): List<AudioEntity>
}