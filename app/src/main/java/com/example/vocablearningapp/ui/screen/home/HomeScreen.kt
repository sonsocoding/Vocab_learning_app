package com.example.vocablearningapp.ui.screen.home

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
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.vocablearningapp.domain.model.VocabularySet
import com.example.vocablearningapp.ui.component.AppScaffold
import com.example.vocablearningapp.ui.component.MainTab
import com.example.vocablearningapp.ui.component.SecondaryButton
import com.example.vocablearningapp.ui.component.SectionHeader
import com.example.vocablearningapp.ui.component.VocabDimens
import com.example.vocablearningapp.ui.component.VocabularySetCard
import com.example.vocablearningapp.ui.theme.Accent
import com.example.vocablearningapp.ui.theme.AccentDark
import com.example.vocablearningapp.ui.theme.Ink
import com.example.vocablearningapp.ui.theme.Muted
import com.example.vocablearningapp.ui.theme.White

import com.example.vocablearningapp.data.StreakState
import com.example.vocablearningapp.ui.theme.Border
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Surface

@Composable
fun HomeScreen(
    onTabSelected: (MainTab) -> Unit,
    sets: List<VocabularySet>,
    onOpenSet: (String) -> Unit,
    onSeeAllSets: () -> Unit,
    onCreateSet: () -> Unit,
    dailyWords: VocabularySet,
    onOpenDailyWords: () -> Unit,
    onOpenAiChat: () -> Unit = {},
    streakState: StreakState = StreakState()
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
            Header(streakState = streakState)
            StreakCard(streakState = streakState)
            AiTutorBanner(onOpenAiChat = onOpenAiChat)
            DailyWordsCard(dailyWords = dailyWords, onOpenDailyWords = onOpenDailyWords)

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

        }
    }
}

@Composable
private fun Header(streakState: StreakState) {
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
            Spacer(modifier = Modifier.height(3.dp))
            Text(
                text = "Ready for a little progress today?",
                style = MaterialTheme.typography.bodyMedium,
                color = Muted
            )
        }
        Surface(
            color = Color(0xFFFFF3E0),
            shape = RoundedCornerShape(50),
            border = BorderStroke(1.dp, Color(0xFFFFCC80))
        ) {
            Row(
                modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = "🔥", style = MaterialTheme.typography.bodyMedium)
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = "${streakState.currentStreak}",
                    style = MaterialTheme.typography.titleMedium,
                    color = Color(0xFFE65100),
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@Composable
private fun StreakCard(streakState: StreakState) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(22.dp),
        colors = CardDefaults.cardColors(containerColor = com.example.vocablearningapp.ui.theme.Surface),
        border = BorderStroke(1.dp, Border)
    ) {
        Column(modifier = Modifier.padding(18.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(text = "🔥", style = MaterialTheme.typography.titleLarge)
                    Spacer(modifier = Modifier.width(8.dp))
                    Column {
                        Text(
                            text = "${streakState.currentStreak} Day Streak",
                            style = MaterialTheme.typography.titleMedium,
                            color = Ink
                        )
                        Text(
                            text = "Best record: ${streakState.bestStreak} days",
                            style = MaterialTheme.typography.bodySmall,
                            color = Muted
                        )
                    }
                }
                Surface(
                    color = if (streakState.isStudiedToday) Color(0xFFE8F5E9) else Color(0xFFFFF3E0),
                    shape = RoundedCornerShape(50)
                ) {
                    Text(
                        text = if (streakState.isStudiedToday) "Active Today ✓" else "Study Today!",
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp),
                        style = MaterialTheme.typography.labelSmall,
                        color = if (streakState.isStudiedToday) Color(0xFF2E7D32) else Color(0xFFE65100),
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            val dayLabels = listOf("M", "T", "W", "T", "F", "S", "S")
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                dayLabels.forEachIndexed { index, label ->
                    val dayNum = index + 1
                    val isActive = dayNum in streakState.activeDaysThisWeek
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Surface(
                            modifier = Modifier.size(36.dp),
                            shape = RoundedCornerShape(10.dp),
                            color = if (isActive) Accent else com.example.vocablearningapp.ui.theme.Surface,
                            border = if (isActive) null else BorderStroke(1.dp, Border)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Text(
                                    text = if (isActive) "🔥" else label,
                                    style = MaterialTheme.typography.labelMedium,
                                    color = if (isActive) White else Muted
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
private fun DailyWordsCard(
    dailyWords: VocabularySet,
    onOpenDailyWords: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = AccentDark)
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            Text(
                text = "DAILY WORDS",
                style = MaterialTheme.typography.labelMedium,
                color = Color(0xFFBBDACC),
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(10.dp))
            Text(
                text = "${dailyWords.words.size} fresh words for level ${dailyWords.level}",
                style = MaterialTheme.typography.headlineSmall,
                color = White
            )
            Spacer(modifier = Modifier.height(5.dp))
            Text(
                text = "Generated from the level you have been learning recently.",
                style = MaterialTheme.typography.bodyMedium,
                color = Color(0xFFD2E5DB)
            )
            Spacer(modifier = Modifier.height(18.dp))
            Button(
                onClick = onOpenDailyWords,
                modifier = Modifier.height(48.dp),
                shape = RoundedCornerShape(14.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = White,
                    contentColor = AccentDark
                )
            ) {
                Icon(imageVector = Icons.Default.PlayArrow, contentDescription = null)
                Spacer(modifier = Modifier.width(7.dp))
                Text(text = "Open daily set", fontWeight = FontWeight.SemiBold)
            }
        }
    }
}

@Composable
private fun AiTutorBanner(onOpenAiChat: () -> Unit) {
    Card(
        onClick = onOpenAiChat,
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(22.dp),
        colors = CardDefaults.cardColors(containerColor = androidx.compose.material3.MaterialTheme.colorScheme.surface),
        border = BorderStroke(1.dp, Color(0xFFC8E6C9))
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            Surface(
                shape = androidx.compose.foundation.shape.CircleShape,
                color = com.example.vocablearningapp.ui.theme.AccentSoft,
                modifier = Modifier.size(46.dp)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(
                        imageVector = Icons.Default.AutoAwesome,
                        contentDescription = null,
                        tint = Accent,
                        modifier = Modifier.size(24.dp)
                    )
                }
            }

            Column(modifier = Modifier.weight(1f)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = "VocabAI Coach",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = Ink
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Surface(
                        color = com.example.vocablearningapp.ui.theme.AccentSoft,
                        shape = RoundedCornerShape(6.dp)
                    ) {
                        Text(
                            text = "AI TUTOR",
                            style = MaterialTheme.typography.labelSmall,
                            color = AccentDark,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                        )
                    }
                }
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = "Generate custom sets & ask any English questions",
                    style = MaterialTheme.typography.bodySmall,
                    color = Muted
                )
            }

            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                contentDescription = null,
                tint = Muted,
                modifier = Modifier.size(18.dp)
            )
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
