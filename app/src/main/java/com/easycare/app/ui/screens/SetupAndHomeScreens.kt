package com.easycare.app.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Message
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.FoodBank
import androidx.compose.material.icons.filled.Medication
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.easycare.app.model.UserPreferences
import com.easycare.app.ui.components.EmergencyButton
import com.easycare.app.ui.components.LargeActionButton
import com.easycare.app.ui.components.LargeMenuCard
import com.easycare.app.ui.components.VoiceButton
import com.easycare.app.ui.theme.AppSpacing
import kotlinx.coroutines.delay
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@Composable
fun LoadingScreen() {
    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        CircularProgressIndicator(modifier = Modifier.padding(AppSpacing.large))
    }
}

@Composable
fun SetupScreen(
    initial: UserPreferences,
    onSave: (UserPreferences) -> Unit,
) {
    var userName by remember(initial) { mutableStateOf(initial.userName) }
    var familyName by remember(initial) { mutableStateOf(initial.familyName) }
    var familyPhone by remember(initial) { mutableStateOf(initial.familyPhone) }
    var emergencyPhone by remember(initial) { mutableStateOf(initial.emergencyPhone) }
    var homeAddress by remember(initial) { mutableStateOf(initial.homeAddress) }
    val valid = userName.isNotBlank() && familyName.isNotBlank() && familyPhone.isNotBlank() && emergencyPhone.isNotBlank()

    Scaffold(containerColor = MaterialTheme.colorScheme.background) { padding ->
        Column(
            Modifier.fillMaxSize().padding(padding).verticalScroll(rememberScrollState()).padding(AppSpacing.large),
            verticalArrangement = Arrangement.spacedBy(AppSpacing.medium),
        ) {
            Text("Welcome to EasyCare", style = MaterialTheme.typography.headlineLarge)
            Text("A caregiver can set up the essentials now. You can change them later in Settings.", style = MaterialTheme.typography.bodyLarge)
            SetupField("My name", userName, { userName = it })
            SetupField("Family member name", familyName, { familyName = it })
            SetupField("Family phone number", familyPhone, { familyPhone = it }, KeyboardType.Phone)
            SetupField("Emergency contact phone", emergencyPhone, { emergencyPhone = it }, KeyboardType.Phone)
            SetupField("Home address", homeAddress, { homeAddress = it })
            Spacer(Modifier.height(AppSpacing.small))
            LargeActionButton(
                text = "Save and Continue",
                enabled = valid,
                onClick = {
                    onSave(
                        initial.copy(
                            userName = userName.trim(),
                            familyName = familyName.trim(),
                            familyPhone = familyPhone.trim(),
                            emergencyPhone = emergencyPhone.trim(),
                            homeAddress = homeAddress.trim(),
                            setupComplete = true,
                        ),
                    )
                },
            )
        }
    }
}

@Composable
private fun SetupField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    keyboardType: KeyboardType = KeyboardType.Text,
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label, fontSize = 18.sp) },
        textStyle = MaterialTheme.typography.bodyLarge,
        singleLine = true,
        keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
        modifier = Modifier.fillMaxWidth().heightIn(min = 76.dp),
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    preferences: UserPreferences,
    onSpeak: (String) -> Unit,
    onVoice: () -> Unit,
    onFood: () -> Unit,
    onFamily: () -> Unit,
    onMessage: () -> Unit,
    onMedicine: () -> Unit,
    onEmergency: () -> Unit,
    onSettings: () -> Unit,
) {
    var now by remember { mutableStateOf(LocalDateTime.now()) }
    LaunchedEffect(Unit) {
        onSpeak("Hello ${preferences.userName}. How can I help you?")
        while (true) {
            now = LocalDateTime.now()
            delay(1_000)
        }
    }
    val greeting = when (now.hour) {
        in 5..11 -> "Good Morning"
        in 12..17 -> "Good Afternoon"
        else -> "Good Evening"
    }
    Scaffold(
        topBar = {
            Row(
                Modifier.fillMaxWidth().padding(horizontal = AppSpacing.large, vertical = AppSpacing.small),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text("EasyCare", style = MaterialTheme.typography.titleLarge, color = MaterialTheme.colorScheme.primary, modifier = Modifier.weight(1f))
                IconButton(onClick = onSettings, modifier = Modifier.padding(4.dp)) {
                    Icon(Icons.Default.Settings, contentDescription = "Caregiver settings")
                }
            }
        },
    ) { padding ->
        Column(
            Modifier.fillMaxSize().padding(padding).padding(horizontal = AppSpacing.large),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text("$greeting ${preferences.userName}", style = MaterialTheme.typography.headlineMedium, textAlign = TextAlign.Center)
            Text(
                now.format(DateTimeFormatter.ofPattern("EEEE, d MMMM  •  HH:mm")),
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center,
            )
            Spacer(Modifier.height(AppSpacing.medium))
            VoiceButton(onClick = onVoice)
            Spacer(Modifier.height(AppSpacing.medium))
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(AppSpacing.medium)) {
                LargeMenuCard("Food", Icons.Default.FoodBank, "Choose food", onFood, Modifier.weight(1f))
                LargeMenuCard("Call Family", Icons.Default.Call, "Call family", onFamily, Modifier.weight(1f))
            }
            Spacer(Modifier.height(AppSpacing.medium))
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(AppSpacing.medium)) {
                LargeMenuCard("Message Family", Icons.AutoMirrored.Filled.Message, "Message family", onMessage, Modifier.weight(1f))
                LargeMenuCard("Medicine", Icons.Default.Medication, "Medicine reminders", onMedicine, Modifier.weight(1f))
            }
            Spacer(Modifier.weight(1f))
            EmergencyButton(onClick = onEmergency)
            Spacer(Modifier.height(AppSpacing.medium))
        }
    }
}
