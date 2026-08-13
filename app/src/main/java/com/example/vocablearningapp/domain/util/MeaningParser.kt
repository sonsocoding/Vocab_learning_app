package com.example.vocablearningapp.domain.util

import com.example.vocablearningapp.domain.model.PartOfSpeech

data class MeaningEntry(
    val partOfSpeech: PartOfSpeech,
    val meaning: String
)

object MeaningParser {
    fun parse(rawMeaning: String, fallbackPos: PartOfSpeech = PartOfSpeech.NOUN): List<MeaningEntry> {
        val trimmed = rawMeaning.trim()
        if (trimmed.isBlank()) return emptyList()

        val entries = mutableListOf<MeaningEntry>()
        val lines = trimmed.split("\n", ";", "|").map { it.trim() }.filter { it.isNotBlank() }

        for (line in lines) {
            val lower = line.lowercase()
            when {
                lower.startsWith("n.") || lower.startsWith("n:") || lower.startsWith("(n)") || lower.startsWith("noun") -> {
                    val text = line.substringAfter(".").substringAfter(":").substringAfter(")").trim()
                    if (text.isNotBlank()) entries.add(MeaningEntry(PartOfSpeech.NOUN, text))
                }
                lower.startsWith("v.") || lower.startsWith("v:") || lower.startsWith("(v)") || lower.startsWith("verb") -> {
                    val text = line.substringAfter(".").substringAfter(":").substringAfter(")").trim()
                    if (text.isNotBlank()) entries.add(MeaningEntry(PartOfSpeech.VERB, text))
                }
                lower.startsWith("adj.") || lower.startsWith("adj:") || lower.startsWith("(adj)") || lower.startsWith("adjective") -> {
                    val text = line.substringAfter(".").substringAfter(":").substringAfter(")").trim()
                    if (text.isNotBlank()) entries.add(MeaningEntry(PartOfSpeech.ADJECTIVE, text))
                }
                else -> {
                    entries.add(MeaningEntry(fallbackPos, line))
                }
            }
        }

        return entries.ifEmpty { listOf(MeaningEntry(fallbackPos, trimmed)) }
    }
}
