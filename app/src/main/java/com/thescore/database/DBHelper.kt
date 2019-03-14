package com.thescore.database

import android.content.Context
import androidx.room.Room
import com.thescore.R
import com.thescore.dagger.AppContextModule
import javax.inject.Inject

open class DBHelper @Inject constructor(@AppContextModule.AppContext private val context: Context) {
    private var database: AppDatabase? = null
    var testMode = false

    fun getDatabase(): AppDatabase {
        if (database == null) {
            database = if (testMode) {
                Room.inMemoryDatabaseBuilder(context, AppDatabase::class.java).allowMainThreadQueries().build()
            } else {
                Room.databaseBuilder(context, AppDatabase::class.java, context.getString(R.string.app_name))
                    .build()
            }
        }
        return database!!
    }
}