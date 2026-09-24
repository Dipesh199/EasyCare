package com.easycare.app.voice

import org.junit.Assert.assertEquals
import org.junit.Test

class VoiceCommandParserTest {
    @Test
    fun parsesSupportedElderlyAssistanceCommands() {
        val examples = mapOf(
            "I want food" to VoiceCommand.Food,
            "I am hungry" to VoiceCommand.Food,
            "Call Dipesh" to VoiceCommand.CallFamily,
            "Tell my family" to VoiceCommand.MessageFamily,
            "I need help" to VoiceCommand.Emergency,
            "My medication" to VoiceCommand.Medicine,
        )

        examples.forEach { (sentence, expected) ->
            assertEquals(expected, VoiceCommandParser.parse(sentence))
        }
    }

    @Test
    fun emergencyTakesPriorityOverGenericWords() {
        assertEquals(VoiceCommand.Emergency, VoiceCommandParser.parse("Help me call my family"))
    }

    @Test
    fun unsupportedCommandReturnsUnknown() {
        assertEquals(VoiceCommand.Unknown, VoiceCommandParser.parse("What is the weather?"))
    }
}
