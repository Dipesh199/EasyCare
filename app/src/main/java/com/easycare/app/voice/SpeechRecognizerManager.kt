package com.easycare.app.voice

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.speech.RecognitionListener
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer

class SpeechRecognizerManager(
    context: Context,
    private val onListeningChanged: (Boolean) -> Unit,
    private val onResult: (String) -> Unit,
    private val onError: (String) -> Unit,
) : RecognitionListener {
    private var recognitionInProgress = false

    private val recognizer = SpeechRecognizer.createSpeechRecognizer(context.applicationContext).also {
        it.setRecognitionListener(this)
    }

    fun start(): Boolean {
        if (recognitionInProgress) return false
        if (!SpeechRecognizer.isRecognitionAvailable(recognizerContext)) {
            onError("Speech recognition is not available on this device. You can still use the buttons.")
            return false
        }
        val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {
            putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
            putExtra(RecognizerIntent.EXTRA_LANGUAGE, java.util.Locale.getDefault())
            putExtra(RecognizerIntent.EXTRA_MAX_RESULTS, 3)
            putExtra(RecognizerIntent.EXTRA_PARTIAL_RESULTS, false)
            putExtra(RecognizerIntent.EXTRA_PROMPT, "How can I help?")
        }
        recognitionInProgress = true
        onListeningChanged(true)
        return runCatching {
            recognizer.startListening(intent)
            true
        }.getOrElse {
            recognitionInProgress = false
            onListeningChanged(false)
            onError("Voice recognition could not start. Please try again or use the buttons.")
            false
        }
    }

    private val recognizerContext: Context = context.applicationContext

    fun destroy() {
        recognitionInProgress = false
        recognizer.cancel()
        recognizer.destroy()
    }

    override fun onReadyForSpeech(params: Bundle?) = onListeningChanged(true)
    override fun onBeginningOfSpeech() = Unit
    override fun onRmsChanged(rmsdB: Float) = Unit
    override fun onBufferReceived(buffer: ByteArray?) = Unit
    override fun onEndOfSpeech() = Unit
    override fun onEvent(eventType: Int, params: Bundle?) = Unit
    override fun onPartialResults(partialResults: Bundle?) = Unit

    override fun onResults(results: Bundle?) {
        recognitionInProgress = false
        onListeningChanged(false)
        val sentence = results?.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)?.firstOrNull()
        if (sentence.isNullOrBlank()) onError("I did not hear a command. Please try again.") else onResult(sentence)
    }

    override fun onError(error: Int) {
        recognitionInProgress = false
        onListeningChanged(false)
        val message = when (error) {
            SpeechRecognizer.ERROR_NO_MATCH, SpeechRecognizer.ERROR_SPEECH_TIMEOUT ->
                "I did not hear a command. Please try again."
            SpeechRecognizer.ERROR_INSUFFICIENT_PERMISSIONS ->
                "Microphone permission is needed for voice commands. You can still use the buttons."
            SpeechRecognizer.ERROR_NETWORK, SpeechRecognizer.ERROR_NETWORK_TIMEOUT ->
                "Voice recognition is unavailable right now. Please use the buttons."
            SpeechRecognizer.ERROR_RECOGNIZER_BUSY ->
                "I am already listening. Please wait a moment."
            SpeechRecognizer.ERROR_CLIENT ->
                "Voice recognition was interrupted. Please tap the microphone and try again."
            SpeechRecognizer.ERROR_AUDIO ->
                "The microphone could not be opened. Please try again."
            SpeechRecognizer.ERROR_SERVER, SpeechRecognizer.ERROR_SERVER_DISCONNECTED ->
                "The voice service is temporarily unavailable. Please try again."
            else -> "Voice recognition could not start. Please try again or use the buttons."
        }
        onError(message)
    }
}
