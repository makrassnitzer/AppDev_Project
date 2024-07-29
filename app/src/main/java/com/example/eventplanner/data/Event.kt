package com.example.eventplanner.data

data class Event(
    val id: String,
    val bezeichnung: String,
    val eventart: String,
    val datum: String,
    val uhrzeit: String,
    val standort: String,
    val teilnehmer: String,
    val ausgaben: Double,
    val additionalInfoPath: String?
)
