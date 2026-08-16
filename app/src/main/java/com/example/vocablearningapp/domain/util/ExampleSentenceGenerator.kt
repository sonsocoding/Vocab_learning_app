package com.example.vocablearningapp.domain.util

import com.example.vocablearningapp.domain.model.PartOfSpeech
import kotlin.math.abs

object ExampleSentenceGenerator {

    private val verbTemplates = listOf(
        "You should %s regularly to achieve the best results.",
        "They decided to %s before making a final decision.",
        "It is important to %s carefully in this situation.",
        "We plan to %s as soon as the project begins.",
        "She tries to %s every day to improve her skills.",
        "They will %s together to solve the issue."
    )

    private val nounTemplates = listOf(
        "The %s was discussed in detail during the meeting.",
        "She has a clear understanding of the %s.",
        "This %s plays an essential role in our daily work.",
        "They found a valuable %s during their research.",
        "He shared important information about the %s.",
        "A good %s helps everyone stay organized and productive."
    )

    private val adjectiveTemplates = listOf(
        "The team found an extremely %s solution to the problem.",
        "She is known for having a very %s approach to her work.",
        "It was a %s experience that helped everyone learn.",
        "They made a %s contribution to the entire project.",
        "This is a %s method to achieve consistent progress.",
        "He remained %s throughout the whole discussion."
    )

    /**
     * Checks if the given sentence is a generic placeholder like "This is an example sentence for xyz."
     */
    fun isPlaceholder(sentence: String): Boolean {
        val clean = sentence.trim().lowercase()
        return clean.isBlank() ||
            clean.startsWith("this is an example") ||
            clean.startsWith("an example sentence for") ||
            clean.contains("example sentence for")
    }

    /**
     * Returns the original sentence if it's a real sentence, or generates a natural English sentence
     * based on the word and its part of speech.
     */
    fun getNaturalExample(
        word: String,
        partOfSpeech: PartOfSpeech = PartOfSpeech.NOUN,
        existingSentence: String = ""
    ): String {
        val cleanWord = word.trim()
        if (cleanWord.isBlank()) return ""

        if (existingSentence.isNotBlank() && !isPlaceholder(existingSentence)) {
            return existingSentence.trim()
        }

        val hash = abs(cleanWord.hashCode())
        return when (partOfSpeech) {
            PartOfSpeech.VERB -> {
                val template = verbTemplates[hash % verbTemplates.size]
                String.format(template, cleanWord)
            }
            PartOfSpeech.ADJECTIVE -> {
                val template = adjectiveTemplates[hash % adjectiveTemplates.size]
                String.format(template, cleanWord)
            }
            PartOfSpeech.NOUN -> {
                val template = nounTemplates[hash % nounTemplates.size]
                String.format(template, cleanWord)
            }
        }
    }
}
