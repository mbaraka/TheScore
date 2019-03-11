package com.thescore.database

import android.content.Context
import androidx.room.Room
import com.thescore.R

object DBHelper {
    lateinit var database: AppDatabase
        private set

    fun start(context: Context) {
        database = Room.databaseBuilder(
            context,
            AppDatabase::class.java, context.getString(R.string.app_name)
        ).build()
    }
}