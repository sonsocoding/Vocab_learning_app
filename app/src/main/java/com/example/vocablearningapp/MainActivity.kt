package com.example.vocablearningapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
import com.example.vocablearningapp.ui.navigation.NavGraph
import com.example.vocablearningapp.ui.state.AppViewModel
import com.example.vocablearningapp.ui.theme.VocabTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            VocabTheme {
                val navController = rememberNavController()
                val appViewModel: AppViewModel = viewModel()
                NavGraph(
                    navController = navController,
                    appViewModel = appViewModel
                )
            }
        }
    }
}
