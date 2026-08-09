package com.example.vocablearningapp.ui.component

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
        IconButton(
            onClick = { /* Audio playback will be connected later. */ },
            modifier = Modifier.align(Alignment.TopEnd)
        ) {
            Icon(imageVector = Icons.Default.VolumeUp, contentDescription = "Play pronunciation", tint = Accent)
        }
        Column(
            modifier = Modifier.align(Alignment.Center),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = item.word, style = MaterialTheme.typography.displaySmall, color = Ink, textAlign = TextAlign.Center)
            Spacer(modifier = Modifier.height(10.dp))
            Text(text = item.pronunciation, style = MaterialTheme.typography.bodyMedium, color = Muted)
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
        Text(text = item.word, style = MaterialTheme.typography.titleMedium, color = Accent)
        Spacer(modifier = Modifier.height(12.dp))
        Text(
            text = item.meaning,
            style = MaterialTheme.typography.headlineSmall,
            color = Ink,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(20.dp))
        Surface(color = SurfaceMuted, shape = RoundedCornerShape(15.dp)) {
            Text(
                text = "“${item.exampleSentence}”",
                modifier = Modifier.padding(15.dp),
                style = MaterialTheme.typography.bodyMedium.copy(fontStyle = FontStyle.Italic),
                color = Ink,
                textAlign = TextAlign.Center
            )
        }
    }
}
