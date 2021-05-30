package com.example.astroweather.database
///https://developer.android.com/training/data-storage/room#groovy
import android.content.Context
import androidx.room.*

@Database(entities = [Location::class], version = 2)
abstract class LocationDatabase: RoomDatabase() {
    companion object{
        @Volatile
        private var database:LocationDatabase? = null

        fun getDatabase(applicationContext: Context):LocationDatabase{
            if(database !=null){
                return database as LocationDatabase
            }
            synchronized(this){
                database =  Room.databaseBuilder(
                    applicationContext,
                    LocationDatabase::class.java, "database"
                ).build()
                return database  as LocationDatabase
            }

        }
    }
    abstract fun locationDao(): LocationDao
}