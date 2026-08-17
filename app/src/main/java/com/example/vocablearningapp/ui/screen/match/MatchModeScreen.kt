package com.example.vocablearningapp.ui.screen.match

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
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

import com.example.vocablearningapp.ui.component.SessionResultSummary
import com.example.vocablearningapp.ui.component.WordSessionResult
import com.example.vocablearningapp.ui.state.AppViewModel

@Composable
fun MatchModeScreen(
    set: VocabularySet?,
    onBack: () -> Unit,
    onRate: (String, FsrsRating) -> Unit,
    onSkipDailyWord: ((String) -> Unit)? = null,
    onLearnDailyWord: ((String) -> Unit)? = null
) {
    var selectedWordId by remember(set?.id) { mutableStateOf<String?>(null) }
    var selectedMeaningId by remember(set?.id) { mutableStateOf<String?>(null) }
    var matchedIds by remember(set?.id) { mutableStateOf(emptySet<String>()) }
    var wordMistakes by remember(set?.id) { mutableStateOf(setOf<String>()) }
    var hasMistake by remember(set?.id) { mutableStateOf(false) }
    val sessionWords = remember(set?.id) { set?.words.orEmpty().shuffled().take(4) }
    val meaningWords = remember(set?.id) { sessionWords.shuffled() }
    val isComplete = sessionWords.isNotEmpty() && matchedIds.size == sessionWords.size

    fun selectWord(item: VocabularyItem) {
        if (item.id !in matchedIds) {
            selectedWordId = item.id
            selectedMeaningId = null
            hasMistake = false
        }
    }

    fun selectMeaning(item: VocabularyItem) {
        val wordId = selectedWordId ?: return
        if (item.id in matchedIds) return

        selectedMeaningId = item.id
        if (item.id == wordId) {
            matchedIds = matchedIds + item.id
            onRate(item.id, FsrsRating.GOOD)
            selectedWordId = null
            selectedMeaningId = null
            hasMistake = false
        } else {
            onRate(wordId, FsrsRating.AGAIN)
            hasMistake = true
            wordMistakes = wordMistakes + wordId
        }
    }

    Scaffold(
        containerColor = Canvas,
        topBar = { VocabTopBar(title = "Match", onBack = onBack) }
    ) { innerPadding ->
        when {
            set == null || sessionWords.isEmpty() -> EmptyState(
                title = "No words to match",
                description = "Add a few words to this set before starting a match game.",
                modifier = Modifier.padding(innerPadding).padding(24.dp)
            )

            isComplete -> {
                val resultsList = sessionWords.map { word ->
                    WordSessionResult(
                        word = word,
                        isCorrect = word.id !in wordMistakes
                    )
                }
                SessionResultSummary(
                    title = "Match complete",
                    results = resultsList,
                    isDailyWords = set?.id == AppViewModel.DAILY_WORDS_SET_ID,
                    onBack = onBack,
                    onSkipDailyWord = onSkipDailyWord,
                    onLearnDailyWord = onLearnDailyWord,
                    modifier = Modifier.padding(innerPadding)
                )
            }

            else -> {
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
                        Text(text = "Match each pair", style = MaterialTheme.typography.titleMedium, color = Ink)
                        Text(text = "${matchedIds.size} / ${sessionWords.size}", style = MaterialTheme.typography.labelLarge, color = Accent)
                    }
                    VocabProgressBar(progress = matchedIds.size.toFloat() / sessionWords.size)
                    Text(
                        text = if (hasMistake) "Not a match. Choose another meaning." else "Select a word, then select its meaning.",
                        style = MaterialTheme.typography.bodyMedium,
                        color = if (hasMistake) Forgot else Muted
                    )

                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            Text(
                                text = "English",
                                style = MaterialTheme.typography.labelMedium,
                                color = Muted,
                                modifier = Modifier.weight(1f),
                                textAlign = TextAlign.Center
                            )
                            Text(
                                text = "Meaning",
                                style = MaterialTheme.typography.labelMedium,
                                color = Muted,
                                modifier = Modifier.weight(1f),
                                textAlign = TextAlign.Center
                            )
                        }

                        sessionWords.indices.forEach { index ->
                            val wordItem = sessionWords[index]
                            val meaningItem = meaningWords[index]
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(IntrinsicSize.Min),
                                horizontalArrangement = Arrangement.spacedBy(12.dp)
                            ) {
                                MatchCard(
                                    text = wordItem.word,
                                    selected = selectedWordId == wordItem.id,
                                    matched = wordItem.id in matchedIds,
                                    failed = false,
                                    onClick = { selectWord(wordItem) },
                                    modifier = Modifier
                                        .weight(1f)
                                        .fillMaxHeight()
                                )
                                MatchCard(
                                    text = meaningItem.meaning,
                                    selected = selectedMeaningId == meaningItem.id,
                                    matched = meaningItem.id in matchedIds,
                                    failed = hasMistake && selectedMeaningId == meaningItem.id,
                                    onClick = { selectMeaning(meaningItem) },
                                    modifier = Modifier
                                        .weight(1f)
                                        .fillMaxHeight()
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun MatchCard(
    text: String,
    selected: Boolean,
    matched: Boolean,
    failed: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val containerColor = when {
        matched -> MasteredSoft
        failed -> ForgotSoft
        selected -> AccentSoft
        else -> Surface
    }
    val contentColor = when {
        matched -> Mastered
        failed -> Forgot
        selected -> Accent
        else -> Ink
    }
    val borderColor = when {
        matched -> Mastered
        failed -> Forgot
        selected -> Accent
        else -> Border
    }
    Card(
        modifier = modifier
            .defaultMinSize(minHeight = 68.dp)
            .clickable(enabled = !matched, onClick = onClick),
        shape = RoundedCornerShape(15.dp),
        colors = CardDefaults.cardColors(containerColor = containerColor),
        border = BorderStroke(if (matched || selected || failed) 1.5.dp else 1.dp, borderColor)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 14.dp, vertical = 12.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = text,
                style = MaterialTheme.typography.bodyMedium.copy(
                    fontWeight = if (selected) FontWeight.SemiBold else FontWeight.Normal
                ),
                color = contentColor,
                textAlign = TextAlign.Center,
                maxLines = 3,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

@Composable
private fun MatchComplete(
    total: Int,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = VocabDimens.ScreenPadding),
        verticalArrangement = Arrangement.Center
    ) {
        Text(text = "Match complete", style = MaterialTheme.typography.headlineSmall, color = Ink)
        Spacer(modifier = Modifier.height(8.dp))
        Text(text = "You matched all $total pairs.", style = MaterialTheme.typography.bodyLarge, color = Muted)
        Spacer(modifier = Modifier.height(22.dp))
        PrimaryButton(text = "Back to set", onClick = onBack, modifier = Modifier.fillMaxWidth())
    }
}
