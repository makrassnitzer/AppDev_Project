package com.example.eventplanner.data

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.io.File

object JsonFileUtil {
    private val gson = Gson()

    fun readFromFile(file: File): List<Event> {
        return if (file.exists()) {
            val json = file.readText()
            val eventType = object : TypeToken<List<Event>>() {}.type
            gson.fromJson(json, eventType)
        } else {
            emptyList()
        }
    }

    fun writeToFile(file: File, events: List<Event>) {
        val json = gson.toJson(events)
        file.writeText(json)
    }
}
