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
        fallbackPos: PartOfSpeech = PartOfSpeech.NOUN,
        word: String = ""
    ): List<MeaningEntry> {
        val trimmedMeaning = rawMeaning.trim()
        if (trimmedMeaning.isBlank()) return emptyList()

        val exampleLines = rawExample.trim().split("\n", "|", ";").map { it.trim() }.filter { it.isNotBlank() }
        val entries = mutableListOf<MeaningEntry>()
        val lines = trimmedMeaning.split("\n", ";", "|").map { it.trim() }.filter { it.isNotBlank() }

        lines.forEachIndexed { index, line ->
            val lower = line.lowercase()
            val rawEx = exampleLines.getOrNull(index)
                ?.replace(Regex("^(n\\.|v\\.|adj\\.|noun\\.|verb\\.|adjective\\.)"), "")
                ?.trim()
                .orEmpty()

            val pos = when {
                lower.startsWith("n.") || lower.startsWith("n:") || lower.startsWith("(n)") || lower.startsWith("noun") -> PartOfSpeech.NOUN
                lower.startsWith("v.") || lower.startsWith("v:") || lower.startsWith("(v)") || lower.startsWith("verb") -> PartOfSpeech.VERB
                lower.startsWith("adj.") || lower.startsWith("adj:") || lower.startsWith("(adj)") || lower.startsWith("adjective") -> PartOfSpeech.ADJECTIVE
                else -> fallbackPos
            }

            val text = when {
                lower.startsWith("n.") || lower.startsWith("n:") || lower.startsWith("(n)") || lower.startsWith("noun") ||
                lower.startsWith("v.") || lower.startsWith("v:") || lower.startsWith("(v)") || lower.startsWith("verb") ||
                lower.startsWith("adj.") || lower.startsWith("adj:") || lower.startsWith("(adj)") || lower.startsWith("adjective") -> {
                    line.substringAfter(".").substringAfter(":").substringAfter(")").trim()
                }
                else -> line
            }

            val finalExample = if (word.isNotBlank()) {
                ExampleSentenceGenerator.getNaturalExample(word, pos, rawEx)
            } else {
                if (ExampleSentenceGenerator.isPlaceholder(rawEx)) "" else rawEx
            }

            if (text.isNotBlank()) {
                entries.add(MeaningEntry(pos, text, finalExample))
            }
        }

        val fallbackEx = if (word.isNotBlank()) {
            ExampleSentenceGenerator.getNaturalExample(word, fallbackPos, rawExample.trim())
        } else {
            if (ExampleSentenceGenerator.isPlaceholder(rawExample.trim())) "" else rawExample.trim()
        }

        return entries.ifEmpty { listOf(MeaningEntry(fallbackPos, trimmedMeaning, fallbackEx)) }
    }
}
