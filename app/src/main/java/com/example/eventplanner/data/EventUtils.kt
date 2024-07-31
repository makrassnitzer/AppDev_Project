package com.example.eventplanner.data

import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.annotation.RequiresApi
import com.example.eventplanner.NotificationReceiver
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import java.io.File
import java.time.LocalDateTime
import java.time.ZoneId
import java.util.Calendar
import java.util.UUID

object EventUtils {

    @RequiresApi(Build.VERSION_CODES.O)
    private val gson: Gson = GsonBuilder()
        .registerTypeAdapter(LocalDateTime::class.java, LocalDateTimeAdapter())
        .create()

    fun generateId(): String {
        return UUID.randomUUID().toString()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun saveEventToFile(event: Event, context: Context) {
        val events = loadEventsFromFile(context).toMutableList()
        events.add(event)
        saveEventsToFile(events, context)
        scheduleNotification(event, context)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun loadEventsFromFile(context: Context): List<Event> {
        val file = File(context.filesDir, "events.json")
        if (!file.exists()) return emptyList()
        val json = file.readText()
        val type = object : TypeToken<List<Event>>() {}.type
        return gson.fromJson(json, type)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun saveEventsToFile(events: List<Event>, context: Context) {
        val json = gson.toJson(events)
        val file = File(context.filesDir, "events.json")
        file.writeText(json)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun getEventById(eventId: String, context: Context): Event? {
        val events = loadEventsFromFile(context)
        return events.find { it.id == eventId }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun updateEventById(eventId: String, updatedEvent: Event, context: Context) {
        val events = loadEventsFromFile(context).toMutableList()
        val index = events.indexOfFirst { it.id == eventId }
        if (index != -1) {
            events[index] = updatedEvent
            saveEventsToFile(events, context)
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun deleteEventById(eventId: String, context: Context) {
        val events = loadEventsFromFile(context).toMutableList()
        val index = events.indexOfFirst { it.id == eventId }
        if (index != -1) {
            events.removeAt(index)
            saveEventsToFile(events, context)
        }
    }

    @SuppressLint("ScheduleExactAlarm")
    @RequiresApi(Build.VERSION_CODES.O)
    private fun scheduleNotification(event: Event, context: Context) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(context, NotificationReceiver::class.java).apply {
            putExtra("eventName", event.bezeichnung)
        }

        val pendingIntent = PendingIntent.getBroadcast(
            context,
            0,
            intent,
            PendingIntent.FLAG_IMMUTABLE // Add this flag
        )

        val eventDateTime = event.datum.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()
        val calendar = Calendar.getInstance().apply {
            timeInMillis = eventDateTime
            add(Calendar.DAY_OF_YEAR, -1)
        }

        alarmManager.setExact(
            AlarmManager.RTC_WAKEUP,
            calendar.timeInMillis,
            pendingIntent
        )
    }
}
