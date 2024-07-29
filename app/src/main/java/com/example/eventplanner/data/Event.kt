package com.example.eventplanner.data

import java.time.LocalDateTime

data class Event(
    val id: String,
    val bezeichnung: String,
    val eventart: String,
    val datum: LocalDateTime,
    val standort: String,
    val teilnehmer: String,
    val ausgaben: Double,
    val additionalInfoPath: String?
)