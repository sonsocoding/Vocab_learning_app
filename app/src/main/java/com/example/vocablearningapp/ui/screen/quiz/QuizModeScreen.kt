package com.example.vocablearningapp.ui.screen.quiz

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
import com.example.vocablearningapp.domain.model.FsrsRating
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

private data class TrueFalseQuestion(
    val wordItem: VocabularyItem,
    val meaningItem: VocabularyItem
) {
    val answer: Boolean
        get() = wordItem.id == meaningItem.id
}

@Composable
fun QuizModeScreen(
    set: VocabularySet?,
    onBack: () -> Unit,
    onRate: (String, FsrsRating) -> Unit
) {
    var currentIndex by remember(set?.id) { mutableStateOf(0) }
    var selectedAnswer by remember(set?.id) { mutableStateOf<Boolean?>(null) }
    var score by remember(set?.id) { mutableStateOf(0) }
    var isComplete by remember(set?.id) { mutableStateOf(false) }
    val questions = remember(set?.id) {
        val words = set?.words.orEmpty().shuffled().take(10)
        words.map { wordItem ->
            val showCorrectMeaning = words.size < 2 || kotlin.random.Random.nextBoolean()
            val meaningItem = if (showCorrectMeaning) {
                wordItem
            } else {
                words.filterNot { it.id == wordItem.id }.random()
            }
            TrueFalseQuestion(wordItem = wordItem, meaningItem = meaningItem)
        }
    }
    val currentQuestion = questions.getOrNull(currentIndex)

    Scaffold(
        containerColor = Canvas,
        topBar = { VocabTopBar(title = "Quiz", onBack = onBack) }
    ) { innerPadding ->
        when {
            set == null || questions.isEmpty() -> EmptyState(
                title = "No words to quiz",
                description = "Add a few words to this set before starting a quiz.",
                modifier = Modifier.padding(innerPadding).padding(24.dp)
            )

            isComplete -> QuizComplete(
                total = questions.size,
                score = score,
                onBack = onBack,
                modifier = Modifier.padding(innerPadding)
            )

            currentQuestion != null -> {
                val hasAnswer = selectedAnswer != null
                val isCorrect = selectedAnswer == currentQuestion.answer
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
                            text = "Question ${currentIndex + 1} of ${questions.size}",
                            style = MaterialTheme.typography.labelLarge,
                            color = Muted
                        )
                        Text(text = "Score $score", style = MaterialTheme.typography.labelLarge, color = Accent)
                    }
                    VocabProgressBar(progress = (currentIndex + 1f) / questions.size)

                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(22.dp),
                        colors = CardDefaults.cardColors(containerColor = Surface),
                        border = BorderStroke(1.dp, Border)
                    ) {
                        Column(modifier = Modifier.padding(20.dp)) {
                            Text(text = "Does this meaning match the word?", style = MaterialTheme.typography.labelMedium, color = Accent)
                            Spacer(modifier = Modifier.height(10.dp))
                            Text(text = currentQuestion.wordItem.word, style = MaterialTheme.typography.headlineSmall, color = Ink)
                            Spacer(modifier = Modifier.height(5.dp))
                            Text(text = currentQuestion.wordItem.pronunciation, style = MaterialTheme.typography.bodyMedium, color = Muted)
                            Spacer(modifier = Modifier.height(16.dp))
                            Text(text = currentQuestion.meaningItem.meaning, style = MaterialTheme.typography.titleLarge, color = Ink)
                        }
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        TrueFalseOption(
                            label = "True",
                            value = true,
                            selectedAnswer = selectedAnswer,
                            correctAnswer = currentQuestion.answer,
                            enabled = !hasAnswer,
                            modifier = Modifier.weight(1f),
                            onClick = {
                                selectedAnswer = true
                                if (currentQuestion.answer) {
                                    score += 1
                                    onRate(currentQuestion.wordItem.id, FsrsRating.GOOD)
                                } else {
                                    onRate(currentQuestion.wordItem.id, FsrsRating.AGAIN)
                                }
                            }
                        )
                        TrueFalseOption(
                            label = "False",
                            value = false,
                            selectedAnswer = selectedAnswer,
                            correctAnswer = currentQuestion.answer,
                            enabled = !hasAnswer,
                            modifier = Modifier.weight(1f),
                            onClick = {
                                selectedAnswer = false
                                if (!currentQuestion.answer) {
                                    score += 1
                                    onRate(currentQuestion.wordItem.id, FsrsRating.GOOD)
                                } else {
                                    onRate(currentQuestion.wordItem.id, FsrsRating.AGAIN)
                                }
                            }
                        )
                    }

                    if (hasAnswer) {
                        Text(
                            text = if (isCorrect) "Correct" else "Not quite. The correct answer is ${if (currentQuestion.answer) "True" else "False"}.",
                            style = MaterialTheme.typography.bodyLarge,
                            color = if (isCorrect) Mastered else Forgot
                        )
                        PrimaryButton(
                            text = if (currentIndex == questions.lastIndex) "See results" else "Next question",
                            onClick = {
                                if (currentIndex == questions.lastIndex) {
                                    isComplete = true
                                } else {
                                    currentIndex += 1
                                    selectedAnswer = null
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
private fun TrueFalseOption(
    label: String,
    value: Boolean,
    selectedAnswer: Boolean?,
    correctAnswer: Boolean,
    enabled: Boolean,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    val containerColor = when {
        selectedAnswer == null -> Surface
        value == correctAnswer -> MasteredSoft
        value == selectedAnswer -> ForgotSoft
        else -> Surface
    }
    val contentColor = when {
        selectedAnswer == null -> Ink
        value == correctAnswer -> Mastered
        value == selectedAnswer -> Forgot
        else -> Muted
    }
    Card(
        modifier = modifier.clickable(enabled = enabled, onClick = onClick),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = containerColor),
        border = BorderStroke(1.dp, if (selectedAnswer == null) Border else containerColor)
    ) {
        Text(
            text = label,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 18.dp),
            style = MaterialTheme.typography.titleMedium,
            color = contentColor,
            textAlign = androidx.compose.ui.text.style.TextAlign.Center
        )
    }
}

@Composable
private fun QuizComplete(
    total: Int,
    score: Int,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    val percentage = (score * 100) / total.coerceAtLeast(1)
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = VocabDimens.ScreenPadding),
        verticalArrangement = Arrangement.Center
    ) {
        Text(text = "Quiz complete", style = MaterialTheme.typography.headlineSmall, color = Ink)
        Spacer(modifier = Modifier.height(8.dp))
        Text(text = "$score / $total correct · $percentage% accuracy", style = MaterialTheme.typography.bodyLarge, color = Muted)
        Spacer(modifier = Modifier.height(22.dp))
        PrimaryButton(text = "Back to set", onClick = onBack, modifier = Modifier.fillMaxWidth())
    }
}
