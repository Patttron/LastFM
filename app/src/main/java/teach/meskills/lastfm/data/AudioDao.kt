package teach.meskills.lastfm.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface AudioDao {
    @Insert
    fun saveAudio(audio: List<AudioEntity>)

    @Query("SELECT * FROM AudioEntity")
    fun getAudio(): List<AudioEntity>
}