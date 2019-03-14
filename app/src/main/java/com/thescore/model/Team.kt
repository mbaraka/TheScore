package com.thescore.model

import androidx.annotation.NonNull
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.thescore.database.DataTypeConverter
import java.io.Serializable

@Entity(tableName = "Team")
data class Team(
        val full_name: String,
        @PrimaryKey @NonNull val id: Int,
        val losses: Int,
        @TypeConverters(DataTypeConverter::class) val players: List<Player>,
        val wins: Int
) : Serializable {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Team

        if (id != other.id) return false

        return true
    }

    override fun hashCode(): Int {
        return id
    }
}