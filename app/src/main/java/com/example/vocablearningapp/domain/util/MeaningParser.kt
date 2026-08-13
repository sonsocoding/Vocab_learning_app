package com.example.vocablearningapp.domain.util

import com.example.vocablearningapp.domain.model.PartOfSpeech

data class MeaningEntry(
    val partOfSpeech: PartOfSpeech,
    val meaning: String,
    val exampleSentence: String = ""
)

object MeaningParser {
    fun parse(
        rawMeaning: String,
        rawExample: String = "",
        fallbackPos: PartOfSpeech = PartOfSpeech.NOUN
    ): List<MeaningEntry> {
        val trimmedMeaning = rawMeaning.trim()
        if (trimmedMeaning.isBlank()) return emptyList()

        val exampleLines = rawExample.trim().split("\n", "|", ";").map { it.trim() }.filter { it.isNotBlank() }
        val entries = mutableListOf<MeaningEntry>()
        val lines = trimmedMeaning.split("\n", ";", "|").map { it.trim() }.filter { it.isNotBlank() }

        lines.forEachIndexed { index, line ->
            val lower = line.lowercase()
            val example = exampleLines.getOrNull(index)
                ?.replace(Regex("^(n\\.|v\\.|adj\\.|noun\\.|verb\\.|adjective\\.)"), "")
                ?.trim()
                .orEmpty()

            when {
                lower.startsWith("n.") || lower.startsWith("n:") || lower.startsWith("(n)") || lower.startsWith("noun") -> {
                    val text = line.substringAfter(".").substringAfter(":").substringAfter(")").trim()
                    if (text.isNotBlank()) entries.add(MeaningEntry(PartOfSpeech.NOUN, text, example))
                }
                lower.startsWith("v.") || lower.startsWith("v:") || lower.startsWith("(v)") || lower.startsWith("verb") -> {
                    val text = line.substringAfter(".").substringAfter(":").substringAfter(")").trim()
                    if (text.isNotBlank()) entries.add(MeaningEntry(PartOfSpeech.VERB, text, example))
                }
                lower.startsWith("adj.") || lower.startsWith("adj:") || lower.startsWith("(adj)") || lower.startsWith("adjective") -> {
                    val text = line.substringAfter(".").substringAfter(":").substringAfter(")").trim()
                    if (text.isNotBlank()) entries.add(MeaningEntry(PartOfSpeech.ADJECTIVE, text, example))
                }
                else -> {
                    entries.add(MeaningEntry(fallbackPos, line, example))
                }
            }
        }

        return entries.ifEmpty { listOf(MeaningEntry(fallbackPos, trimmedMeaning, rawExample.trim())) }
    }
}
