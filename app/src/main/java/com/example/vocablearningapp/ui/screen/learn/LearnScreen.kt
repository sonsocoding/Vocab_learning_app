package com.example.vocablearningapp.ui.screen.learn

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BookmarkBorder
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import com.example.vocablearningapp.domain.model.StudyMode
import com.example.vocablearningapp.domain.model.VocabularySet
import com.example.vocablearningapp.ui.component.AppScaffold
import com.example.vocablearningapp.ui.component.MainTab
import com.example.vocablearningapp.ui.component.PrimaryButton
import com.example.vocablearningapp.ui.component.SectionHeader
import com.example.vocablearningapp.ui.component.StudyModeCard
import com.example.vocablearningapp.ui.component.VocabDimens
import com.example.vocablearningapp.ui.component.VocabProgressBar
import com.example.vocablearningapp.ui.component.VocabularySetCard
import com.example.vocablearningapp.ui.theme.Accent
import com.example.vocablearningapp.ui.theme.Ink
import com.example.vocablearningapp.ui.theme.Muted
import com.example.vocablearningapp.ui.theme.Surface

@Composable
fun LearnScreen(
    onTabSelected: (MainTab) -> Unit,
    sets: List<VocabularySet>,
    onOpenSet: (String) -> Unit,
    onOpenFlashcards: (String) -> Unit,
    onOpenMode: (StudyMode) -> Unit
) {
    AppScaffold(selectedTab = MainTab.LEARN, onTabSelected = onTabSelected) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(innerPadding)
                .padding(horizontal = VocabDimens.ScreenPadding, vertical = 18.dp),
            verticalArrangement = Arrangement.spacedBy(VocabDimens.SectionGap)
        ) {
            Column {
                Text(text = "Learn", style = MaterialTheme.typography.headlineSmall, color = Ink)
                Spacer(modifier = Modifier.height(5.dp))
                Text(
                    text = "Choose a way to build your vocabulary today.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Muted
                )
            }

            sets.firstOrNull()?.let { set ->
                ContinueLearningCard(set = set, onContinue = { onOpenFlashcards(set.id) })
            }

            Column(verticalArrangement = Arrangement.spacedBy(14.dp)) {
                SectionHeader(title = "Recommended review")
                sets.sortedBy { it.progress }.take(2).forEach { set ->
                    VocabularySetCard(set = set, onClick = { onOpenSet(set.id) }, compact = true)
                }
            }

            Column(verticalArrangement = Arrangement.spacedBy(14.dp)) {
                SectionHeader(title = "Practice modes")
                ModeRow(
                    left = ModeDefinition(StudyMode.FLASHCARDS, Icons.Default.BookmarkBorder),
                    right = ModeDefinition(StudyMode.MULTIPLE_CHOICE, Icons.Default.Check),
                    onOpenMode = { mode ->
                        if (mode == StudyMode.FLASHCARDS) sets.firstOrNull()?.let { onOpenFlashcards(it.id) }
                        else onOpenMode(mode)
                    }
                )
                ModeRow(
                    left = ModeDefinition(StudyMode.FILL_IN_BLANK, Icons.Default.Edit),
                    right = ModeDefinition(StudyMode.MATCH, Icons.Default.PlayArrow),
                    onOpenMode = onOpenMode
                )
                ModeRow(
                    left = ModeDefinition(StudyMode.SENTENCE_PRACTICE, Icons.Default.MenuBook),
                    right = ModeDefinition(StudyMode.LEARN, Icons.Default.BookmarkBorder),
                    onOpenMode = onOpenMode
                )
            }

            Column(verticalArrangement = Arrangement.spacedBy(14.dp)) {
                SectionHeader(title = "Your sets")
                sets.forEach { set ->
                    VocabularySetCard(set = set, onClick = { onOpenSet(set.id) }, compact = true)
                }
            }
        }
    }
}

@Composable
private fun ContinueLearningCard(set: VocabularySet, onContinue: () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = androidx.compose.foundation.shape.RoundedCornerShape(22.dp),
        colors = CardDefaults.cardColors(containerColor = Surface)
    ) {
        Column(modifier = Modifier.padding(18.dp)) {
            Text(text = "CONTINUE LEARNING", style = MaterialTheme.typography.labelMedium, color = Accent)
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = set.title, style = MaterialTheme.typography.titleLarge, color = Ink)
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = "${set.words.size} words · ${set.progress}% mastered", style = MaterialTheme.typography.bodyMedium, color = Muted)
            Spacer(modifier = Modifier.height(14.dp))
            VocabProgressBar(progress = set.progress / 100f)
            Spacer(modifier = Modifier.height(16.dp))
            PrimaryButton(text = "Resume", onClick = onContinue, icon = Icons.Default.PlayArrow)
        }
    }
}

private data class ModeDefinition(val mode: StudyMode, val icon: ImageVector)

@Composable
private fun ModeRow(
    left: ModeDefinition,
    right: ModeDefinition,
    onOpenMode: (StudyMode) -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        StudyModeCard(
            title = left.mode.title,
            subtitle = left.mode.subtitle,
            icon = left.icon,
            onClick = { onOpenMode(left.mode) },
            modifier = Modifier.weight(1f)
        )
        StudyModeCard(
            title = right.mode.title,
            subtitle = right.mode.subtitle,
            icon = right.icon,
            onClick = { onOpenMode(right.mode) },
            modifier = Modifier.weight(1f)
        )
    }
}
