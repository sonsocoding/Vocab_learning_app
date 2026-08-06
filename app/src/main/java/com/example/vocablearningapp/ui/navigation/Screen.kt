package com.example.vocablearningapp.ui.navigation

sealed class Screen(val route: String) {
    object Login : Screen("login")
    object Home : Screen("home")
    object LevelList : Screen("level")
    object TopicList : Screen("topic/{levelId}") {
        fun createRoute(levelId: String) = "topic/$levelId"
    }
    object DeckList : Screen("deck/{topicId}") {
        fun createRoute(topicId: Long) = "deck/$topicId"
    }
    object Flashcard : Screen("flashcard/{deckId}") {
        fun createRoute(deckId: Long) = "flashcard/$deckId"
    }
    object StudyResult : Screen("study_result/{deckId}/{totalWords}/{hardCount}/{somewhatCount}/{rememberedCount}/{veryWellCount}") {
        fun createRoute(
            deckId: Long,
            totalWords: Int,
            hardCount: Int,
            somewhatCount: Int,
            rememberedCount: Int,
            veryWellCount: Int
        ) = "study_result/$deckId/$totalWords/$hardCount/$somewhatCount/$rememberedCount/$veryWellCount"
    }
}
