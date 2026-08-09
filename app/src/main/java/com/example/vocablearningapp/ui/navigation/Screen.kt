package com.example.vocablearningapp.ui.navigation

sealed class Screen(val route: String) {
    object Home : Screen("home")
    object Learn : Screen("learn")
    object Review : Screen("review")
    object Progress : Screen("progress")
    object AllSets : Screen("all-sets")
    object SetDetail : Screen("set-detail/{setId}") {
        fun createRoute(setId: String) = "set-detail/$setId"
    }
    object Flashcards : Screen("flashcards/{setId}") {
        fun createRoute(setId: String) = "flashcards/$setId"
    }
    object Practice : Screen("practice/{mode}/{setId}") {
        fun createRoute(mode: String, setId: String) = "practice/$mode/$setId"
    }
    object SetEditor : Screen("set-editor/{setId}") {
        fun createRoute(setId: String) = "set-editor/$setId"
    }
}
