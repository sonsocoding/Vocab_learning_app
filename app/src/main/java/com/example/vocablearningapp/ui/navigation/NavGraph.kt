package com.example.vocablearningapp.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.vocablearningapp.domain.model.StudyMode
import com.example.vocablearningapp.ui.screen.flashcard.FlashcardScreen
import com.example.vocablearningapp.ui.screen.home.HomeScreen
import com.example.vocablearningapp.ui.screen.learn.LearnScreen
import com.example.vocablearningapp.ui.screen.learn.LearnModeScreen
import com.example.vocablearningapp.ui.screen.progress.ProgressScreen
import com.example.vocablearningapp.ui.screen.quiz.QuizModeScreen
import com.example.vocablearningapp.ui.screen.review.ReviewScreen
import com.example.vocablearningapp.ui.screen.set.AllSetsScreen
import com.example.vocablearningapp.ui.screen.set.PracticePlaceholderScreen
import com.example.vocablearningapp.ui.screen.set.SetDetailScreen
import com.example.vocablearningapp.ui.screen.set.SetEditorScreen
import com.example.vocablearningapp.ui.state.AppViewModel
import com.example.vocablearningapp.ui.component.MainTab

@Composable
fun NavGraph(
    navController: NavHostController,
    appViewModel: AppViewModel
) {
    fun navigateToTab(tab: MainTab) {
        navController.navigate(tab.route) {
            popUpTo(Screen.Home.route) { saveState = true }
            launchSingleTop = true
            restoreState = true
        }
    }

    NavHost(
        navController = navController,
        startDestination = Screen.Home.route
    ) {
        composable(Screen.Home.route) {
            HomeScreen(
                onTabSelected = ::navigateToTab,
                sets = appViewModel.sets,
                onOpenSet = { id -> navController.navigate(Screen.SetDetail.createRoute(id)) },
                onSeeAllSets = { navController.navigate(Screen.AllSets.route) },
                onCreateSet = { navController.navigate(Screen.SetEditor.createRoute("new")) },
                dailyWords = appViewModel.dailyWordsSet,
                onOpenDailyWords = {
                    navController.navigate(
                        Screen.Flashcards.createRoute(AppViewModel.DAILY_WORDS_SET_ID)
                    )
                }
            )
        }

        composable(Screen.Learn.route) {
            LearnScreen(
                onTabSelected = ::navigateToTab,
                sets = appViewModel.sets,
                onOpenSet = { id -> navController.navigate(Screen.SetDetail.createRoute(id)) },
                onCreateSet = { navController.navigate(Screen.SetEditor.createRoute("new")) }
            )
        }

        composable(Screen.Review.route) {
            ReviewScreen(
                onTabSelected = ::navigateToTab,
                sets = appViewModel.sets,
                items = appViewModel.reviewItems,
                onStartReview = {
                    navController.navigate(
                        Screen.Flashcards.createRoute(AppViewModel.TODAY_REVIEW_SET_ID)
                    )
                },
                onOpenSet = { id -> navController.navigate(Screen.SetDetail.createRoute(id)) }
            )
        }

        composable(Screen.Progress.route) {
            ProgressScreen(
                onTabSelected = ::navigateToTab,
                sets = appViewModel.sets,
                items = appViewModel.allItems,
                onOpenSet = { id -> navController.navigate(Screen.SetDetail.createRoute(id)) }
            )
        }

        composable(Screen.AllSets.route) {
            AllSetsScreen(
                sets = appViewModel.sets,
                onBack = { navController.popBackStack() },
                onOpenSet = { id -> navController.navigate(Screen.SetDetail.createRoute(id)) },
                onCreateSet = { navController.navigate(Screen.SetEditor.createRoute("new")) }
            )
        }

        composable(
            route = Screen.SetDetail.route,
            arguments = listOf(navArgument("setId") { type = NavType.StringType })
        ) { backStackEntry ->
            val setId = backStackEntry.arguments?.getString("setId").orEmpty()
            SetDetailScreen(
                set = appViewModel.setById(setId),
                onBack = { navController.popBackStack() },
                onStartLearning = { navController.navigate(Screen.Flashcards.createRoute(setId)) },
                onOpenMode = { mode ->
                    if (mode == StudyMode.FLASHCARDS) {
                        navController.navigate(Screen.Flashcards.createRoute(setId))
                    } else {
                        navController.navigate(Screen.Practice.createRoute(mode.name, setId))
                    }
                },
                onEditSet = { navController.navigate(Screen.SetEditor.createRoute(setId)) }
            )
        }

        composable(
            route = Screen.Flashcards.route,
            arguments = listOf(navArgument("setId") { type = NavType.StringType })
        ) { backStackEntry ->
            val setId = backStackEntry.arguments?.getString("setId").orEmpty()
            FlashcardScreen(
                set = appViewModel.setById(setId),
                onBack = { navController.popBackStack() },
                onRate = appViewModel::rateItem,
                onFinished = {
                    navController.popBackStack()
                }
            )
        }

        composable(
            route = Screen.Practice.route,
            arguments = listOf(
                navArgument("mode") { type = NavType.StringType },
                navArgument("setId") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val modeName = backStackEntry.arguments?.getString("mode").orEmpty()
            val setId = backStackEntry.arguments?.getString("setId").orEmpty()
            val mode = StudyMode.entries.firstOrNull { it.name == modeName } ?: StudyMode.QUIZ
            when (mode) {
                StudyMode.LEARN -> LearnModeScreen(
                    set = appViewModel.setById(setId),
                    onBack = { navController.popBackStack() },
                    onRate = appViewModel::rateItem
                )

                StudyMode.QUIZ -> QuizModeScreen(
                    set = appViewModel.setById(setId),
                    onBack = { navController.popBackStack() },
                    onRate = appViewModel::rateItem
                )

                else -> PracticePlaceholderScreen(
                    mode = mode,
                    onBack = { navController.popBackStack() }
                )
            }
        }

        composable(
            route = Screen.SetEditor.route,
            arguments = listOf(navArgument("setId") { type = NavType.StringType })
        ) { backStackEntry ->
            val setId = backStackEntry.arguments?.getString("setId") ?: "new"
            SetEditorScreen(
                set = appViewModel.setById(setId),
                onBack = { navController.popBackStack() },
                onSave = { title, description, level, category, words ->
                    appViewModel.saveSet(setId, title, description, level, category, words)
                    navController.popBackStack()
                }
            )
        }
    }
}
