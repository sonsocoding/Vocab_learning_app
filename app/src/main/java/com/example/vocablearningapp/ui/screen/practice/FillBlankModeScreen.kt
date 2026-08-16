package com.example.vocablearningapp.ui.screen.practice

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
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

private data class ClozeQuestion(
    val item: VocabularyItem,
    val sentence: String,
    val targetWord: String,
    val prefix: String,
    val suffix: String
)

@Composable
fun FillBlankModeScreen(
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
            val regex = Regex("\\b${Regex.escape(item.word.trim())}\\b", RegexOption.IGNORE_CASE)
            val match = regex.find(sentence)
            if (match != null) {
                val prefix = sentence.substring(0, match.range.first)
                val suffix = sentence.substring(match.range.last + 1)
                ClozeQuestion(item, sentence, match.value, prefix, suffix)
            } else {
                // Fallback if word not exact boundary match
                ClozeQuestion(
                    item = item,
                    sentence = sentence,
                    targetWord = item.word.trim(),
                    prefix = "Sentence: ",
                    suffix = " (${item.meaning})"
                )
            }
        }
    }

    var currentIndex by remember(set?.id) { mutableStateOf(0) }
    var userInput by remember(set?.id, currentIndex) { mutableStateOf("") }
    var isSubmitted by remember(set?.id, currentIndex) { mutableStateOf(false) }
    var isCorrect by remember(set?.id, currentIndex) { mutableStateOf(false) }
    var hintRevealedCount by remember(set?.id, currentIndex) { mutableStateOf(0) }
    var attemptCount by remember(set?.id, currentIndex) { mutableStateOf(0) }
    var startTimeMs by remember(set?.id, currentIndex) { mutableStateOf(System.currentTimeMillis()) }
    var score by remember(set?.id) { mutableStateOf(0) }
    var isComplete by remember(set?.id) { mutableStateOf(false) }
    var wordResults by remember(set?.id) { mutableStateOf(mapOf<String, Boolean>()) }

    val currentQuestion = questions.getOrNull(currentIndex)

    Scaffold(
        containerColor = Canvas,
        topBar = { VocabTopBar(title = "Fill in the Blank", onBack = onBack) }
    ) { innerPadding ->
        when {
            set == null || questions.isEmpty() -> {
                EmptyState(
                    title = "No words to practice",
                    description = "Add words to this set to practice filling in the blanks.",
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
                    title = "Fill in the Blank Complete!",
                    results = resultsList,
                    isDailyWords = set?.id == AppViewModel.DAILY_WORDS_SET_ID,
                    onBack = onBack,
                    onSkipDailyWord = onSkipDailyWord,
                    onLearnDailyWord = onLearnDailyWord,
                    modifier = Modifier.padding(innerPadding)
                )
            }

            currentQuestion != null -> {
                val target = currentQuestion.targetWord

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

                    // Main Cloze Card
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(22.dp),
                        colors = CardDefaults.cardColors(containerColor = Surface),
                        border = BorderStroke(
                            width = 1.dp,
                            color = if (isSubmitted) {
                                if (isCorrect) Mastered else Forgot
                            } else Border
                        )
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(20.dp),
                            verticalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                            // Hint Bar (Part of Speech & Meaning)
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Surface(
                                    color = AccentSoft,
                                    shape = RoundedCornerShape(8.dp)
                                ) {
                                    Text(
                                        text = currentQuestion.item.partOfSpeech.label.uppercase(),
                                        style = MaterialTheme.typography.labelSmall,
                                        color = AccentDark,
                                        fontWeight = FontWeight.Bold,
                                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                    )
                                }

                                SpeechButton(
                                    text = currentQuestion.sentence,
                                    contentDescription = "Listen to full sentence",
                                    tint = Accent
                                )
                            }

                            // Vietnamese Meaning prompt
                            Text(
                                text = "Nghĩa: ${currentQuestion.item.meaning}",
                                style = MaterialTheme.typography.titleMedium,
                                color = Ink,
                                fontWeight = FontWeight.SemiBold
                            )

                            // Sentence with Blank Gap
                            Surface(
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(14.dp),
                                color = SurfaceMuted
                            ) {
                                val annotatedSentence = buildAnnotatedString {
                                    append(currentQuestion.prefix)
                                    withStyle(
                                        SpanStyle(
                                            color = if (isSubmitted) {
                                                if (isCorrect) Mastered else Forgot
                                            } else Accent,
                                            fontWeight = FontWeight.Bold,
                                            background = if (isSubmitted) {
                                                if (isCorrect) MasteredSoft else ForgotSoft
                                            } else AccentSoft
                                        )
                                    ) {
                                        if (isSubmitted) {
                                            append(" $target ")
                                        } else {
                                            val hintText = target.take(hintRevealedCount)
                                            val remainingDots = "_".repeat((target.length - hintRevealedCount).coerceAtLeast(3))
                                            append(" $hintText$remainingDots ")
                                        }
                                    }
                                    append(currentQuestion.suffix)
                                }

                                Text(
                                    text = annotatedSentence,
                                    style = MaterialTheme.typography.bodyLarge.copy(
                                        lineHeight = 26.sp
                                    ),
                                    modifier = Modifier.padding(16.dp)
                                )
                            }

                            // Typing Input Field
                            OutlinedTextField(
                                value = userInput,
                                onValueChange = { if (!isSubmitted) userInput = it },
                                label = { Text("Type missing word") },
                                placeholder = { Text("e.g. ${target.take(1)}...") },
                                singleLine = true,
                                enabled = !isSubmitted,
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(14.dp),
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedBorderColor = Accent,
                                    unfocusedBorderColor = Border
                                ),
                                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                                keyboardActions = KeyboardActions(
                                    onDone = {
                                        if (userInput.isNotBlank() && !isSubmitted) {
                                            val correct = userInput.trim().equals(target, ignoreCase = true)
                                            isCorrect = correct
                                            isSubmitted = true
                                            attemptCount++
                                            val elapsedMs = System.currentTimeMillis() - startTimeMs
                                            val rating = FsrsRatingEvaluator.evaluate(
                                                isCorrect = correct,
                                                responseTimeMs = elapsedMs,
                                                modeType = QuizModeType.FILL_IN_BLANK,
                                                attemptCount = attemptCount,
                                                hintUsed = hintRevealedCount > 0
                                            )
                                            onRate(currentQuestion.item.id, rating)
                                            wordResults = wordResults + (currentQuestion.item.id to correct)
                                            if (correct) score++
                                        }
                                    }
                                )
                            )

                            // Result feedback message
                            if (isSubmitted) {
                                Surface(
                                    modifier = Modifier.fillMaxWidth(),
                                    shape = RoundedCornerShape(12.dp),
                                    color = if (isCorrect) MasteredSoft else ForgotSoft
                                ) {
                                    Row(
                                        modifier = Modifier.padding(12.dp),
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                                    ) {
                                        Icon(
                                            imageVector = if (isCorrect) Icons.Default.Check else Icons.Default.Close,
                                            contentDescription = null,
                                            tint = if (isCorrect) Mastered else Forgot
                                        )
                                        Column {
                                            Text(
                                                text = if (isCorrect) "Chính xác! Hoàn hảo." else "Chưa chính xác!",
                                                style = MaterialTheme.typography.labelLarge,
                                                fontWeight = FontWeight.Bold,
                                                color = if (isCorrect) Mastered else Forgot
                                            )
                                            if (!isCorrect) {
                                                Text(
                                                    text = "Đáp án đúng: $target (${currentQuestion.item.pronunciation})",
                                                    style = MaterialTheme.typography.bodySmall,
                                                    color = Ink
                                                )
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }

                    // Action Buttons (Hint, Submit, Continue)
                    if (!isSubmitted) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            SecondaryButton(
                                text = if (hintRevealedCount == 0) "💡 Gợi ý (Hint)" else "Thêm chữ cái",
                                onClick = {
                                    if (hintRevealedCount < target.length) {
                                        hintRevealedCount++
                                    }
                                },
                                modifier = Modifier.weight(1f)
                            )

                            PrimaryButton(
                                text = "Kiểm tra",
                                onClick = {
                                    if (userInput.isNotBlank()) {
                                        val correct = userInput.trim().equals(target, ignoreCase = true)
                                        isCorrect = correct
                                        isSubmitted = true
                                        attemptCount++
                                        val elapsedMs = System.currentTimeMillis() - startTimeMs
                                        val rating = FsrsRatingEvaluator.evaluate(
                                            isCorrect = correct,
                                            responseTimeMs = elapsedMs,
                                            modeType = QuizModeType.FILL_IN_BLANK,
                                            attemptCount = attemptCount,
                                            hintUsed = hintRevealedCount > 0
                                        )
                                        onRate(currentQuestion.item.id, rating)
                                        wordResults = wordResults + (currentQuestion.item.id to correct)
                                        if (correct) score++
                                    }
                                },
                                enabled = userInput.isNotBlank(),
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
                                    modeType = QuizModeType.FILL_IN_BLANK,
                                    attemptCount = 2,
                                    hintUsed = true
                                )
                                onRate(currentQuestion.item.id, rating)
                                wordResults = wordResults + (currentQuestion.item.id to false)
                            },
                            modifier = Modifier.align(Alignment.CenterHorizontally)
                        ) {
                            Text("Xem đáp án", color = Muted, style = MaterialTheme.typography.bodyMedium)
                        }
                    } else {
                        PrimaryButton(
                            text = if (currentIndex < questions.size - 1) "Tiếp theo" else "Xem kết quả",
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
