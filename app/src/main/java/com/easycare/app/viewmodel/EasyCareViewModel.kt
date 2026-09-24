package com.easycare.app.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.easycare.app.model.Meal
import com.easycare.app.model.Medicine
import com.easycare.app.model.UserPreferences
import com.easycare.app.preferences.UserPreferencesRepository
import com.easycare.app.repository.EasyCareRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

data class EasyCareUiState(
    val loaded: Boolean = false,
    val preferences: UserPreferences = UserPreferences(),
    val meals: List<Meal> = emptyList(),
    val medicines: List<Medicine> = emptyList(),
    val takenMedicineIds: Set<String> = emptySet(),
)

class EasyCareViewModel(
    private val preferencesRepository: UserPreferencesRepository,
    repository: EasyCareRepository,
) : ViewModel() {
    private val takenMedicineIds = MutableStateFlow<Set<String>>(emptySet())

    val uiState: StateFlow<EasyCareUiState> = combine(
        preferencesRepository.preferences,
        takenMedicineIds,
    ) { preferences, takenIds ->
        EasyCareUiState(
            loaded = true,
            preferences = preferences,
            meals = repository.meals,
            medicines = repository.medicines,
            takenMedicineIds = takenIds,
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), EasyCareUiState())

    fun savePreferences(preferences: UserPreferences) {
        viewModelScope.launch { preferencesRepository.save(preferences) }
    }

    fun markMedicineTaken(id: String) {
        takenMedicineIds.value += id
    }

    class Factory(
        private val preferencesRepository: UserPreferencesRepository,
        private val repository: EasyCareRepository,
    ) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T =
            EasyCareViewModel(preferencesRepository, repository) as T
    }
}
