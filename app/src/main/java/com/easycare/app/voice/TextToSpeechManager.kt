package com.easycare.app.voice

import android.content.Context
import android.speech.tts.TextToSpeech
import java.util.Locale

class TextToSpeechManager(context: Context) : TextToSpeech.OnInitListener {
    private var ready = false
    private var pendingText: String? = null
    private val textToSpeech = TextToSpeech(context.applicationContext, this)

    override fun onInit(status: Int) {
        ready = status == TextToSpeech.SUCCESS
        if (ready) {
            textToSpeech.language = Locale.getDefault()
            pendingText?.let(::speak)
            pendingText = null
        }
    }

    fun speak(text: String, enabled: Boolean = true) {
        if (!enabled) return
        if (!ready) {
            pendingText = text
            return
        }
        textToSpeech.speak(text, TextToSpeech.QUEUE_FLUSH, null, "easycare-${System.nanoTime()}")
    }

    fun stop() = textToSpeech.stop()

    fun shutdown() {
        textToSpeech.stop()
        textToSpeech.shutdown()
    }
}
