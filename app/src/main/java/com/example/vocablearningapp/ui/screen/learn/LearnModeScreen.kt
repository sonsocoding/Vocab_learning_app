package com.example.vocablearningapp.ui.screen.learn

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.vocablearningapp.domain.model.MemoryLevel
import com.example.vocablearningapp.domain.model.VocabularyItem
import com.example.vocablearningapp.domain.model.VocabularySet
import com.example.vocablearningapp.ui.component.EmptyState
import com.example.vocablearningapp.ui.component.PrimaryButton
import com.example.vocablearningapp.ui.component.VocabDimens
import com.example.vocablearningapp.ui.component.VocabProgressBar
import com.example.vocablearningapp.ui.component.VocabTopBar
import com.example.vocablearningapp.ui.theme.Accent
import com.example.vocablearningapp.ui.theme.Border
import com.example.vocablearningapp.ui.theme.Canvas
import com.example.vocablearningapp.ui.theme.Forgot
import com.example.vocablearningapp.ui.theme.ForgotSoft
import com.example.vocablearningapp.ui.theme.Ink
import com.example.vocablearningapp.ui.theme.Mastered
import com.example.vocablearningapp.ui.theme.MasteredSoft
import com.example.vocablearningapp.ui.theme.Muted
import com.example.vocablearningapp.ui.theme.Surface

@Composable
fun LearnModeScreen(
    set: VocabularySet?,
    onBack: () -> Unit,
    onRate: (String, MemoryLevel) -> Unit
) {
    var currentIndex by remember(set?.id) { mutableStateOf(0) }
    var selectedAnswerId by remember(set?.id) { mutableStateOf<String?>(null) }
    var correctAnswers by remember(set?.id) { mutableStateOf(0) }
    var isComplete by remember(set?.id) { mutableStateOf(false) }
    val sessionWords = remember(set?.id) { set?.words.orEmpty().shuffled().take(10) }
    val currentItem = sessionWords.getOrNull(currentIndex)
    val options = remember(currentItem?.id) {
        currentItem?.let { item ->
            (sessionWords.filterNot { it.id == item.id }.shuffled().take(3) + item).shuffled()
        }.orEmpty()
    }

    Scaffold(
        containerColor = Canvas,
        topBar = { VocabTopBar(title = "Learn", onBack = onBack) }
    ) { innerPadding ->
        when {
            set == null || sessionWords.isEmpty() -> EmptyState(
                title = "No words to learn",
                description = "Add a few words to this set before starting a learning session.",
                modifier = Modifier.padding(innerPadding).padding(24.dp)
            )

            isComplete -> LearnComplete(
                total = sessionWords.size,
                correct = correctAnswers,
                onBack = onBack,
                modifier = Modifier.padding(innerPadding)
            )

            currentItem != null -> {
                val isAnswerSelected = selectedAnswerId != null
                val isCorrect = selectedAnswerId == currentItem.id
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding)
                        .padding(horizontal = VocabDimens.ScreenPadding, vertical = 14.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "Question ${currentIndex + 1} of ${sessionWords.size}",
                            style = MaterialTheme.typography.labelLarge,
                            color = Muted
                        )
                        Text(
                            text = "$correctAnswers correct",
                            style = MaterialTheme.typography.labelLarge,
                            color = Accent
                        )
                    }
                    VocabProgressBar(progress = (currentIndex + 1f) / sessionWords.size)

                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(22.dp),
                        colors = CardDefaults.cardColors(containerColor = Surface),
                        border = BorderStroke(1.dp, Border)
                    ) {
                        Column(modifier = Modifier.padding(20.dp)) {
                            Text(text = "Choose the Vietnamese meaning", style = MaterialTheme.typography.labelMedium, color = Accent)
                            Spacer(modifier = Modifier.height(10.dp))
                            Text(text = currentItem.word, style = MaterialTheme.typography.headlineSmall, color = Ink)
                            Spacer(modifier = Modifier.height(5.dp))
                            Text(text = currentItem.pronunciation, style = MaterialTheme.typography.bodyMedium, color = Muted)
                        }
                    }

                    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                        options.forEach { option ->
                            LearnAnswerOption(
                                item = option,
                                selectedAnswerId = selectedAnswerId,
                                correctAnswerId = currentItem.id,
                                enabled = !isAnswerSelected,
                                onClick = {
                                    selectedAnswerId = option.id
                                    if (option.id == currentItem.id) {
                                        correctAnswers += 1
                                        onRate(currentItem.id, MemoryLevel.LEARNING)
                                    } else {
                                        onRate(currentItem.id, MemoryLevel.FORGOT)
                                    }
                                }
                            )
                        }
                    }

                    if (isAnswerSelected) {
                        Text(
                            text = if (isCorrect) "Correct" else "The correct answer is ${currentItem.meaning}",
                            style = MaterialTheme.typography.bodyLarge,
                            color = if (isCorrect) Mastered else Forgot
                        )
                        PrimaryButton(
                            text = if (currentIndex == sessionWords.lastIndex) "Finish" else "Continue",
                            onClick = {
                                if (currentIndex == sessionWords.lastIndex) {
                                    isComplete = true
                                } else {
                                    currentIndex += 1
                                    selectedAnswerId = null
                                }
                            },
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun LearnAnswerOption(
    item: VocabularyItem,
    selectedAnswerId: String?,
    correctAnswerId: String,
    enabled: Boolean,
    onClick: () -> Unit
) {
    val containerColor = when {
        selectedAnswerId == null -> Surface
        item.id == correctAnswerId -> MasteredSoft
        item.id == selectedAnswerId -> ForgotSoft
        else -> Surface
    }
    val contentColor = when {
        selectedAnswerId == null -> Ink
        item.id == correctAnswerId -> Mastered
        item.id == selectedAnswerId -> Forgot
        else -> Muted
    }
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(enabled = enabled, onClick = onClick),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = containerColor),
        border = BorderStroke(1.dp, if (selectedAnswerId == null) Border else containerColor)
    ) {
        Text(
            text = item.meaning,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 17.dp),
            style = MaterialTheme.typography.bodyLarge,
            color = contentColor
        )
    }
}

@Composable
private fun LearnComplete(
    total: Int,
    correct: Int,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = VocabDimens.ScreenPadding),
        verticalArrangement = Arrangement.Center
    ) {
        Text(text = "Learning session complete", style = MaterialTheme.typography.headlineSmall, color = Ink)
        Spacer(modifier = Modifier.height(8.dp))
        Text(text = "$correct of $total answers were correct.", style = MaterialTheme.typography.bodyLarge, color = Muted)
        Spacer(modifier = Modifier.height(22.dp))
        PrimaryButton(text = "Back to set", onClick = onBack, modifier = Modifier.fillMaxWidth())
    }
}
