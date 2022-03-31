package com.example.movies.database

import android.content.Context
import android.content.IntentFilter
import androidx.room.*
import com.example.movies.Converters
import com.example.movies.dao.PopularMovieDao
import com.example.movies.model.PopularDB

@Database(entities = arrayOf((PopularDB::class)),version = 1)
@TypeConverters(Converters::class)
abstract class PopularMovieDatabase : RoomDatabase() {

    abstract fun popularMovieDao() : PopularMovieDao

    companion object{

        private var INSTANCE : PopularMovieDatabase? = null

        fun getDatabase (context : Context) : PopularMovieDatabase{

            val temInstance = INSTANCE
            if (temInstance != null){
                return temInstance
            }
            synchronized(this){
                val instance = Room.databaseBuilder(
                    context,
                    PopularMovieDatabase::class.java,
                    "PopularMovieDB"
                ).allowMainThreadQueries().build()
                INSTANCE = instance
                return instance
            }
        }
    }
}