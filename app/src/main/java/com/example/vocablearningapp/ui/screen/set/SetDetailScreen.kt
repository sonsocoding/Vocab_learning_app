package com.example.vocablearningapp.ui.screen.set

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.BookmarkBorder
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.unit.dp
import com.example.vocablearningapp.domain.model.StudyMode
import com.example.vocablearningapp.domain.model.VocabularySet
import com.example.vocablearningapp.ui.component.EmptyState
import com.example.vocablearningapp.ui.component.PrimaryButton
import com.example.vocablearningapp.ui.component.SectionHeader
import com.example.vocablearningapp.ui.component.StudyModeCard
import com.example.vocablearningapp.ui.component.VocabDimens
import com.example.vocablearningapp.ui.component.VocabProgressBar
import com.example.vocablearningapp.ui.component.VocabTopBar
import com.example.vocablearningapp.ui.component.VocabularyRow
import com.example.vocablearningapp.ui.theme.Accent
import com.example.vocablearningapp.ui.theme.AccentSoft
import com.example.vocablearningapp.ui.theme.Border
import com.example.vocablearningapp.ui.theme.Canvas
import com.example.vocablearningapp.ui.theme.Ink
import com.example.vocablearningapp.ui.theme.Muted
import com.example.vocablearningapp.ui.theme.Surface

@Composable
fun SetDetailScreen(
    set: VocabularySet?,
    onBack: () -> Unit,
    onStartLearning: () -> Unit,
    onOpenMode: (StudyMode) -> Unit,
    onEditSet: () -> Unit
) {
    var showMenu by remember { mutableStateOf(false) }

    Scaffold(
        containerColor = Canvas,
        topBar = {
            VocabTopBar(
                title = set?.title ?: "Vocabulary set",
                onBack = onBack,
                actions = {
                    IconButton(onClick = { showMenu = true }) {
                        Icon(imageVector = Icons.Default.MoreVert, contentDescription = "More", tint = Ink)
                    }
                    DropdownMenu(
                        expanded = showMenu,
                        onDismissRequest = { showMenu = false }
                    ) {
                        DropdownMenuItem(
                            text = { Text("Edit set") },
                            onClick = {
                                showMenu = false
                                onEditSet()
                            },
                            leadingIcon = { Icon(Icons.Default.Edit, contentDescription = null) }
                        )
                    }
                }
            )
        }
    ) { innerPadding ->
        if (set == null) {
            EmptyState(
                title = "Set not found",
                description = "This vocabulary set is no longer available.",
                modifier = Modifier.padding(innerPadding).padding(24.dp)
            )
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(innerPadding)
                    .padding(horizontal = VocabDimens.ScreenPadding, vertical = 8.dp),
                verticalArrangement = Arrangement.spacedBy(22.dp)
            ) {
                SetSummary(set = set)

                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    SectionHeader(title = "Study modes")
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        StudyModeCard(
                            title = StudyMode.FLASHCARDS.title,
                            subtitle = StudyMode.FLASHCARDS.subtitle,
                            icon = Icons.Default.BookmarkBorder,
                            onClick = { onOpenMode(StudyMode.FLASHCARDS) },
                            modifier = Modifier.weight(1f)
                        )
                        StudyModeCard(
                            title = StudyMode.LEARN.title,
                            subtitle = StudyMode.LEARN.subtitle,
                            icon = Icons.Default.MenuBook,
                            onClick = { onOpenMode(StudyMode.LEARN) },
                            modifier = Modifier.weight(1f)
                        )
                    }
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        StudyModeCard(
                            title = StudyMode.QUIZ.title,
                            subtitle = StudyMode.QUIZ.subtitle,
                            icon = Icons.Default.Check,
                            onClick = { onOpenMode(StudyMode.QUIZ) },
                            modifier = Modifier.weight(1f)
                        )
                        StudyModeCard(
                            title = StudyMode.MATCH.title,
                            subtitle = StudyMode.MATCH.subtitle,
                            icon = Icons.Default.PlayArrow,
                            onClick = { onOpenMode(StudyMode.MATCH) },
                            modifier = Modifier.weight(1f)
                        )
                    }
                }

                PrimaryButton(
                    text = "Start learning",
                    onClick = onStartLearning,
                    modifier = Modifier.fillMaxWidth(),
                    icon = Icons.Default.PlayArrow
                )

                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    SectionHeader(
                        title = "Vocabulary",
                        actionLabel = "Add word",
                        onAction = onEditSet
                    )
                    set.words.forEach { item -> VocabularyRow(item = item) }
                }

                PrimaryButton(
                    text = "Edit set",
                    onClick = onEditSet,
                    modifier = Modifier.fillMaxWidth(),
                    icon = Icons.Default.Edit
                )
                Spacer(modifier = Modifier.height(12.dp))
            }
        }
    }
}

@Composable
private fun SetSummary(set: VocabularySet) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(22.dp),
        colors = CardDefaults.cardColors(containerColor = Surface),
        border = androidx.compose.foundation.BorderStroke(1.dp, Border)
    ) {
        Column(modifier = Modifier.padding(18.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(text = set.category.uppercase(), style = MaterialTheme.typography.labelMedium, color = Accent)
                    Spacer(modifier = Modifier.height(7.dp))
                    Text(text = set.title, style = MaterialTheme.typography.headlineSmall, color = Ink)
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(text = "${set.words.size} words · Level ${set.level}", style = MaterialTheme.typography.bodyMedium, color = Muted)
                }
                Text(text = "${set.progress}%", style = MaterialTheme.typography.headlineSmall, color = Accent)
            }
            if (set.description.isNotBlank()) {
                Spacer(modifier = Modifier.height(14.dp))
                Text(text = set.description, style = MaterialTheme.typography.bodyMedium, color = Muted)
            }
            Spacer(modifier = Modifier.height(16.dp))
            VocabProgressBar(progress = set.progress / 100f)
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = "Current mastery", style = MaterialTheme.typography.labelMedium, color = Muted)
        }
    }
}
