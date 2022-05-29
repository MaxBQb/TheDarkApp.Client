package lab.maxb.dark.presentation.repository.room.converters

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.lang.reflect.Type


class ListConverter {
    @TypeConverter
    fun fromList(list: List<String>?) = list?.let {
        gson.toJson(list, type)
    }

    @TypeConverter
    fun toList(list: String?) = list?.let {
        gson.fromJson<List<String>>(list, type)
    }

    companion object {
        private val gson = Gson()
        private val type: Type = object : TypeToken<List<String>?>() {}.type
    }
}