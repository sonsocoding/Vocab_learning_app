package com.example.vocablearningapp.domain.model

/** The four states used by the FSRS card lifecycle. */
enum class FsrsState(val label: String) {
    NEW("New"),
    LEARNING("Learning"),
    REVIEW("Review"),
    RELEARNING("Relearning")
}

/** FSRS ratings, ordered exactly as Again=1, Hard=2, Good=3 and Easy=4. */
enum class FsrsRating(val value: Int, val label: String) {
    AGAIN(1, "Again"),
    HARD(2, "Hard"),
    GOOD(3, "Good"),
    EASY(4, "Easy")
}

enum class PartOfSpeech(val label: String) {
    NOUN("noun"),
    VERB("verb"),
    ADJECTIVE("adj")
}

data class VocabularyItem(
    val id: String,
    val word: String,
    val meaning: String,
    val pronunciation: String,
    val partOfSpeech: PartOfSpeech = PartOfSpeech.NOUN,
    val exampleSentence: String,
    val fsrsState: FsrsState = FsrsState.NEW,
    val fsrsStep: Int? = null,
    val stability: Double = 0.0,
    val difficulty: Double = 0.0,
    val dueAtMillis: Long = 0L,
    val lastReviewAtMillis: Long? = null,
    val scheduledDays: Int = 0,
    val reviewCount: Int = 0,
    val lapseCount: Int = 0
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
            (words.count { it.fsrsState == FsrsState.REVIEW } * 100) / words.size
        }
}

enum class StudyMode(val title: String, val subtitle: String) {
    FLASHCARDS("Flashcards", "Flip and self-rate"),
    LEARN("Learn", "Build knowledge step by step"),
    QUIZ("Quiz", "Test your knowledge"),
    MATCH("Match", "Pair words and meanings"),
    MULTIPLE_CHOICE("Multiple Choice", "Choose the right answer"),
    FILL_IN_BLANK("Fill in Blank", "Spell words in context"),
    SENTENCE_PRACTICE("Sentence Scramble", "Unscramble word order puzzle")
}
