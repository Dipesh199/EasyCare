package com.easycare.app.ui.screens

import android.Manifest
import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Message
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.Emergency
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import com.easycare.app.core.openSms
import com.easycare.app.location.LocationProvider
import com.easycare.app.model.ContactTarget
import com.easycare.app.model.Medicine
import com.easycare.app.model.TextSize
import com.easycare.app.model.UserPreferences
import com.easycare.app.ui.components.LargeActionButton
import com.easycare.app.ui.components.ContactTargetSelector
import com.easycare.app.ui.components.MedicineCard
import com.easycare.app.ui.components.SecondaryActionButton
import com.easycare.app.ui.components.SimpleTopBar
import com.easycare.app.ui.theme.AppSpacing
import com.easycare.app.voice.SpeechRecognizerManager
import kotlinx.coroutines.launch

@Composable
fun EmergencyScreen(
    familyName: String,
    familyPhone: String,
    emergencyPhone: String,
    onBack: () -> Unit,
    onCallFamily: () -> Unit,
    onCallEmergency: () -> Unit,
    onSpeak: (String) -> Unit,
) {
    val context = LocalContext.current
    val locationProvider = remember { LocationProvider(context.applicationContext) }
    val scope = rememberCoroutineScope()
    var status by remember { mutableStateOf("") }
    var listening by remember { mutableStateOf(false) }
    var selectedContact by remember { mutableStateOf(ContactTarget.EmergencyContact) }
    var pendingMessage by remember { mutableStateOf("I need help. Please contact me immediately.") }
    var pendingPhone by remember { mutableStateOf(emergencyPhone) }
    var dictatedMessage by remember { mutableStateOf<String?>(null) }
    var spokenPreview by remember { mutableStateOf("") }

    fun phoneFor(target: ContactTarget): String = when (target) {
        ContactTarget.Family -> familyPhone
        ContactTarget.EmergencyContact -> emergencyPhone
    }

    fun openEmergencyMessage(messageText: String, phone: String, includeLocation: Boolean) {
        scope.launch {
            if (includeLocation) status = "Getting your location…"
            val coordinates = if (includeLocation) locationProvider.currentLocation() else null
            val message = buildString {
                append(messageText)
                coordinates?.let { append("\n\nMy location:\n${it.mapsUrl}") }
            }
            status = if (openSms(context, phone, message)) {
                if (coordinates == null) "SMS opened and is ready to send." else "SMS opened with your location."
            } else {
                "No SMS app is available on this device."
            }
        }
    }

    val locationPermissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions(),
    ) { permissions ->
        if (permissions.values.any { it }) openEmergencyMessage(pendingMessage, pendingPhone, includeLocation = true)
        else {
            status = "Location permission was not allowed. The message will be opened without location."
            openEmergencyMessage(pendingMessage, pendingPhone, includeLocation = false)
        }
    }

    fun prepareEmergencyMessage(messageText: String) {
        pendingMessage = messageText
        pendingPhone = phoneFor(selectedContact)
        if (locationProvider.hasPermission()) {
            openEmergencyMessage(messageText, pendingPhone, includeLocation = true)
        } else {
            locationPermissionLauncher.launch(
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION),
            )
        }
    }

    val recognizer = remember {
        SpeechRecognizerManager(
            context,
            onListeningChanged = { listening = it },
            onResult = {
                spokenPreview = it
                dictatedMessage = it
            },
            onError = { status = it },
        )
    }
    DisposableEffect(recognizer) { onDispose { recognizer.destroy() } }
    val microphonePermissionLauncher = rememberLauncherForActivityResult(ActivityResultContracts.RequestPermission()) { granted ->
        if (granted) recognizer.start()
        else status = "Microphone permission is needed to speak an emergency message. You can still send the quick alert."
    }
    fun speakEmergencyMessage() {
        status = "Speak your emergency message now."
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_GRANTED) {
            recognizer.start()
        } else {
            microphonePermissionLauncher.launch(Manifest.permission.RECORD_AUDIO)
        }
    }

    LaunchedEffect(dictatedMessage) {
        val spokenText = dictatedMessage ?: return@LaunchedEffect
        dictatedMessage = null
        status = "I heard: “$spokenText”"
        prepareEmergencyMessage("I need help. $spokenText")
    }

    LaunchedEffect(Unit) { onSpeak("Do you need emergency help?") }
    Scaffold(topBar = { SimpleTopBar("Emergency Help", onBack) }) { padding ->
        Column(
            Modifier.fillMaxSize().padding(padding).verticalScroll(rememberScrollState()).padding(AppSpacing.large),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Card(colors = CardDefaults.cardColors(MaterialTheme.colorScheme.errorContainer)) {
                Column(Modifier.fillMaxWidth().padding(AppSpacing.large), horizontalAlignment = Alignment.CenterHorizontally) {
                    androidx.compose.material3.Icon(
                        Icons.Default.Emergency,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.error,
                        modifier = Modifier.size(64.dp),
                    )
                    Text("Do you need help?", style = MaterialTheme.typography.headlineLarge, textAlign = TextAlign.Center)
                }
            }
            Spacer(Modifier.height(AppSpacing.large))
            ContactTargetSelector(
                selected = selectedContact,
                familyName = familyName,
                onSelected = { selectedContact = it },
            )
            Spacer(Modifier.height(AppSpacing.medium))
            LargeActionButton("CALL $familyName", onCallFamily, icon = Icons.Default.Call)
            Spacer(Modifier.height(AppSpacing.medium))
            LargeActionButton(
                "SEND QUICK ALERT",
                onClick = { prepareEmergencyMessage("I need help. Please contact me immediately.") },
                icon = Icons.AutoMirrored.Filled.Message,
            )
            Spacer(Modifier.height(AppSpacing.medium))
            LargeActionButton(
                text = if (listening) "LISTENING…" else "SPEAK EMERGENCY MESSAGE",
                onClick = ::speakEmergencyMessage,
                icon = Icons.Default.Mic,
                enabled = !listening,
            )
            Spacer(Modifier.height(AppSpacing.medium))
            LargeActionButton("CALL EMERGENCY SERVICES", onCallEmergency, icon = Icons.Default.Emergency)
            if (spokenPreview.isNotBlank()) {
                Spacer(Modifier.height(AppSpacing.medium))
                Text(
                    "Your message: “$spokenPreview”",
                    style = MaterialTheme.typography.bodyLarge,
                    textAlign = TextAlign.Center,
                )
            }
            if (status.isNotBlank()) {
                Spacer(Modifier.height(AppSpacing.medium))
                Text(status, style = MaterialTheme.typography.bodyMedium, textAlign = TextAlign.Center)
            }
            Spacer(Modifier.height(AppSpacing.large))
            SecondaryActionButton("CANCEL", onBack)
        }
    }
}

@Composable
fun MedicineScreen(
    medicines: List<Medicine>,
    takenIds: Set<String>,
    onTaken: (String) -> Unit,
    onBack: () -> Unit,
    onSpeak: (String) -> Unit,
) {
    LaunchedEffect(Unit) { onSpeak("Here are your medicine reminders.") }
    Scaffold(topBar = { SimpleTopBar("My Medicine", onBack) }) { padding ->
        Column(
            Modifier.fillMaxSize().padding(padding).verticalScroll(rememberScrollState()).padding(AppSpacing.large),
            verticalArrangement = Arrangement.spacedBy(AppSpacing.medium),
        ) {
            medicines.forEach { medicine ->
                MedicineCard(
                    medicine = medicine,
                    isTaken = medicine.id in takenIds,
                    onTaken = {
                        onTaken(medicine.id)
                        onSpeak("Medicine marked as taken.")
                    },
                )
            }
            Text(
                "Reminder prototype only. EasyCare does not provide medical advice.",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth().padding(AppSpacing.small),
            )
        }
    }
}

@Composable
fun SettingsScreen(
    initial: UserPreferences,
    onBack: () -> Unit,
    onSave: (UserPreferences) -> Unit,
) {
    var userName by remember(initial) { mutableStateOf(initial.userName) }
    var familyName by remember(initial) { mutableStateOf(initial.familyName) }
    var familyPhone by remember(initial) { mutableStateOf(initial.familyPhone) }
    var emergencyPhone by remember(initial) { mutableStateOf(initial.emergencyPhone) }
    var voiceEnabled by remember(initial) { mutableStateOf(initial.voiceEnabled) }
    var textSize by remember(initial) { mutableStateOf(initial.textSize) }

    Scaffold(topBar = { SimpleTopBar("Caregiver Settings", onBack) }) { padding ->
        Column(
            Modifier.fillMaxSize().padding(padding).verticalScroll(rememberScrollState()).padding(AppSpacing.large),
            verticalArrangement = Arrangement.spacedBy(AppSpacing.medium),
        ) {
            SettingsField("My Name", userName, { userName = it })
            Text("Family Contact", style = MaterialTheme.typography.titleMedium)
            SettingsField("Family member name", familyName, { familyName = it })
            SettingsField("Family phone", familyPhone, { familyPhone = it }, KeyboardType.Phone)
            Text("Emergency Contact", style = MaterialTheme.typography.titleMedium)
            SettingsField("Emergency phone", emergencyPhone, { emergencyPhone = it }, KeyboardType.Phone)
            Card(colors = CardDefaults.cardColors(MaterialTheme.colorScheme.surfaceVariant)) {
                Row(
                    Modifier.fillMaxWidth().heightIn(min = 76.dp).padding(horizontal = AppSpacing.medium),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Text("Voice Assistant", style = MaterialTheme.typography.titleMedium, modifier = Modifier.weight(1f))
                    Text(if (voiceEnabled) "ON" else "OFF", style = MaterialTheme.typography.bodyLarge, fontWeight = FontWeight.Bold)
                    Spacer(Modifier.padding(4.dp))
                    Switch(checked = voiceEnabled, onCheckedChange = { voiceEnabled = it })
                }
            }
            Text("Text Size", style = MaterialTheme.typography.titleMedium)
            Column(verticalArrangement = Arrangement.spacedBy(AppSpacing.small)) {
                TextSize.entries.forEach { option ->
                    FilterChip(
                        selected = textSize == option,
                        onClick = { textSize = option },
                        label = { Text(option.label, style = MaterialTheme.typography.bodyLarge) },
                        modifier = Modifier.fillMaxWidth().heightIn(min = 64.dp),
                    )
                }
            }
            LargeActionButton(
                "SAVE SETTINGS",
                onClick = {
                    onSave(
                        initial.copy(
                            userName = userName.trim(),
                            familyName = familyName.trim(),
                            familyPhone = familyPhone.trim(),
                            emergencyPhone = emergencyPhone.trim(),
                            voiceEnabled = voiceEnabled,
                            textSize = textSize,
                        ),
                    )
                },
                enabled = userName.isNotBlank() && familyName.isNotBlank() && familyPhone.isNotBlank() && emergencyPhone.isNotBlank(),
            )
            SecondaryActionButton("CANCEL", onBack)
        }
    }
}

@Composable
private fun SettingsField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    keyboardType: KeyboardType = KeyboardType.Text,
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label) },
        textStyle = MaterialTheme.typography.bodyLarge,
        singleLine = true,
        keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
        modifier = Modifier.fillMaxWidth().heightIn(min = 76.dp),
    )
}
