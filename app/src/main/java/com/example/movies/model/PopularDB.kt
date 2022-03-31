package com.example.movies.model

import android.graphics.Bitmap
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "popular_movie_table")
class PopularDB(
    @PrimaryKey
    var id: Int,

    @ColumnInfo(name = "original_title")
    var original_title : String,

    @ColumnInfo(name = "release_date")
    var release_date : String,

    @ColumnInfo(name = "rating")
    var rating : String,

    @ColumnInfo(name = "summary")
    var summary : String,

    @ColumnInfo(name = "backposter")
    var backposter: Bitmap,

    @ColumnInfo(name = "poster")
    var poster: Bitmap
)


