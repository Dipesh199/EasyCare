package com.easycare.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import com.easycare.app.navigation.EasyCareApp
import com.easycare.app.preferences.UserPreferencesRepository
import com.easycare.app.repository.EasyCareRepository
import com.easycare.app.viewmodel.EasyCareViewModel
import com.easycare.app.voice.TextToSpeechManager

class MainActivity : ComponentActivity() {
    private val viewModel: EasyCareViewModel by viewModels {
        EasyCareViewModel.Factory(
            preferencesRepository = UserPreferencesRepository(applicationContext),
            repository = EasyCareRepository(),
        )
    }
    private lateinit var textToSpeech: TextToSpeechManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        textToSpeech = TextToSpeechManager(applicationContext)
        setContent { EasyCareApp(viewModel, textToSpeech) }
    }

    override fun onStop() {
        textToSpeech.stop()
        super.onStop()
    }

    override fun onDestroy() {
        textToSpeech.shutdown()
        super.onDestroy()
    }
}
