package com.thescore.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.thescore.model.Team

@Database(entities = [Team::class],
        version = 1, exportSchema = false)
@TypeConverters(DataTypeConverter::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun teamsDao(): TeamDao
}
