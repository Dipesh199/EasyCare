package com.easycare.app.ui.screens

import android.Manifest
import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
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
import com.easycare.app.ui.components.SecondaryActionButton
import com.easycare.app.ui.components.SimpleTopBar
import com.easycare.app.ui.components.VoiceButton
import com.easycare.app.ui.theme.AppSpacing
import com.easycare.app.voice.SpeechRecognizerManager
import com.easycare.app.voice.VoiceCommand
import com.easycare.app.voice.VoiceCommandParser
import kotlinx.coroutines.delay

@Composable
fun VoiceAssistantScreen(
    familyName: String,
    onBack: () -> Unit,
    onSpeak: (String) -> Unit,
    onCommand: (VoiceCommand) -> Unit,
) {
    val context = LocalContext.current
    var listening by remember { mutableStateOf(false) }
    var detectedSentence by remember { mutableStateOf("") }
    var status by remember { mutableStateOf("Tap the microphone and say what you need.") }
    var pendingCommand by remember { mutableStateOf<VoiceCommand?>(null) }

    val recognizer = remember {
        SpeechRecognizerManager(
            context = context,
            onListeningChanged = { listening = it },
            onResult = { sentence ->
                detectedSentence = sentence
                val command = VoiceCommandParser.parse(sentence)
                pendingCommand = command
                status = when (command) {
                    VoiceCommand.Food -> "Opening Food"
                    VoiceCommand.CallFamily -> "Opening phone dialer"
                    VoiceCommand.MessageFamily -> "Opening Message"
                    VoiceCommand.Emergency -> "Opening Emergency Help"
                    VoiceCommand.Medicine -> "Opening Medicine"
                    VoiceCommand.Unknown -> "I did not understand. Please try again or use the buttons."
                }
                val reply = when (command) {
                    VoiceCommand.Food -> "What would you like to eat?"
                    VoiceCommand.CallFamily -> "Opening the phone dialer for $familyName."
                    VoiceCommand.MessageFamily -> "What message would you like to send?"
                    VoiceCommand.Emergency -> "Do you need emergency help?"
                    VoiceCommand.Medicine -> "Here are your medicine reminders."
                    VoiceCommand.Unknown -> status
                }
                onSpeak(reply)
            },
            onError = { status = it },
        )
    }
    DisposableEffect(recognizer) { onDispose { recognizer.destroy() } }

    val permissionLauncher = rememberLauncherForActivityResult(ActivityResultContracts.RequestPermission()) { granted ->
        if (granted) recognizer.start()
        else status = "Microphone permission is needed for voice commands. You can still use the buttons."
    }

    fun startListening() {
        detectedSentence = ""
        pendingCommand = null
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_GRANTED) {
            recognizer.start()
        } else {
            permissionLauncher.launch(Manifest.permission.RECORD_AUDIO)
        }
    }

    LaunchedEffect(Unit) { startListening() }
    LaunchedEffect(pendingCommand) {
        val command = pendingCommand ?: return@LaunchedEffect
        if (command != VoiceCommand.Unknown) {
            delay(1_000)
            onCommand(command)
        }
    }

    Scaffold(topBar = { SimpleTopBar("Voice Assistant", onBack) }) { padding ->
        Column(
            Modifier.fillMaxSize().padding(padding).padding(AppSpacing.large),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Spacer(Modifier.height(AppSpacing.large))
            Text(
                if (listening) "Listening…" else status,
                style = MaterialTheme.typography.headlineMedium,
                textAlign = TextAlign.Center,
            )
            Spacer(Modifier.height(AppSpacing.extraLarge))
            VoiceButton(
                onClick = ::startListening,
                label = if (listening) "Listening…" else "Tap and Speak",
                enabled = !listening,
            )
            if (detectedSentence.isNotBlank()) {
                Spacer(Modifier.height(AppSpacing.extraLarge))
                Text("I heard:", style = MaterialTheme.typography.titleMedium)
                Text("“$detectedSentence”", style = MaterialTheme.typography.bodyLarge, textAlign = TextAlign.Center)
            }
            Spacer(Modifier.weight(1f))
            SecondaryActionButton("Go Back", onBack, Modifier.fillMaxWidth())
        }
    }
}
