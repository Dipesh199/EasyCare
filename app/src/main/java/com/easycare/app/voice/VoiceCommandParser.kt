package com.easycare.app.voice

sealed interface VoiceCommand {
    data object Food : VoiceCommand
    data object CallFamily : VoiceCommand
    data object MessageFamily : VoiceCommand
    data object Emergency : VoiceCommand
    data object Medicine : VoiceCommand
    data object Unknown : VoiceCommand
}

object VoiceCommandParser {
    private data class Rule(
        val command: VoiceCommand,
        val phrases: List<String>,
    )

    private val rules = listOf(
        Rule(VoiceCommand.Emergency, listOf("help me", "emergency", "need help")),
        Rule(VoiceCommand.Food, listOf("want food", "order food", "hungry", "meal", "food")),
        Rule(VoiceCommand.CallFamily, listOf("call my son", "call family", "call dipesh", "phone dipesh", "phone family")),
        Rule(VoiceCommand.MessageFamily, listOf("send message", "message my son", "message dipesh", "tell my family")),
        Rule(VoiceCommand.Medicine, listOf("my medicine", "medication", "medicine")),
    )

    fun parse(sentence: String): VoiceCommand {
        val normalized = sentence.lowercase().trim()
        return rules.firstOrNull { rule -> rule.phrases.any(normalized::contains) }?.command
            ?: VoiceCommand.Unknown
    }
}
