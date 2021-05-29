package com.example.astroweather.database
///https://developer.android.com/training/data-storage/room#groovy
import android.content.Context
import androidx.room.*

@Database(entities = [Location::class], version = 1)
abstract class LocationDatabase: RoomDatabase() {
    companion object{
        fun getInstance(applicationContext: Context,databaseName:String){
            Room.databaseBuilder(
                applicationContext,
                LocationDatabase::class.java, databaseName
            ).build()
        }
    }
    abstract fun locationDao(): LocationDao
}