package com.example.vocablearningapp.ui.screen.review

import androidx.compose.foundation.horizontalScroll
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
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.MaterialTheme
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
import com.example.vocablearningapp.ui.component.AppScaffold
import com.example.vocablearningapp.ui.component.MainTab
import com.example.vocablearningapp.ui.component.PrimaryButton
import com.example.vocablearningapp.ui.component.SectionHeader
import com.example.vocablearningapp.ui.component.VocabDimens
import com.example.vocablearningapp.ui.component.VocabularyRow
import com.example.vocablearningapp.ui.theme.Accent
import com.example.vocablearningapp.ui.theme.Ink
import com.example.vocablearningapp.ui.theme.Muted
import com.example.vocablearningapp.ui.theme.Surface

@Composable
fun ReviewScreen(
    onTabSelected: (MainTab) -> Unit,
    items: List<VocabularyItem>,
    onStartReview: () -> Unit
) {
    var selectedFilter by remember { mutableStateOf<MemoryLevel?>(null) }
    val filteredItems = selectedFilter?.let { level -> items.filter { it.memoryLevel == level } } ?: items

    AppScaffold(selectedTab = MainTab.REVIEW, onTabSelected = onTabSelected) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(innerPadding)
                .padding(horizontal = VocabDimens.ScreenPadding, vertical = 18.dp),
            verticalArrangement = Arrangement.spacedBy(VocabDimens.SectionGap)
        ) {
            Column {
                Text(text = "Review", style = MaterialTheme.typography.headlineSmall, color = Ink)
                Spacer(modifier = Modifier.height(5.dp))
                Text(
                    text = "Small, regular reviews make new words stick.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Muted
                )
            }

            ReviewHero(onStartReview = onStartReview)

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState()),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                FilterChip(
                    selected = selectedFilter == null,
                    onClick = { selectedFilter = null },
                    label = { Text("All") }
                )
                MemoryLevel.entries.forEach { level ->
                    FilterChip(
                        selected = selectedFilter == level,
                        onClick = { selectedFilter = level },
                        label = { Text(level.label) }
                    )
                }
            }

            ReviewSection(
                title = "Due today",
                subtitle = "${filteredItems.count { it.nextReviewDate == "Today" }} words are ready",
                items = filteredItems.filter { it.nextReviewDate == "Today" }.take(4)
            )
            ReviewSection(
                title = "Weak words",
                subtitle = "Rated Not remembered",
                items = filteredItems.filter { it.memoryLevel == MemoryLevel.NOT_REMEMBERED }.take(4)
            )
            ReviewSection(
                title = "Learning",
                subtitle = "Rated Somewhat remembered",
                items = filteredItems.filter { it.memoryLevel == MemoryLevel.SOMEWHAT_REMEMBERED }.take(4)
            )
            ReviewSection(
                title = "Mastered",
                subtitle = "Rated Remembered",
                items = filteredItems.filter { it.memoryLevel == MemoryLevel.REMEMBERED }.take(4)
            )
        }
    }
}

@Composable
private fun ReviewHero(onStartReview: () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(22.dp),
        colors = CardDefaults.cardColors(containerColor = Surface)
    ) {
        Column(modifier = Modifier.padding(18.dp)) {
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                androidx.compose.material3.Icon(imageVector = Icons.Default.Timer, contentDescription = null, tint = Accent)
                Text(text = "TODAY'S REVIEW", style = MaterialTheme.typography.labelMedium, color = Accent)
            }
            Spacer(modifier = Modifier.height(9.dp))
            Text(text = "12 words due today", style = MaterialTheme.typography.titleLarge, color = Ink)
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = "Estimated 8 minutes", style = MaterialTheme.typography.bodyMedium, color = Muted)
            Spacer(modifier = Modifier.height(16.dp))
            PrimaryButton(
                text = "Start review",
                onClick = onStartReview,
                modifier = Modifier.fillMaxWidth(),
                icon = Icons.Default.PlayArrow
            )
        }
    }
}

@Composable
private fun ReviewSection(
    title: String,
    subtitle: String,
    items: List<VocabularyItem>
) {
    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
        SectionHeader(title = title)
        Text(text = subtitle, style = MaterialTheme.typography.bodyMedium, color = Muted)
        if (items.isEmpty()) {
            Text(text = "No words here yet.", style = MaterialTheme.typography.bodyMedium, color = Muted)
        } else {
            items.forEach { item -> VocabularyRow(item = item) }
        }
    }
}
