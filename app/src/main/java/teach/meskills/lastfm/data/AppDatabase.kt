package teach.meskills.lastfm.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [AudioEntity::class], version = 1)
abstract class AppDatabase : RoomDatabase() {

    abstract fun audioDao(): AudioDao

    companion object {
        fun build(context: Context): AppDatabase {
            return Room.databaseBuilder(context, AppDatabase::class.java, "AppDatabase").build()
        }
    }
}