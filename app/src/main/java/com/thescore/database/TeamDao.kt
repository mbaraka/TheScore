package com.thescore.database

import androidx.room.*
import com.thescore.model.Team

@Dao
interface TeamDao {

    @Query("SELECT * FROM team")
    fun getAll(): List<Team>

    @Insert
    fun insertAll(data: List<Team>)

    @Update
    fun update(data: List<Team>)

    @Delete
    fun delete(data: Team)
}