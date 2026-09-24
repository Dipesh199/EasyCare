package com.easycare.app.ui.screens

import android.Manifest
import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Message
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import com.easycare.app.model.ContactTarget
import com.easycare.app.ui.components.ContactCard
import com.easycare.app.ui.components.ContactTargetSelector
import com.easycare.app.ui.components.LargeActionButton
import com.easycare.app.ui.components.SecondaryActionButton
import com.easycare.app.ui.components.SimpleTopBar
import com.easycare.app.ui.theme.AppSpacing
import com.easycare.app.voice.SpeechRecognizerManager

@Composable
fun FamilyScreen(
    name: String,
    phone: String,
    onBack: () -> Unit,
    onCall: () -> Unit,
    onMessage: () -> Unit,
    onSpeak: (String) -> Unit,
) {
    LaunchedEffect(Unit) { onSpeak("What would you like to do?") }
    Scaffold(topBar = { SimpleTopBar("Family", onBack) }) { padding ->
        Column(
            Modifier.fillMaxSize().padding(padding).padding(AppSpacing.large),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
        ) {
            ContactCard(name, phone)
            Spacer(Modifier.height(AppSpacing.extraLarge))
            LargeActionButton("CALL ${name.uppercase()}", onCall, icon = Icons.Default.Call)
            Spacer(Modifier.height(AppSpacing.medium))
            LargeActionButton("MESSAGE ${name.uppercase()}", onMessage, icon = Icons.AutoMirrored.Filled.Message)
        }
    }
}

@Composable
fun MessageScreen(
    familyName: String,
    onBack: () -> Unit,
    onSend: (ContactTarget, String) -> Boolean,
    onSpeak: (String) -> Unit,
) {
    val context = LocalContext.current
    var message by remember { mutableStateOf("") }
    var listening by remember { mutableStateOf(false) }
    var status by remember { mutableStateOf("") }
    var selectedContact by remember { mutableStateOf(ContactTarget.Family) }
    val recognizer = remember {
        SpeechRecognizerManager(
            context,
            onListeningChanged = { listening = it },
            onResult = { message = it; status = "Message added." },
            onError = { status = it },
        )
    }
    DisposableEffect(recognizer) { onDispose { recognizer.destroy() } }
    val permissionLauncher = rememberLauncherForActivityResult(ActivityResultContracts.RequestPermission()) { granted ->
        if (granted) recognizer.start()
        else status = "Microphone permission is needed for dictation. You can still type your message."
    }
    fun dictate() {
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_GRANTED) recognizer.start()
        else permissionLauncher.launch(Manifest.permission.RECORD_AUDIO)
    }

    LaunchedEffect(Unit) { onSpeak("What message would you like to send?") }
    Scaffold(topBar = { SimpleTopBar("Send Message", onBack) }) { padding ->
        Column(
            Modifier.fillMaxSize().padding(padding).verticalScroll(rememberScrollState()).padding(AppSpacing.large),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text("Your message", style = MaterialTheme.typography.headlineMedium)
            Spacer(Modifier.height(AppSpacing.medium))
            ContactTargetSelector(
                selected = selectedContact,
                familyName = familyName,
                onSelected = { selectedContact = it },
            )
            Spacer(Modifier.height(AppSpacing.medium))
            OutlinedTextField(
                value = message,
                onValueChange = { message = it },
                modifier = Modifier.fillMaxWidth().height(190.dp),
                textStyle = MaterialTheme.typography.bodyLarge,
                placeholder = { Text("Type or speak your message", style = MaterialTheme.typography.bodyLarge) },
            )
            Spacer(Modifier.height(AppSpacing.medium))
            LargeActionButton(
                text = if (listening) "LISTENING…" else "SPEAK MESSAGE",
                onClick = ::dictate,
                icon = Icons.Default.Mic,
                enabled = !listening,
            )
            if (status.isNotBlank()) {
                Spacer(Modifier.height(AppSpacing.small))
                Text(status, style = MaterialTheme.typography.bodyMedium, textAlign = TextAlign.Center)
            }
            Spacer(Modifier.height(AppSpacing.large))
            LargeActionButton(
                "SEND MESSAGE",
                onClick = {
                    if (!onSend(selectedContact, message.trim())) status = "No SMS app is available."
                },
                enabled = message.isNotBlank(),
                icon = Icons.AutoMirrored.Filled.Message,
            )
            Spacer(Modifier.height(AppSpacing.medium))
            SecondaryActionButton("CANCEL", onBack)
        }
    }
}
