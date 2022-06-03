package lab.maxb.dark.presentation.repository.room.converters

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.lang.reflect.Type


class CollectionsConverter {
    @TypeConverter
    fun fromList(list: List<String>?) = list?.let {
        gson.toJson(list, typeList)
    }

    @TypeConverter
    fun toList(list: String?) = list?.let {
        gson.fromJson<List<String>>(list, typeList)
    }

    @TypeConverter
    fun fromSet(list: Set<String>?) = list?.let {
        gson.toJson(list, typeSet)
    }

    @TypeConverter
    fun toSet(list: String?) = list?.let {
        gson.fromJson<Set<String>>(list, typeSet)
    }

    companion object {
        private val gson = Gson()
        private val typeList: Type = object : TypeToken<List<String>?>() {}.type
        private val typeSet: Type = object : TypeToken<Set<String>?>() {}.type
    }
}