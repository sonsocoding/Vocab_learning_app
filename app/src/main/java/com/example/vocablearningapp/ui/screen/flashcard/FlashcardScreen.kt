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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.vocablearningapp.domain.model.MemoryLevel
import com.example.vocablearningapp.domain.model.VocabularySet
import com.example.vocablearningapp.ui.component.EmptyState
import com.example.vocablearningapp.ui.component.Flashcard
import com.example.vocablearningapp.ui.component.MemoryLevelButton
import com.example.vocablearningapp.ui.component.VocabDimens
import com.example.vocablearningapp.ui.component.VocabProgressBar
import com.example.vocablearningapp.ui.theme.Canvas
import com.example.vocablearningapp.ui.theme.Ink
import com.example.vocablearningapp.ui.theme.Muted

@OptIn(ExperimentalMaterial3Api::class)

@Composable
fun FlashcardScreen(
    set: VocabularySet?,
    onBack: () -> Unit,
    onRate: (String, MemoryLevel) -> Unit,
    onFinished: () -> Unit
) {
    var currentIndex by remember(set?.id) { mutableStateOf(0) }
    var isFlipped by remember(set?.id) { mutableStateOf(false) }
    val sessionWords = remember(set?.id) { set?.words.orEmpty() }

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
        } else if (currentIndex >= sessionWords.size) {
            onFinished()
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
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        MemoryLevel.entries.forEach { level ->
                            MemoryLevelButton(
                                level = level,
                                enabled = isFlipped,
                                onClick = {
                                    onRate(item.id, level)
                                    if (currentIndex == sessionWords.lastIndex) {
                                        onFinished()
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
