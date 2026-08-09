package com.example.vocablearningapp.ui.screen.home

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BookmarkBorder
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.vocablearningapp.domain.model.StudyMode
import com.example.vocablearningapp.domain.model.VocabularySet
import com.example.vocablearningapp.ui.component.AppScaffold
import com.example.vocablearningapp.ui.component.MainTab
import com.example.vocablearningapp.ui.component.SecondaryButton
import com.example.vocablearningapp.ui.component.SectionHeader
import com.example.vocablearningapp.ui.component.StudyModeCard
import com.example.vocablearningapp.ui.component.VocabDimens
import com.example.vocablearningapp.ui.component.VocabProgressBar
import com.example.vocablearningapp.ui.component.VocabularySetCard
import com.example.vocablearningapp.ui.theme.Accent
import com.example.vocablearningapp.ui.theme.AccentDark
import com.example.vocablearningapp.ui.theme.Ink
import com.example.vocablearningapp.ui.theme.Muted
import com.example.vocablearningapp.ui.theme.Surface
import com.example.vocablearningapp.ui.theme.White

@Composable
fun HomeScreen(
    onTabSelected: (MainTab) -> Unit,
    sets: List<VocabularySet>,
    onOpenSet: (String) -> Unit,
    onSeeAllSets: () -> Unit,
    onCreateSet: () -> Unit,
    onOpenPractice: (StudyMode) -> Unit,
    onContinueReview: () -> Unit
) {
    AppScaffold(
        selectedTab = MainTab.HOME,
        onTabSelected = onTabSelected
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(innerPadding)
                .padding(horizontal = VocabDimens.ScreenPadding, vertical = 18.dp),
            verticalArrangement = Arrangement.spacedBy(VocabDimens.SectionGap)
        ) {
            Header()
            DueTodayCard(onContinueReview = onContinueReview)
            WeeklyProgressCard()

            Column(verticalArrangement = Arrangement.spacedBy(14.dp)) {
                SectionHeader(
                    title = "Vocabulary sets",
                    actionLabel = "See all",
                    onAction = onSeeAllSets
                )
                MockSetPreview(
                    sets = sets.take(3),
                    onOpenSet = onOpenSet
                )
                SecondaryButton(
                    text = "Create a new set",
                    onClick = onCreateSet,
                    modifier = Modifier.fillMaxWidth(),
                    icon = Icons.Default.Edit
                )
            }

            Column(verticalArrangement = Arrangement.spacedBy(14.dp)) {
                SectionHeader(title = "Quick practice")
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .horizontalScroll(rememberScrollState()),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    QuickMode(
                        title = "Flashcards",
                        subtitle = "Review cards",
                        icon = Icons.Default.BookmarkBorder,
                        onClick = { onOpenPractice(StudyMode.FLASHCARDS) }
                    )
                    QuickMode(
                        title = "Quiz",
                        subtitle = "Test yourself",
                        icon = Icons.Default.Check,
                        onClick = { onOpenPractice(StudyMode.QUIZ) }
                    )
                    QuickMode(
                        title = "Fill blank",
                        subtitle = "Complete words",
                        icon = Icons.Default.Edit,
                        onClick = { onOpenPractice(StudyMode.FILL_IN_BLANK) }
                    )
                    QuickMode(
                        title = "Match",
                        subtitle = "Pair meanings",
                        icon = Icons.Default.PlayArrow,
                        onClick = { onOpenPractice(StudyMode.MATCH) }
                    )
                }
            }
        }
    }
}

@Composable
private fun Header() {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = "Good evening",
                style = MaterialTheme.typography.headlineSmall,
                color = Ink
            )
            Spacer(modifier = Modifier.height(5.dp))
            Text(
                text = "Ready for a little progress today?",
                style = MaterialTheme.typography.bodyMedium,
                color = Muted
            )
        }
    }
}

@Composable
private fun DueTodayCard(onContinueReview: () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = AccentDark)
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            Text(
                text = "TODAY'S FOCUS",
                style = MaterialTheme.typography.labelMedium,
                color = Color(0xFFBBDACC),
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(10.dp))
            Text(
                text = "12 words due today",
                style = MaterialTheme.typography.headlineSmall,
                color = White
            )
            Spacer(modifier = Modifier.height(5.dp))
            Text(
                text = "A short review keeps your memory moving forward.",
                style = MaterialTheme.typography.bodyMedium,
                color = Color(0xFFD2E5DB)
            )
            Spacer(modifier = Modifier.height(18.dp))
            Button(
                onClick = onContinueReview,
                modifier = Modifier.height(48.dp),
                shape = RoundedCornerShape(14.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = White,
                    contentColor = AccentDark
                )
            ) {
                Icon(imageVector = Icons.Default.PlayArrow, contentDescription = null)
                Spacer(modifier = Modifier.width(7.dp))
                Text(text = "Continue review", fontWeight = FontWeight.SemiBold)
            }
        }
    }
}

@Composable
private fun WeeklyProgressCard() {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(VocabDimens.CardRadius),
        colors = CardDefaults.cardColors(containerColor = Surface)
    ) {
        Column(modifier = Modifier.padding(18.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(text = "This week's progress", style = MaterialTheme.typography.titleMedium, color = Ink)
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(text = "42 / 60 words reviewed", style = MaterialTheme.typography.bodyMedium, color = Muted)
                }
                Text(text = "70%", style = MaterialTheme.typography.titleMedium, color = Accent)
            }
            Spacer(modifier = Modifier.height(15.dp))
            VocabProgressBar(progress = 0.7f)
        }
    }
}

@Composable
private fun MockSetPreview(
    sets: List<VocabularySet>,
    onOpenSet: (String) -> Unit
) {
    sets.forEach { item ->
        VocabularySetCard(
            set = item,
            onClick = { onOpenSet(item.id) },
            compact = true
        )
    }
}

@Composable
private fun QuickMode(
    title: String,
    subtitle: String,
    icon: ImageVector,
    onClick: () -> Unit
) {
    StudyModeCard(
        title = title,
        subtitle = subtitle,
        icon = icon,
        onClick = onClick,
        modifier = Modifier.width(154.dp)
    )
}
