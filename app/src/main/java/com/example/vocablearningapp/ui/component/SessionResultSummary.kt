package com.example.vocablearningapp.ui.component

import androidx.compose.foundation.BorderStroke
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
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.vocablearningapp.domain.model.VocabularyItem
import com.example.vocablearningapp.ui.theme.Accent
import com.example.vocablearningapp.ui.theme.Border
import com.example.vocablearningapp.ui.theme.Forgot
import com.example.vocablearningapp.ui.theme.ForgotSoft
import com.example.vocablearningapp.ui.theme.Ink
import com.example.vocablearningapp.ui.theme.Mastered
import com.example.vocablearningapp.ui.theme.MasteredSoft
import com.example.vocablearningapp.ui.theme.Muted
import com.example.vocablearningapp.ui.theme.Surface as AppSurface
import com.example.vocablearningapp.ui.theme.White

data class WordSessionResult(
    val word: VocabularyItem,
    val isCorrect: Boolean
)

@Composable
fun SessionResultSummary(
    title: String,
    results: List<WordSessionResult>,
    isDailyWords: Boolean,
    onBack: () -> Unit,
    onSkipDailyWord: ((String) -> Unit)? = null,
    onLearnDailyWord: ((String) -> Unit)? = null,
    modifier: Modifier = Modifier
) {
    var skippedIds by remember { mutableStateOf(setOf<String>()) }
    var learnedIds by remember { mutableStateOf(setOf<String>()) }

    val correctCount = results.count { it.isCorrect }
    val total = results.size
    val accuracy = if (total > 0) (correctCount * 100) / total else 0

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = VocabDimens.ScreenPadding, vertical = 14.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(22.dp),
            colors = CardDefaults.cardColors(containerColor = AppSurface),
            border = BorderStroke(1.dp, Border)
        ) {
            Column(modifier = Modifier.padding(20.dp)) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.headlineSmall,
                    color = Ink
                )
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = "$correctCount of $total correct ($accuracy% accuracy)",
                    style = MaterialTheme.typography.bodyLarge,
                    color = Muted
                )
            }
        }

        Text(
            text = "Detailed Breakdown",
            style = MaterialTheme.typography.titleMedium,
            color = Ink
        )

        results.forEach { result ->
            val item = result.word
            val isSkipped = item.id in skippedIds
            val isLearned = item.id in learnedIds

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = when {
                        isLearned -> MasteredSoft
                        isSkipped -> ForgotSoft
                        else -> AppSurface
                    }
                ),
                border = BorderStroke(1.dp, Border)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = item.word,
                                style = MaterialTheme.typography.titleMedium,
                                color = Ink
                            )
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                text = "${item.pronunciation} · ${item.meaning}",
                                style = MaterialTheme.typography.bodyMedium,
                                color = Muted
                            )
                        }

                        val badgeColor = if (result.isCorrect) MasteredSoft else ForgotSoft
                        val textColor = if (result.isCorrect) Mastered else Forgot
                        Surface(
                            color = badgeColor,
                            shape = RoundedCornerShape(50)
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                Icon(
                                    imageVector = if (result.isCorrect) Icons.Default.Check else Icons.Default.Close,
                                    contentDescription = null,
                                    tint = textColor,
                                    modifier = Modifier.height(14.dp)
                                )
                                Text(
                                    text = if (result.isCorrect) "Correct" else "Incorrect",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = textColor
                                )
                            }
                        }
                    }

                    if (isDailyWords) {
                        Spacer(modifier = Modifier.height(12.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.End,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            if (isSkipped) {
                                Text(
                                    text = "Skipped",
                                    style = MaterialTheme.typography.labelMedium,
                                    color = Forgot
                                )
                            } else if (isLearned) {
                                Text(
                                    text = "Learned",
                                    style = MaterialTheme.typography.labelMedium,
                                    color = Mastered
                                )
                            } else {
                                OutlinedButton(
                                    onClick = {
                                        onSkipDailyWord?.invoke(item.id)
                                        skippedIds = skippedIds + item.id
                                    },
                                    modifier = Modifier.height(36.dp),
                                    shape = RoundedCornerShape(10.dp),
                                    border = BorderStroke(1.dp, Border)
                                ) {
                                    Text(
                                        text = "Skip",
                                        style = MaterialTheme.typography.labelMedium,
                                        color = Muted
                                    )
                                }
                                Spacer(modifier = Modifier.width(8.dp))
                                Button(
                                    onClick = {
                                        onLearnDailyWord?.invoke(item.id)
                                        learnedIds = learnedIds + item.id
                                    },
                                    modifier = Modifier.height(36.dp),
                                    shape = RoundedCornerShape(10.dp),
                                    colors = ButtonDefaults.buttonColors(containerColor = Accent)
                                ) {
                                    Text(
                                        text = "Learn",
                                        style = MaterialTheme.typography.labelMedium,
                                        color = White
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(8.dp))
        PrimaryButton(
            text = "Back to set",
            onClick = onBack,
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(16.dp))
    }
}
