package com.example.vocablearningapp.ui.screen.practice

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.vocablearningapp.domain.model.FsrsRating
import com.example.vocablearningapp.domain.model.VocabularyItem
import com.example.vocablearningapp.domain.model.VocabularySet
import com.example.vocablearningapp.domain.srs.FsrsRatingEvaluator
import com.example.vocablearningapp.domain.srs.QuizModeType
import com.example.vocablearningapp.domain.util.ExampleSentenceGenerator
import com.example.vocablearningapp.ui.component.EmptyState
import com.example.vocablearningapp.ui.component.PrimaryButton
import com.example.vocablearningapp.ui.component.SecondaryButton
import com.example.vocablearningapp.ui.component.SessionResultSummary
import com.example.vocablearningapp.ui.component.SpeechButton
import com.example.vocablearningapp.ui.component.VocabDimens
import com.example.vocablearningapp.ui.component.VocabProgressBar
import com.example.vocablearningapp.ui.component.VocabTopBar
import com.example.vocablearningapp.ui.component.WordSessionResult
import com.example.vocablearningapp.ui.state.AppViewModel
import com.example.vocablearningapp.ui.theme.Accent
import com.example.vocablearningapp.ui.theme.AccentDark
import com.example.vocablearningapp.ui.theme.AccentSoft
import com.example.vocablearningapp.ui.theme.Border
import com.example.vocablearningapp.ui.theme.Canvas
import com.example.vocablearningapp.ui.theme.Forgot
import com.example.vocablearningapp.ui.theme.ForgotSoft
import com.example.vocablearningapp.ui.theme.Ink
import com.example.vocablearningapp.ui.theme.Mastered
import com.example.vocablearningapp.ui.theme.MasteredSoft
import com.example.vocablearningapp.ui.theme.Muted
import com.example.vocablearningapp.ui.theme.Surface
import com.example.vocablearningapp.ui.theme.SurfaceMuted

private data class ScrambleToken(
    val id: Int,
    val text: String,
    val isTargetWord: Boolean
)

private data class ScrambleQuestion(
    val item: VocabularyItem,
    val fullSentence: String,
    val originalTokens: List<ScrambleToken>,
    val shuffledTokens: List<ScrambleToken>
)

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun SentenceScrambleScreen(
    set: VocabularySet?,
    onBack: () -> Unit,
    onRate: (String, FsrsRating) -> Unit,
    onSkipDailyWord: ((String) -> Unit)? = null,
    onLearnDailyWord: ((String) -> Unit)? = null
) {
    val sessionWords = remember(set?.id) { set?.words.orEmpty().shuffled().take(10) }
    val questions = remember(set?.id) {
        sessionWords.map { item ->
            val sentence = ExampleSentenceGenerator.getNaturalExample(
                word = item.word,
                partOfSpeech = item.partOfSpeech,
                existingSentence = item.exampleSentence
            )
            // Split sentence into natural tokens (words)
            val rawTokens = sentence.split(Regex("\\s+")).filter { it.isNotBlank() }
            val tokens = rawTokens.mapIndexed { index, tok ->
                val cleanTok = tok.replace(Regex("[^a-zA-Z0-9]"), "").lowercase()
                val cleanWord = item.word.trim().lowercase()
                val isTarget = cleanTok == cleanWord || cleanTok.contains(cleanWord)
                ScrambleToken(id = index, text = tok, isTargetWord = isTarget)
            }
            ScrambleQuestion(
                item = item,
                fullSentence = sentence,
                originalTokens = tokens,
                shuffledTokens = tokens.shuffled()
            )
        }
    }

    var currentIndex by remember(set?.id) { mutableStateOf(0) }
    val placedTokens = remember(set?.id, currentIndex) { mutableStateListOf<ScrambleToken>() }
    var isSubmitted by remember(set?.id, currentIndex) { mutableStateOf(false) }
    var isCorrect by remember(set?.id, currentIndex) { mutableStateOf(false) }
    var undoCount by remember(set?.id, currentIndex) { mutableStateOf(0) }
    var hintUsed by remember(set?.id, currentIndex) { mutableStateOf(false) }
    var attemptCount by remember(set?.id, currentIndex) { mutableStateOf(0) }
    var startTimeMs by remember(set?.id, currentIndex) { mutableStateOf(System.currentTimeMillis()) }
    var score by remember(set?.id) { mutableStateOf(0) }
    var isComplete by remember(set?.id) { mutableStateOf(false) }
    var wordResults by remember(set?.id) { mutableStateOf(mapOf<String, Boolean>()) }

    val currentQuestion = questions.getOrNull(currentIndex)

    Scaffold(
        containerColor = Canvas,
        topBar = { VocabTopBar(title = "Sentence Scramble", onBack = onBack) }
    ) { innerPadding ->
        when {
            set == null || questions.isEmpty() -> {
                EmptyState(
                    title = "No sentences to unscramble",
                    description = "Add words with example sentences to this set to play.",
                    modifier = Modifier.padding(innerPadding).padding(24.dp)
                )
            }

            isComplete -> {
                val resultsList = sessionWords.map { word ->
                    WordSessionResult(
                        word = word,
                        isCorrect = wordResults[word.id] ?: false
                    )
                }
                SessionResultSummary(
                    title = "Sentence Scramble Complete!",
                    results = resultsList,
                    isDailyWords = set?.id == AppViewModel.DAILY_WORDS_SET_ID,
                    onBack = onBack,
                    onSkipDailyWord = onSkipDailyWord,
                    onLearnDailyWord = onLearnDailyWord,
                    modifier = Modifier.padding(innerPadding)
                )
            }

            currentQuestion != null -> {
                val availableTokens = currentQuestion.shuffledTokens.filterNot { placed ->
                    placedTokens.any { it.id == placed.id }
                }

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding)
                        .padding(horizontal = VocabDimens.ScreenPadding, vertical = 12.dp)
                        .verticalScroll(rememberScrollState()),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    // Header Status
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Question ${currentIndex + 1}/${questions.size}",
                            style = MaterialTheme.typography.labelLarge,
                            color = Muted,
                            fontWeight = FontWeight.SemiBold
                        )
                        Text(
                            text = "Score: $score",
                            style = MaterialTheme.typography.labelLarge,
                            color = Accent,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    VocabProgressBar(
                        progress = ((currentIndex + 1).toFloat() / questions.size.toFloat()).coerceIn(0f, 1f)
                    )

                    // Target Word Banner
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(18.dp),
                        colors = CardDefaults.cardColors(containerColor = Surface),
                        border = BorderStroke(1.dp, Border)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Text(
                                        text = currentQuestion.item.word,
                                        style = MaterialTheme.typography.titleMedium,
                                        fontWeight = FontWeight.Bold,
                                        color = AccentDark
                                    )
                                    Spacer(modifier = Modifier.size(6.dp))
                                    Surface(
                                        color = AccentSoft,
                                        shape = RoundedCornerShape(6.dp)
                                    ) {
                                        Text(
                                            text = currentQuestion.item.partOfSpeech.label,
                                            style = MaterialTheme.typography.labelSmall,
                                            color = AccentDark,
                                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                        )
                                    }
                                }
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = currentQuestion.item.meaning,
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = Ink
                                )
                            }

                            if (isSubmitted) {
                                SpeechButton(
                                    text = currentQuestion.fullSentence,
                                    contentDescription = "Listen to solved sentence",
                                    tint = Accent
                                )
                            }
                        }
                    }

                    // Drop Zone (Sentence Assembly Area)
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(20.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = if (isSubmitted) {
                                if (isCorrect) MasteredSoft else ForgotSoft
                            } else SurfaceMuted
                        ),
                        border = BorderStroke(
                            width = 1.5.dp,
                            color = if (isSubmitted) {
                                if (isCorrect) Mastered else Forgot
                            } else Border
                        )
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(18.dp),
                            verticalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "Arrange words into correct grammar:",
                                    style = MaterialTheme.typography.labelMedium,
                                    color = Muted,
                                    fontWeight = FontWeight.Medium
                                )
                                if (placedTokens.isNotEmpty() && !isSubmitted) {
                                    TextButton(onClick = {
                                        placedTokens.clear()
                                        undoCount++
                                    }) {
                                        Icon(Icons.Default.Refresh, contentDescription = "Reset", modifier = Modifier.size(16.dp))
                                        Spacer(modifier = Modifier.size(4.dp))
                                        Text("Reset", style = MaterialTheme.typography.labelSmall)
                                    }
                                }
                            }

                            if (placedTokens.isEmpty()) {
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(56.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = "Tap words below to assemble the sentence...",
                                        style = MaterialTheme.typography.bodyMedium,
                                        color = Muted,
                                        textAlign = TextAlign.Center
                                    )
                                }
                            } else {
                                FlowRow(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                                    verticalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    placedTokens.forEach { token ->
                                        Surface(
                                            shape = RoundedCornerShape(10.dp),
                                            color = if (token.isTargetWord) AccentSoft else Surface,
                                            border = BorderStroke(
                                                1.dp,
                                                if (token.isTargetWord) Accent else Border
                                            ),
                                            modifier = Modifier.clickable(enabled = !isSubmitted) {
                                                placedTokens.remove(token)
                                                undoCount++
                                            }
                                        ) {
                                            Text(
                                                text = token.text,
                                                style = MaterialTheme.typography.bodyLarge,
                                                fontWeight = if (token.isTargetWord) FontWeight.Bold else FontWeight.Medium,
                                                color = if (token.isTargetWord) AccentDark else Ink,
                                                modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp)
                                            )
                                        }
                                    }
                                }
                            }

                            // Result banner
                            if (isSubmitted) {
                                Row(
                                    modifier = Modifier.padding(top = 8.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                                ) {
                                    Icon(
                                        imageVector = if (isCorrect) Icons.Default.Check else Icons.Default.Close,
                                        contentDescription = null,
                                        tint = if (isCorrect) Mastered else Forgot,
                                        modifier = Modifier.size(20.dp)
                                    )
                                    Text(
                                        text = if (isCorrect) "Correct! Perfect word order." else "Incorrect word order!",
                                        style = MaterialTheme.typography.labelLarge,
                                        fontWeight = FontWeight.Bold,
                                        color = if (isCorrect) Mastered else Forgot
                                    )
                                }
                                if (!isCorrect) {
                                    Text(
                                        text = "Correct sentence: \"${currentQuestion.fullSentence}\"",
                                        style = MaterialTheme.typography.bodySmall,
                                        color = Ink
                                    )
                                }
                            }
                        }
                    }

                    // Word Bank (Available tokens to pick)
                    if (!isSubmitted && availableTokens.isNotEmpty()) {
                        Text(
                            text = "Available words:",
                            style = MaterialTheme.typography.labelMedium,
                            color = Muted,
                            fontWeight = FontWeight.SemiBold
                        )

                        FlowRow(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            availableTokens.forEach { token ->
                                Surface(
                                    shape = RoundedCornerShape(12.dp),
                                    color = if (token.isTargetWord) AccentSoft else Surface,
                                    border = BorderStroke(
                                        1.dp,
                                        if (token.isTargetWord) Accent else Border
                                    ),
                                    shadowElevation = 1.dp,
                                    modifier = Modifier.clickable {
                                        placedTokens.add(token)
                                    }
                                ) {
                                    Text(
                                        text = token.text,
                                        style = MaterialTheme.typography.bodyLarge,
                                        fontWeight = if (token.isTargetWord) FontWeight.Bold else FontWeight.Medium,
                                        color = if (token.isTargetWord) AccentDark else Ink,
                                        modifier = Modifier.padding(horizontal = 14.dp, vertical = 10.dp)
                                    )
                                }
                            }
                        }
                    }

                    // Action Controls
                    if (!isSubmitted) {
                        val isAllPlaced = placedTokens.size == currentQuestion.originalTokens.size
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            SecondaryButton(
                                text = "💡 Next Word Hint",
                                onClick = {
                                    val nextIndex = placedTokens.size
                                    if (nextIndex < currentQuestion.originalTokens.size) {
                                        val nextExpectedToken = currentQuestion.originalTokens[nextIndex]
                                        // Place the expected token
                                        if (!placedTokens.any { it.id == nextExpectedToken.id }) {
                                            placedTokens.add(nextExpectedToken)
                                            hintUsed = true
                                        }
                                    }
                                },
                                enabled = placedTokens.size < currentQuestion.originalTokens.size,
                                modifier = Modifier.weight(1f)
                            )

                            PrimaryButton(
                                text = "Check",
                                onClick = {
                                    if (isAllPlaced) {
                                        val userSentence = placedTokens.joinToString(" ") { it.text }
                                        val expectedSentence = currentQuestion.originalTokens.joinToString(" ") { it.text }
                                        val correct = userSentence.equals(expectedSentence, ignoreCase = true)
                                        isCorrect = correct
                                        isSubmitted = true
                                        attemptCount++
                                        val elapsedMs = System.currentTimeMillis() - startTimeMs
                                        val rating = FsrsRatingEvaluator.evaluate(
                                            isCorrect = correct,
                                            responseTimeMs = elapsedMs,
                                            modeType = QuizModeType.SENTENCE_SCRAMBLE,
                                            attemptCount = attemptCount,
                                            hintUsed = hintUsed || undoCount > 2
                                        )
                                        onRate(currentQuestion.item.id, rating)
                                        wordResults = wordResults + (currentQuestion.item.id to correct)
                                        if (correct) score++
                                    }
                                },
                                enabled = isAllPlaced,
                                modifier = Modifier.weight(1f)
                            )
                        }

                        TextButton(
                            onClick = {
                                isCorrect = false
                                isSubmitted = true
                                attemptCount = 2
                                val elapsedMs = System.currentTimeMillis() - startTimeMs
                                val rating = FsrsRatingEvaluator.evaluate(
                                    isCorrect = false,
                                    responseTimeMs = elapsedMs,
                                    modeType = QuizModeType.SENTENCE_SCRAMBLE,
                                    attemptCount = 2,
                                    hintUsed = true
                                )
                                onRate(currentQuestion.item.id, rating)
                                wordResults = wordResults + (currentQuestion.item.id to false)
                            },
                            modifier = Modifier.align(Alignment.CenterHorizontally)
                        ) {
                            Text("Show solution", color = Muted, style = MaterialTheme.typography.bodyMedium)
                        }
                    } else {
                        PrimaryButton(
                            text = if (currentIndex < questions.size - 1) "Next" else "See results",
                            onClick = {
                                if (currentIndex < questions.size - 1) {
                                    currentIndex++
                                } else {
                                    isComplete = true
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
