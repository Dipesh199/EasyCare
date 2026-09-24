package com.easycare.app.repository

import com.easycare.app.data.MockDataProvider
import com.easycare.app.model.Meal
import com.easycare.app.model.Medicine

class EasyCareRepository {
    val meals: List<Meal> = MockDataProvider.meals
    val medicines: List<Medicine> = MockDataProvider.medicines

    fun meal(id: String): Meal? = MockDataProvider.meal(id)
}
