package com.thescore.database

import android.content.Context
import androidx.room.Room
import com.thescore.R
import com.thescore.dagger.AppContextModule
import com.thescore.model.Team
import javax.inject.Inject

open class DBHelper @Inject constructor(@AppContextModule.AppContext private val context: Context) {
    private var database: AppDatabase? = null
    var testMode = false

    init {
            database = if (testMode) {
                Room.inMemoryDatabaseBuilder(context, AppDatabase::class.java).allowMainThreadQueries().build()
            } else {
                Room.databaseBuilder(context, AppDatabase::class.java, context.getString(R.string.app_name))
                    .build()
            }
    }

    open fun getAllTeams(): List<Team> {
        if (testMode) {
            return ArrayList()
        }
        return database!!.teamsDao().getAll()
    }

    open fun insertTeams(teams: List<Team>) {
        if (!testMode) {
            database!!.teamsDao().insertAll(teams)
        }
    }

    fun updateTeams(teams: List<Team>) {
        if (!testMode) {
            database!!.teamsDao().insertAll(teams)
        }
    }
}