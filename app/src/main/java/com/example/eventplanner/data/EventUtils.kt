package com.example.eventplanner.data

import android.content.Context
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.io.File
import java.util.UUID

object EventUtils {

    fun generateId(): String {
        return UUID.randomUUID().toString()
    }

    fun saveEventToFile(event: Event, context: Context) {
        val events = loadEventsFromFile(context).toMutableList()
        events.add(event)
        saveEventsToFile(events, context)
    }

    fun loadEventsFromFile(context: Context): List<Event> {
        val file = File(context.filesDir, "events.json")
        if (!file.exists()) return emptyList()
        val json = file.readText()
        val type = object : TypeToken<List<Event>>() {}.type
        return Gson().fromJson(json, type)
    }

    fun saveEventsToFile(events: List<Event>, context: Context) {
        val json = Gson().toJson(events)
        val file = File(context.filesDir, "events.json")
        file.writeText(json)
    }

    fun getEventById(eventId: String, context: Context): Event? {
        val events = loadEventsFromFile(context)
        return events.find { it.id == eventId }
    }

    fun updateEventById(eventId: String, updatedEvent: Event, context: Context) {
        val events = loadEventsFromFile(context).toMutableList()
        val index = events.indexOfFirst { it.id == eventId }
        if (index != -1) {
            events[index] = updatedEvent
            saveEventsToFile(events, context)
        }
    }

    fun deleteEventById(eventId: String, context: Context) {
        val events = loadEventsFromFile(context).toMutableList()
        val index = events.indexOfFirst { it.id == eventId }
        if (index != -1) {
            events.removeAt(index)
            saveEventsToFile(events, context)
        }
    }
}
