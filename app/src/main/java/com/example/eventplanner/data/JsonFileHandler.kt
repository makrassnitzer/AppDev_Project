package com.example.eventplanner.data

import android.content.Context
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.io.File
import java.io.FileReader
import java.io.FileWriter

class JsonFileHandler(private val context: Context) {

    private val gson = Gson()
    private val fileName = "events.json"

    fun saveEvents(events: List<Event>) {
        val jsonString = gson.toJson(events)
        val file = File(context.filesDir, fileName)
        FileWriter(file).use {
            it.write(jsonString)
        }
    }

    fun loadEvents(): List<Event> {
        val file = File(context.filesDir, fileName)
        if (!file.exists()) return emptyList()

        FileReader(file).use {
            val eventType = object : TypeToken<List<Event>>() {}.type
            return gson.fromJson(it, eventType)
        }
    }

    fun addEvent(event: Event) {
        val events = loadEvents().toMutableList()
        events.add(event)
        saveEvents(events)
    }

    fun getEventById(id: Int): Event? {
        return loadEvents().find { it.id == id }
    }

    fun updateEvent(updatedEvent: Event) {
        val events = loadEvents().toMutableList()
        val index = events.indexOfFirst { it.id == updatedEvent.id }
        if (index != -1) {
            events[index] = updatedEvent
            saveEvents(events)
        }
    }

    fun deleteEvent(id: Int) {
        val events = loadEvents().toMutableList()
        events.removeAll { it.id == id }
        saveEvents(events)
    }
}