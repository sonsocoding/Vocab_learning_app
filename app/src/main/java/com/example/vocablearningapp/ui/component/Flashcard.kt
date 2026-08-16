package com.example.vocablearningapp.ui.component

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.vocablearningapp.domain.model.VocabularyItem
import com.example.vocablearningapp.ui.theme.Accent
import com.example.vocablearningapp.ui.theme.Border
import com.example.vocablearningapp.ui.theme.Ink
import com.example.vocablearningapp.ui.theme.Muted
import com.example.vocablearningapp.ui.theme.Surface
import com.example.vocablearningapp.ui.theme.SurfaceMuted

@Composable
fun Flashcard(
    item: VocabularyItem,
    isFlipped: Boolean,
    onFlip: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .height(370.dp)
            .clickable(onClick = onFlip),
        shape = RoundedCornerShape(26.dp),
        colors = CardDefaults.cardColors(containerColor = Surface),
        border = BorderStroke(1.dp, Border),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        if (!isFlipped) {
            FrontOfCard(item = item)
        } else {
            BackOfCard(item = item)
        }
    }
}

@Composable
private fun FrontOfCard(item: VocabularyItem) {
    Box(modifier = Modifier.fillMaxSize().padding(24.dp)) {
        SpeechButton(
            text = item.word,
            contentDescription = "Play word pronunciation",
            modifier = Modifier.align(Alignment.TopEnd)
        )
        Column(
            modifier = Modifier.align(Alignment.Center),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = item.word, style = MaterialTheme.typography.displaySmall, color = Ink, textAlign = TextAlign.Center)
            Spacer(modifier = Modifier.height(10.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(text = item.pronunciation, style = MaterialTheme.typography.bodyMedium, color = Muted)
                Spacer(modifier = Modifier.size(8.dp))
                Text(text = item.partOfSpeech.label, style = MaterialTheme.typography.labelMedium, color = Accent)
            }
        }
    }
}

@Composable
private fun BackOfCard(item: VocabularyItem) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(text = item.word, style = MaterialTheme.typography.titleMedium, color = Accent)
            SpeechButton(
                text = item.word,
                contentDescription = "Play word pronunciation",
                tint = Accent
            )
        }
        Spacer(modifier = Modifier.height(12.dp))
        val meaningEntries = com.example.vocablearningapp.domain.util.MeaningParser.parse(
            rawMeaning = item.meaning,
            rawExample = item.exampleSentence,
            fallbackPos = item.partOfSpeech,
            word = item.word
        )

        if (meaningEntries.size > 1) {
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                meaningEntries.forEach { entry ->
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center
                        ) {
                            Surface(
                                color = com.example.vocablearningapp.ui.theme.AccentSoft,
                                shape = RoundedCornerShape(50)
                            ) {
                                Text(
                                    text = entry.partOfSpeech.label,
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp),
                                    style = MaterialTheme.typography.labelSmall,
                                    color = Accent,
                                    fontWeight = androidx.compose.ui.text.font.FontWeight.Bold
                                )
                            }
                            Spacer(modifier = Modifier.size(6.dp))
                            Text(
                                text = entry.meaning,
                                style = MaterialTheme.typography.titleMedium,
                                color = Ink,
                                textAlign = TextAlign.Center
                            )
                        }
                        if (entry.exampleSentence.isNotBlank()) {
                            Spacer(modifier = Modifier.height(3.dp))
                            Text(
                                text = "“${entry.exampleSentence}”",
                                style = MaterialTheme.typography.bodySmall.copy(fontStyle = FontStyle.Italic),
                                color = Muted,
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                }
            }
        } else {
            Text(
                text = item.meaning,
                style = MaterialTheme.typography.headlineSmall,
                color = Ink,
                textAlign = TextAlign.Center
            )
            val displayExample = com.example.vocablearningapp.domain.util.ExampleSentenceGenerator.getNaturalExample(
                word = item.word,
                partOfSpeech = item.partOfSpeech,
                existingSentence = item.exampleSentence
            )
            if (displayExample.isNotBlank()) {
                Spacer(modifier = Modifier.height(18.dp))
                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    color = SurfaceMuted,
                    shape = RoundedCornerShape(15.dp)
                ) {
                    Row(
                        modifier = Modifier.padding(start = 15.dp, top = 10.dp, end = 8.dp, bottom = 10.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "“$displayExample”",
                            modifier = Modifier.weight(1f),
                            style = MaterialTheme.typography.bodyMedium.copy(fontStyle = FontStyle.Italic),
                            color = Ink,
                            textAlign = TextAlign.Center
                        )
                        SpeechButton(
                            text = displayExample,
                            contentDescription = "Play example sentence",
                            tint = Accent
                        )
                    }
                }
            }
        }
    }
}
