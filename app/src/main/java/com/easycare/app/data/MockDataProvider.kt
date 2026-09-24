package com.easycare.app.data

import com.easycare.app.model.Meal
import com.easycare.app.model.Medicine

object MockDataProvider {
    val meals = listOf(
        Meal("gujarati", "Gujarati Thali", "Dal, rice, sabzi and 4 rotis", 10),
        Meal("khichdi", "Khichdi Kadhi", "Comforting khichdi with warm kadhi", 8),
        Meal("punjabi", "Punjabi Thali", "Dal, paneer, rice and rotis", 12),
        Meal("home", "Simple Home Meal", "A light, home-style meal", 7),
    )

    val medicines = listOf(
        Medicine("b12", "Morning", "Vitamin B12", "08:00"),
        Medicine("blood_pressure", "Afternoon", "Blood Pressure Medicine", "14:00"),
        Medicine("vitamin_d", "Evening", "Vitamin D", "19:00"),
    )

    fun meal(id: String): Meal? = meals.firstOrNull { it.id == id }
}
