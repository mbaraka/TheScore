package com.thescore.model

import androidx.annotation.NonNull
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.thescore.database.DataTypeConverter

@Entity(tableName = "Team")
data class Team(
        val full_name: String,
        @PrimaryKey @NonNull val id: Int,
        val losses: Int,
        @TypeConverters(DataTypeConverter::class) val players: List<Player>,
        val wins: Int
)