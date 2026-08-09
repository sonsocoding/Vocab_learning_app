package com.example.vocablearningapp.ui.screen.progress

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
import androidx.compose.material.icons.filled.BookmarkBorder
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.automirrored.filled.ShowChart
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.vocablearningapp.domain.model.MemoryLevel
import com.example.vocablearningapp.domain.model.VocabularyItem
import com.example.vocablearningapp.domain.model.VocabularySet
import com.example.vocablearningapp.ui.component.AppScaffold
import com.example.vocablearningapp.ui.component.MainTab
import com.example.vocablearningapp.ui.component.SectionHeader
import com.example.vocablearningapp.ui.component.StatCard
import com.example.vocablearningapp.ui.component.VocabDimens
import com.example.vocablearningapp.ui.component.VocabProgressBar
import com.example.vocablearningapp.ui.component.VocabularySetCard
import com.example.vocablearningapp.ui.theme.Accent
import com.example.vocablearningapp.ui.theme.Ink
import com.example.vocablearningapp.ui.theme.Muted
import com.example.vocablearningapp.ui.theme.Forgot
import com.example.vocablearningapp.ui.theme.Learning
import com.example.vocablearningapp.ui.theme.Mastered
import com.example.vocablearningapp.ui.theme.Surface

@Composable
fun ProgressScreen(
    onTabSelected: (MainTab) -> Unit,
    sets: List<VocabularySet>,
    items: List<VocabularyItem>,
    onOpenSet: (String) -> Unit
) {
    val totalWords = items.size
    val masteredWords = items.count { it.memoryLevel == MemoryLevel.MASTERED }

    AppScaffold(selectedTab = MainTab.PROGRESS, onTabSelected = onTabSelected) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(innerPadding)
                .padding(horizontal = VocabDimens.ScreenPadding, vertical = 18.dp),
            verticalArrangement = Arrangement.spacedBy(VocabDimens.SectionGap)
        ) {
            Column {
                Text(text = "Progress", style = MaterialTheme.typography.headlineSmall, color = Ink)
                Spacer(modifier = Modifier.height(5.dp))
                Text(text = "A quiet look at the work you have put in.", style = MaterialTheme.typography.bodyMedium, color = Muted)
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                StatCard(
                    value = totalWords.toString(),
                    label = "Words learned",
                    icon = Icons.Default.BookmarkBorder,
                    modifier = Modifier.weight(1f)
                )
                StatCard(
                    value = masteredWords.toString(),
                    label = "Words mastered",
                    icon = Icons.Default.Check,
                    modifier = Modifier.weight(1f)
                )
            }
            StatCard(
                value = "78%",
                label = "Review accuracy",
                icon = Icons.AutoMirrored.Filled.ShowChart,
                modifier = Modifier.fillMaxWidth()
            )

            MasteryBreakdown(items = items)

            Column(verticalArrangement = Arrangement.spacedBy(14.dp)) {
                SectionHeader(title = "Recent sets")
                sets.forEach { set ->
                    VocabularySetCard(set = set, onClick = { onOpenSet(set.id) }, compact = true)
                }
            }
        }
    }
}

@Composable
private fun MasteryBreakdown(items: List<VocabularyItem>) {
    Column(verticalArrangement = Arrangement.spacedBy(14.dp)) {
        SectionHeader(title = "Mastery")
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(VocabDimens.CardRadius),
            colors = CardDefaults.cardColors(containerColor = Surface)
        ) {
            Column(modifier = Modifier.padding(18.dp), verticalArrangement = Arrangement.spacedBy(17.dp)) {
                MasteryLine(
                    level = MemoryLevel.FORGOT,
                    count = items.count { it.memoryLevel == MemoryLevel.FORGOT },
                    total = items.size,
                    color = Forgot
                )
                MasteryLine(
                    level = MemoryLevel.LEARNING,
                    count = items.count { it.memoryLevel == MemoryLevel.LEARNING },
                    total = items.size,
                    color = Learning
                )
                MasteryLine(
                    level = MemoryLevel.MASTERED,
                    count = items.count { it.memoryLevel == MemoryLevel.MASTERED },
                    total = items.size,
                    color = Mastered
                )
            }
        }
    }
}

@Composable
private fun MasteryLine(level: MemoryLevel, count: Int, total: Int, color: Color) {
    Column(verticalArrangement = Arrangement.spacedBy(7.dp)) {
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Text(text = level.label, style = MaterialTheme.typography.bodyMedium, color = Ink)
            Text(text = "$count words", style = MaterialTheme.typography.bodyMedium, color = Muted)
        }
        VocabProgressBar(
            progress = if (total == 0) 0f else count.toFloat() / total,
            color = color
        )
    }
}
