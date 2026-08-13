package com.example.vocablearningapp.ui.screen.flashcard

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.vocablearningapp.domain.model.FsrsRating
import com.example.vocablearningapp.domain.model.VocabularySet
import com.example.vocablearningapp.ui.component.EmptyState
import com.example.vocablearningapp.ui.component.Flashcard
import com.example.vocablearningapp.ui.component.FsrsRatingButton
import com.example.vocablearningapp.ui.component.PrimaryButton
import com.example.vocablearningapp.ui.component.VocabDimens
import com.example.vocablearningapp.ui.component.VocabProgressBar
import com.example.vocablearningapp.ui.theme.Canvas
import com.example.vocablearningapp.ui.theme.Ink
import com.example.vocablearningapp.ui.theme.Muted

import com.example.vocablearningapp.ui.component.SecondaryButton
import com.example.vocablearningapp.ui.state.AppViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FlashcardScreen(
    set: VocabularySet?,
    onBack: () -> Unit,
    onRate: (String, FsrsRating) -> Unit,
    onFinished: () -> Unit,
    onSkipDailyWord: ((String) -> Unit)? = null
) {
    var currentIndex by remember(set?.id) { mutableStateOf(0) }
    var isFlipped by remember(set?.id) { mutableStateOf(false) }
    var isComplete by remember(set?.id) { mutableStateOf(false) }
    var hasChosenStudy by remember(currentIndex) { mutableStateOf(false) }
    val sessionWords = remember(set?.id) { set?.words.orEmpty() }
    val isDailyWords = set?.id == AppViewModel.DAILY_WORDS_SET_ID

    Scaffold(
        containerColor = Canvas,
        topBar = {
            TopAppBar(
                title = { Text(text = "Flashcards", color = Ink) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(imageVector = Icons.Default.Close, contentDescription = "Close", tint = Ink)
                    }
                },
                actions = {
                    if (sessionWords.isNotEmpty()) {
                        Text(
                            text = "${(currentIndex + 1).coerceAtMost(sessionWords.size)} / ${sessionWords.size}",
                            modifier = Modifier.padding(end = 20.dp),
                            style = MaterialTheme.typography.labelLarge,
                            color = Muted
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Canvas)
            )
        }
    ) { innerPadding ->
        if (sessionWords.isEmpty()) {
            EmptyState(
                title = "No cards yet",
                description = "Add a few words to start a study session.",
                modifier = Modifier.padding(innerPadding).padding(24.dp)
            )
        } else if (isComplete || currentIndex >= sessionWords.size) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .padding(horizontal = VocabDimens.ScreenPadding),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(text = "Session complete", style = MaterialTheme.typography.headlineSmall, color = Ink)
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "You reviewed ${sessionWords.size} cards.",
                    style = MaterialTheme.typography.bodyLarge,
                    color = Muted
                )
                Spacer(modifier = Modifier.height(22.dp))
                PrimaryButton(
                    text = "Back to set",
                    onClick = onFinished,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        } else {
            val item = sessionWords[currentIndex]
            val progress = (currentIndex + 1).toFloat() / sessionWords.size

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .padding(horizontal = VocabDimens.ScreenPadding, vertical = 10.dp),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(text = "Session progress", style = MaterialTheme.typography.labelMedium, color = Muted)
                        Text(text = "${(progress * 100).toInt()}%", style = MaterialTheme.typography.labelMedium, color = Ink)
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    VocabProgressBar(progress = progress)
                }

                Flashcard(
                    item = item,
                    isFlipped = isFlipped,
                    onFlip = { isFlipped = !isFlipped }
                )

                Column {
                    if (isDailyWords && !hasChosenStudy) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            SecondaryButton(
                                text = "Skip",
                                onClick = {
                                    onSkipDailyWord?.invoke(item.id)
                                    if (currentIndex >= sessionWords.lastIndex) {
                                        isComplete = true
                                    } else {
                                        currentIndex += 1
                                        isFlipped = false
                                    }
                                },
                                modifier = Modifier.weight(1f)
                            )
                            PrimaryButton(
                                text = "Study",
                                onClick = {
                                    hasChosenStudy = true
                                },
                                modifier = Modifier.weight(1f)
                            )
                        }
                    } else {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            FsrsRating.entries.forEach { rating ->
                                FsrsRatingButton(
                                    rating = rating,
                                    enabled = isFlipped,
                                    onClick = {
                                        onRate(item.id, rating)
                                        if (currentIndex == sessionWords.lastIndex) {
                                            isComplete = true
                                        } else {
                                            currentIndex += 1
                                            isFlipped = false
                                        }
                                    },
                                    modifier = Modifier.weight(1f)
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
