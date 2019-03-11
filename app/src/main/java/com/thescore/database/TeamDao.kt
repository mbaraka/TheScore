package com.thescore.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.thescore.model.Team

@Dao
interface TeamDao {

    @Query("SELECT * FROM team")
    fun getAll(): List<Team>

    @Insert
    fun insertAll(cities: List<Team>)

    @Delete
    fun delete(user: Team)
}