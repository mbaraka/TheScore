package com.thescore.database

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.thescore.model.Player
import java.util.*

class DataTypeConverter {
    companion object {
        private val gson = Gson()

        @TypeConverter
        @JvmStatic
        fun stringToPlayerList(data: String?): MutableList<Player> {
            if (data == null) {
                return Collections.emptyList()
            }

            val listType = object : TypeToken<MutableList<Player>>() {}.type
            return gson.fromJson(data, listType)
        }

        @TypeConverter
        @JvmStatic
        fun playerListToString(someObjects: MutableList<Player>): String {
            return gson.toJson(someObjects)
        }

//        //
//        @TypeConverter
//        @JvmStatic
//        fun toCoord(data: String?): Coord {
//            val listType = object : TypeToken<Coord>() {}.type
//            return gson.fromJson<Coord>(data, listType)
//        }
//
//        @TypeConverter
//        @JvmStatic
//        fun fromCoord(someObjects: Coord): String {
//            return gson.toJson(someObjects)
//        }
    }

}