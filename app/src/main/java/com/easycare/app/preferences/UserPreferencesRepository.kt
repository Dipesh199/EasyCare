package com.easycare.app.preferences

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.easycare.app.model.TextSize
import com.easycare.app.model.UserPreferences
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.easyCareDataStore by preferencesDataStore(name = "easycare_preferences")

class UserPreferencesRepository(private val context: Context) {
    private object Keys {
        val userName = stringPreferencesKey("user_name")
        val familyName = stringPreferencesKey("family_name")
        val familyPhone = stringPreferencesKey("family_phone")
        val emergencyPhone = stringPreferencesKey("emergency_phone")
        val homeAddress = stringPreferencesKey("home_address")
        val voiceEnabled = booleanPreferencesKey("voice_enabled")
        val textSize = stringPreferencesKey("text_size")
        val setupComplete = booleanPreferencesKey("setup_complete")
    }

    val preferences: Flow<UserPreferences> = context.easyCareDataStore.data.map { values ->
        UserPreferences(
            userName = values[Keys.userName] ?: "Ramesh",
            familyName = values[Keys.familyName] ?: "Dipesh",
            familyPhone = values[Keys.familyPhone] ?: "+491234567890",
            emergencyPhone = values[Keys.emergencyPhone] ?: "+491234567890",
            homeAddress = values[Keys.homeAddress].orEmpty(),
            voiceEnabled = values[Keys.voiceEnabled] ?: true,
            textSize = runCatching {
                TextSize.valueOf(values[Keys.textSize] ?: TextSize.Large.name)
            }.getOrDefault(TextSize.Large),
            setupComplete = values[Keys.setupComplete] ?: false,
        )
    }

    suspend fun save(userPreferences: UserPreferences) {
        context.easyCareDataStore.edit { values ->
            values[Keys.userName] = userPreferences.userName
            values[Keys.familyName] = userPreferences.familyName
            values[Keys.familyPhone] = userPreferences.familyPhone
            values[Keys.emergencyPhone] = userPreferences.emergencyPhone
            values[Keys.homeAddress] = userPreferences.homeAddress
            values[Keys.voiceEnabled] = userPreferences.voiceEnabled
            values[Keys.textSize] = userPreferences.textSize.name
            values[Keys.setupComplete] = userPreferences.setupComplete
        }
    }
}
