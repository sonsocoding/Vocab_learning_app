package com.example.vocablearningapp.domain.model

enum class MemoryLevel(val label: String) {
    NOT_REMEMBERED("Not remembered"),
    SOMEWHAT_REMEMBERED("Somewhat remembered"),
    REMEMBERED("Remembered")
}

data class VocabularyItem(
    val id: String,
    val word: String,
    val meaning: String,
    val pronunciation: String,
    val exampleSentence: String,
    val memoryLevel: MemoryLevel = MemoryLevel.NOT_REMEMBERED,
    val nextReviewDate: String = "Today"
)

data class VocabularySet(
    val id: String,
    val title: String,
    val description: String,
    val category: String,
    val level: String,
    val words: List<VocabularyItem>
) {
    val progress: Int
        get() = if (words.isEmpty()) 0 else {
            (words.count { it.memoryLevel == MemoryLevel.REMEMBERED } * 100) / words.size
        }
}

enum class StudyMode(val title: String, val subtitle: String) {
    FLASHCARDS("Flashcards", "Flip and self-rate"),
    LEARN("Learn", "Build knowledge step by step"),
    QUIZ("Quiz", "Test your knowledge"),
    MATCH("Match", "Pair words and meanings"),
    MULTIPLE_CHOICE("Multiple Choice", "Choose the right answer"),
    FILL_IN_BLANK("Fill in the Blank", "Complete each sentence"),
    SENTENCE_PRACTICE("Sentence Practice", "Use words in context")
}
