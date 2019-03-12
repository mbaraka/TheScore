package com.thescore.model

import androidx.annotation.NonNull
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable


data class Player(
        val first_name: String,
        @PrimaryKey @NonNull val id: Int,
        val last_name: String,
        val number: Int,
        val position: String
) :Serializable