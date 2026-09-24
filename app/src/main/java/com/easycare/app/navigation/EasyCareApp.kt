package com.easycare.app.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.compose.runtime.getValue
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.easycare.app.core.openDialer
import com.easycare.app.core.openSms
import com.easycare.app.model.ContactTarget
import com.easycare.app.ui.screens.EmergencyScreen
import com.easycare.app.ui.screens.FamilyScreen
import com.easycare.app.ui.screens.FoodScreen
import com.easycare.app.ui.screens.HomeScreen
import com.easycare.app.ui.screens.LoadingScreen
import com.easycare.app.ui.screens.MedicineScreen
import com.easycare.app.ui.screens.MessageScreen
import com.easycare.app.ui.screens.OrderSuccessScreen
import com.easycare.app.ui.screens.SettingsScreen
import com.easycare.app.ui.screens.SetupScreen
import com.easycare.app.ui.screens.VoiceAssistantScreen
import com.easycare.app.ui.theme.EasyCareTheme
import com.easycare.app.viewmodel.EasyCareViewModel
import com.easycare.app.voice.TextToSpeechManager
import com.easycare.app.voice.VoiceCommand
import androidx.compose.ui.platform.LocalContext

private object Route {
    const val Bootstrap = "bootstrap"
    const val Setup = "setup"
    const val Home = "home"
    const val Voice = "voice"
    const val Food = "food"
    const val OrderSuccess = "order_success"
    const val Family = "family"
    const val Message = "message"
    const val Emergency = "emergency"
    const val Medicine = "medicine"
    const val Settings = "settings"
}

@Composable
fun EasyCareApp(viewModel: EasyCareViewModel, textToSpeech: TextToSpeechManager) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val navController = rememberNavController()
    val context = LocalContext.current
    val speak: (String) -> Unit = { text -> textToSpeech.speak(text, uiState.preferences.voiceEnabled) }

    EasyCareTheme(textSize = uiState.preferences.textSize) {
        NavHost(navController = navController, startDestination = Route.Bootstrap) {
            composable(Route.Bootstrap) {
                LoadingScreen()
                LaunchedEffect(uiState.loaded) {
                    if (uiState.loaded) {
                        navController.navigate(if (uiState.preferences.setupComplete) Route.Home else Route.Setup) {
                            popUpTo(Route.Bootstrap) { inclusive = true }
                        }
                    }
                }
            }
            composable(Route.Setup) {
                SetupScreen(uiState.preferences) { preferences ->
                    viewModel.savePreferences(preferences)
                    navController.navigate(Route.Home) { popUpTo(Route.Setup) { inclusive = true } }
                }
            }
            composable(Route.Home) {
                HomeScreen(
                    preferences = uiState.preferences,
                    onSpeak = speak,
                    onVoice = { navController.navigate(Route.Voice) },
                    onFood = { navController.navigate(Route.Food) },
                    onFamily = { navController.navigate(Route.Family) },
                    onMessage = { navController.navigate(Route.Message) },
                    onMedicine = { navController.navigate(Route.Medicine) },
                    onEmergency = { navController.navigate(Route.Emergency) },
                    onSettings = { navController.navigate(Route.Settings) },
                )
            }
            composable(Route.Voice) {
                VoiceAssistantScreen(
                    familyName = uiState.preferences.familyName,
                    onBack = navController::popBackStack,
                    onSpeak = speak,
                    onCommand = { command ->
                        if (command == VoiceCommand.CallFamily) {
                            navController.popBackStack()
                            openDialer(context, uiState.preferences.familyPhone)
                        } else {
                            navController.navigateFromVoice(command)
                        }
                    },
                )
            }
            composable(Route.Food) {
                FoodScreen(
                    meals = uiState.meals,
                    onBack = navController::popBackStack,
                    onSelect = { navController.navigate(Route.OrderSuccess) },
                    onSpeak = speak,
                )
            }
            composable(Route.OrderSuccess) {
                OrderSuccessScreen(
                    onHome = { navController.returnHome() },
                    onSpeak = speak,
                )
            }
            composable(Route.Family) {
                FamilyScreen(
                    name = uiState.preferences.familyName,
                    phone = uiState.preferences.familyPhone,
                    onBack = navController::popBackStack,
                    onCall = { openDialer(context, uiState.preferences.familyPhone) },
                    onMessage = { navController.navigate(Route.Message) },
                    onSpeak = speak,
                )
            }
            composable(Route.Message) {
                MessageScreen(
                    familyName = uiState.preferences.familyName,
                    onBack = navController::popBackStack,
                    onSend = { target, message ->
                        val phone = when (target) {
                            ContactTarget.Family -> uiState.preferences.familyPhone
                            ContactTarget.EmergencyContact -> uiState.preferences.emergencyPhone
                        }
                        openSms(context, phone, message)
                    },
                    onSpeak = speak,
                )
            }
            composable(Route.Emergency) {
                EmergencyScreen(
                    familyName = uiState.preferences.familyName,
                    familyPhone = uiState.preferences.familyPhone,
                    emergencyPhone = uiState.preferences.emergencyPhone,
                    onBack = navController::popBackStack,
                    onCallFamily = { openDialer(context, uiState.preferences.familyPhone) },
                    onCallEmergency = { openDialer(context, "112") },
                    onSpeak = speak,
                )
            }
            composable(Route.Medicine) {
                MedicineScreen(
                    medicines = uiState.medicines,
                    takenIds = uiState.takenMedicineIds,
                    onTaken = viewModel::markMedicineTaken,
                    onBack = navController::popBackStack,
                    onSpeak = speak,
                )
            }
            composable(Route.Settings) {
                SettingsScreen(
                    initial = uiState.preferences,
                    onBack = navController::popBackStack,
                    onSave = {
                        viewModel.savePreferences(it)
                        navController.popBackStack()
                    },
                )
            }
        }
    }
}

private fun NavHostController.navigateFromVoice(command: VoiceCommand) {
    val destination = when (command) {
        VoiceCommand.Food -> Route.Food
        VoiceCommand.CallFamily -> return
        VoiceCommand.MessageFamily -> Route.Message
        VoiceCommand.Emergency -> Route.Emergency
        VoiceCommand.Medicine -> Route.Medicine
        VoiceCommand.Unknown -> return
    }
    navigate(destination) { popUpTo(Route.Voice) { inclusive = true } }
}

private fun NavHostController.returnHome() {
    navigate(Route.Home) {
        popUpTo(Route.Home) { inclusive = false }
        launchSingleTop = true
    }
}
