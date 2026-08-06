package com.example.vocablearningapp.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.vocablearningapp.AppContainer
import com.example.vocablearningapp.ui.screen.deck.DeckListScreen
import com.example.vocablearningapp.ui.screen.deck.DeckViewModel
import com.example.vocablearningapp.ui.screen.flashcard.FlashcardScreen
import com.example.vocablearningapp.ui.screen.flashcard.FlashcardViewModel
import com.example.vocablearningapp.ui.screen.home.HomeScreen
import com.example.vocablearningapp.ui.screen.home.HomeViewModel
import com.example.vocablearningapp.ui.screen.level.LevelListScreen
import com.example.vocablearningapp.ui.screen.level.LevelViewModel
import com.example.vocablearningapp.ui.screen.login.LoginScreen
import com.example.vocablearningapp.ui.screen.login.LoginViewModel
import com.example.vocablearningapp.ui.screen.result.StudyResultScreen
import com.example.vocablearningapp.ui.screen.result.StudyResultViewModel
import com.example.vocablearningapp.ui.screen.topic.TopicListScreen
import com.example.vocablearningapp.ui.screen.topic.TopicViewModel

@Composable
fun NavGraph(
    navController: NavHostController,
    container: AppContainer
) {
    val currentUserId by container.userRepository.currentUserId.collectAsState(initial = null)
    val startDestination = if (currentUserId != null) Screen.Home.route else Screen.Login.route

    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        // Login Screen
        composable(Screen.Login.route) {
            val viewModel: LoginViewModel = viewModel(
                factory = LoginViewModel.Factory(container.userRepository)
            )
            LoginScreen(
                viewModel = viewModel,
                onLoginSuccess = {
                    navController.navigate(Screen.Home.route) {
                        popUpTo(Screen.Login.route) { inclusive = true }
                    }
                }
            )
        }

        // Home Screen
        composable(Screen.Home.route) {
            val viewModel: HomeViewModel = viewModel(
                factory = HomeViewModel.Factory(container.userRepository, container.vocabRepository)
            )
            HomeScreen(
                viewModel = viewModel,
                onNavigateToLevels = { navController.navigate(Screen.LevelList.route) },
                onNavigateToTopic = { levelId -> navController.navigate(Screen.TopicList.createRoute(levelId)) },
                onNavigateToFlashcard = { deckId -> navController.navigate(Screen.Flashcard.createRoute(deckId)) },
                onLogout = {
                    navController.navigate(Screen.Login.route) {
                        popUpTo(Screen.Home.route) { inclusive = true }
                    }
                }
            )
        }

        // Level List Screen
        composable(Screen.LevelList.route) {
            val viewModel: LevelViewModel = viewModel(
                factory = LevelViewModel.Factory(container.vocabRepository)
            )
            LevelListScreen(
                viewModel = viewModel,
                onNavigateBack = { navController.popBackStack() },
                onLevelSelected = { levelId ->
                    navController.navigate(Screen.TopicList.createRoute(levelId))
                }
            )
        }

        // Topic List Screen
        composable(
            route = Screen.TopicList.route,
            arguments = listOf(navArgument("levelId") { type = NavType.StringType })
        ) { backStackEntry ->
            val levelId = backStackEntry.arguments?.getString("levelId") ?: ""
            val viewModel: TopicViewModel = viewModel(
                factory = TopicViewModel.Factory(container.vocabRepository)
            )
            TopicListScreen(
                levelId = levelId,
                viewModel = viewModel,
                onNavigateBack = { navController.popBackStack() },
                onTopicSelected = { topicId ->
                    navController.navigate(Screen.DeckList.createRoute(topicId))
                }
            )
        }

        // Deck List Screen
        composable(
            route = Screen.DeckList.route,
            arguments = listOf(navArgument("topicId") { type = NavType.LongType })
        ) { backStackEntry ->
            val topicId = backStackEntry.arguments?.getLong("topicId") ?: 0L
            val viewModel: DeckViewModel = viewModel(
                factory = DeckViewModel.Factory(container.userRepository, container.vocabRepository)
            )
            DeckListScreen(
                topicId = topicId,
                viewModel = viewModel,
                onNavigateBack = { navController.popBackStack() },
                onDeckSelected = { deckId ->
                    navController.navigate(Screen.Flashcard.createRoute(deckId))
                }
            )
        }

        // Flashcard Screen
        composable(
            route = Screen.Flashcard.route,
            arguments = listOf(navArgument("deckId") { type = NavType.LongType })
        ) { backStackEntry ->
            val deckId = backStackEntry.arguments?.getLong("deckId") ?: 0L
            val viewModel: FlashcardViewModel = viewModel(
                factory = FlashcardViewModel.Factory(container.userRepository, container.vocabRepository)
            )
            FlashcardScreen(
                deckId = deckId,
                viewModel = viewModel,
                onNavigateBack = { navController.popBackStack() },
                onFinishSession = { id, totalWords, hard, somewhat, remembered, veryWell ->
                    navController.navigate(
                        Screen.StudyResult.createRoute(
                            deckId = id,
                            totalWords = totalWords,
                            hardCount = hard,
                            somewhatCount = somewhat,
                            rememberedCount = remembered,
                            veryWellCount = veryWell
                        )
                    ) {
                        popUpTo(Screen.Flashcard.route) { inclusive = true }
                    }
                }
            )
        }

        // Study Result Screen
        composable(
            route = Screen.StudyResult.route,
            arguments = listOf(
                navArgument("deckId") { type = NavType.LongType },
                navArgument("totalWords") { type = NavType.IntType },
                navArgument("hardCount") { type = NavType.IntType },
                navArgument("somewhatCount") { type = NavType.IntType },
                navArgument("rememberedCount") { type = NavType.IntType },
                navArgument("veryWellCount") { type = NavType.IntType }
            )
        ) { backStackEntry ->
            val deckId = backStackEntry.arguments?.getLong("deckId") ?: 0L
            val totalWords = backStackEntry.arguments?.getInt("totalWords") ?: 0
            val hardCount = backStackEntry.arguments?.getInt("hardCount") ?: 0
            val somewhatCount = backStackEntry.arguments?.getInt("somewhatCount") ?: 0
            val rememberedCount = backStackEntry.arguments?.getInt("rememberedCount") ?: 0
            val veryWellCount = backStackEntry.arguments?.getInt("veryWellCount") ?: 0

            val viewModel: StudyResultViewModel = viewModel(
                factory = StudyResultViewModel.Factory(container.vocabRepository)
            )

            StudyResultScreen(
                deckId = deckId,
                totalWords = totalWords,
                hardCount = hardCount,
                somewhatCount = somewhatCount,
                rememberedCount = rememberedCount,
                veryWellCount = veryWellCount,
                viewModel = viewModel,
                onRelearnDeck = { id ->
                    navController.navigate(Screen.Flashcard.createRoute(id)) {
                        popUpTo(Screen.StudyResult.route) { inclusive = true }
                    }
                },
                onBackToHome = {
                    navController.navigate(Screen.Home.route) {
                        popUpTo(Screen.Home.route) { inclusive = true }
                    }
                }
            )
        }
    }
}
