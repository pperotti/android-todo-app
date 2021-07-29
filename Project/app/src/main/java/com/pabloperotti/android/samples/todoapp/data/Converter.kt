package com.example.todoapp.data

import androidx.room.TypeConverter
import com.pabloperotti.android.samples.todoapp.data.models.Priority

// Handles Priority <-> String conversions in the database
class Converter {

    @TypeConverter
    fun fromPriority(priority: Priority): String {
        return priority.name
    }

    @TypeConverter
    fun toPriority(priority: String): Priority {
        return Priority.valueOf(priority)
    }

}