package com.easycare.app.model

data class UserPreferences(
    val userName: String = "Ramesh",
    val familyName: String = "Dipesh",
    val familyPhone: String = "+491234567890",
    val emergencyPhone: String = "+491234567890",
    val homeAddress: String = "",
    val voiceEnabled: Boolean = true,
    val textSize: TextSize = TextSize.Large,
    val setupComplete: Boolean = false,
)

enum class TextSize(val label: String, val multiplier: Float) {
    Normal("Normal", 1f),
    Large("Large", 1.12f),
    ExtraLarge("Extra Large", 1.25f),
}

enum class ContactTarget {
    Family,
    EmergencyContact,
}

data class Meal(
    val id: String,
    val name: String,
    val description: String,
    val priceEuros: Int,
)

data class Medicine(
    val id: String,
    val period: String,
    val name: String,
    val time: String,
)
