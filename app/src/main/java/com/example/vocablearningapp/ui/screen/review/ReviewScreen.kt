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
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.vocablearningapp.domain.model.FsrsState
import com.example.vocablearningapp.domain.model.VocabularyItem
import com.example.vocablearningapp.domain.model.VocabularySet
import com.example.vocablearningapp.domain.srs.FsrsScheduler
import com.example.vocablearningapp.ui.component.AppScaffold
import com.example.vocablearningapp.ui.component.MainTab
import com.example.vocablearningapp.ui.component.PrimaryButton
import com.example.vocablearningapp.ui.component.SectionHeader
import com.example.vocablearningapp.ui.component.VocabDimens
import com.example.vocablearningapp.ui.component.VocabularyRow
import com.example.vocablearningapp.ui.component.VocabularySetCard
import com.example.vocablearningapp.ui.theme.Accent
import com.example.vocablearningapp.ui.theme.Ink
import com.example.vocablearningapp.ui.theme.Muted
import com.example.vocablearningapp.ui.theme.Surface

@Composable
fun ReviewScreen(
    onTabSelected: (MainTab) -> Unit,
    sets: List<VocabularySet>,
    items: List<VocabularyItem>,
    onStartReview: () -> Unit,
    onOpenSet: (String) -> Unit
) {
    var selectedFilter by remember { mutableStateOf<FsrsState?>(null) }
    val scheduler = remember { FsrsScheduler() }
    val nowMillis = System.currentTimeMillis()
    val filteredItems = selectedFilter?.let { state -> items.filter { it.fsrsState == state } } ?: items
    val dueItems = filteredItems.filter { scheduler.isDue(it, nowMillis) }

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
                    text = "FSRS schedules the right words for your daily review.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Muted
                )
            }

            ReviewHero(dueCount = dueItems.size, onStartReview = onStartReview)

            RecommendedReview(sets = sets, onOpenSet = onOpenSet)

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
                FsrsState.entries.forEach { state ->
                    FilterChip(
                        selected = selectedFilter == state,
                        onClick = { selectedFilter = state },
                        label = { Text(state.label) }
                    )
                }
            }

            ReviewSection(
                title = "Due today",
                subtitle = "${dueItems.size} cards are ready",
                items = dueItems.take(4)
            )
            ReviewSection(
                title = "New",
                subtitle = "Cards waiting for their first review",
                items = filteredItems.filter { it.fsrsState == FsrsState.NEW }.take(4)
            )
            ReviewSection(
                title = "Learning",
                subtitle = "Cards moving through learning steps",
                items = filteredItems.filter { it.fsrsState == FsrsState.LEARNING }.take(4)
            )
            ReviewSection(
                title = "Relearning",
                subtitle = "Cards being rebuilt after a lapse",
                items = filteredItems.filter { it.fsrsState == FsrsState.RELEARNING }.take(4)
            )
            ReviewSection(
                title = "Review",
                subtitle = "Cards that have graduated to long-term review",
                items = filteredItems.filter { it.fsrsState == FsrsState.REVIEW }.take(4)
            )
        }
    }
}

@Composable
private fun RecommendedReview(
    sets: List<VocabularySet>,
    onOpenSet: (String) -> Unit
) {
    val recommendedSets = sets
        .sortedWith(
            compareByDescending<VocabularySet> { set ->
                set.words.count { it.fsrsState != FsrsState.REVIEW }
            }.thenBy { it.progress }
        )
        .take(3)

    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
        SectionHeader(title = "Recommended review")
        Text(
            text = "Sets with cards still in New, Learning or Relearning.",
            style = MaterialTheme.typography.bodyMedium,
            color = Muted
        )
        recommendedSets.forEach { set ->
            VocabularySetCard(set = set, onClick = { onOpenSet(set.id) }, compact = true)
        }
    }
}

@Composable
private fun ReviewHero(
    dueCount: Int,
    onStartReview: () -> Unit
) {
    val estimatedMinutes = maxOf(1, (dueCount + 2) / 3)
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(22.dp),
        colors = CardDefaults.cardColors(containerColor = Surface)
    ) {
        Column(modifier = Modifier.padding(18.dp)) {
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Icon(imageVector = Icons.Default.Timer, contentDescription = null, tint = Accent)
                Text(text = "DAILY REVIEW", style = MaterialTheme.typography.labelMedium, color = Accent)
            }
            Spacer(modifier = Modifier.height(9.dp))
            Text(text = "$dueCount words due today", style = MaterialTheme.typography.titleLarge, color = Ink)
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = "Estimated $estimatedMinutes minutes", style = MaterialTheme.typography.bodyMedium, color = Muted)
            Spacer(modifier = Modifier.height(16.dp))
            PrimaryButton(
                text = "Start daily review",
                onClick = onStartReview,
                modifier = Modifier.fillMaxWidth(),
                icon = Icons.Default.PlayArrow,
                enabled = dueCount > 0
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
            Text(text = "No cards here yet.", style = MaterialTheme.typography.bodyMedium, color = Muted)
        } else {
            items.forEach { item -> VocabularyRow(item = item) }
        }
    }
}
