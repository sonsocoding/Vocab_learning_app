package com.example.vocablearningapp.ui.screen.set

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.vocablearningapp.domain.model.StudyMode
import com.example.vocablearningapp.domain.model.VocabularySet
import com.example.vocablearningapp.ui.component.PrimaryButton
import com.example.vocablearningapp.ui.component.VocabDimens
import com.example.vocablearningapp.ui.component.VocabTopBar
import com.example.vocablearningapp.ui.component.VocabularySetCard
import com.example.vocablearningapp.ui.theme.Canvas
import com.example.vocablearningapp.ui.theme.Ink
import com.example.vocablearningapp.ui.theme.Muted
import androidx.compose.material3.MaterialTheme

import com.example.vocablearningapp.ui.component.SecondaryButton
import androidx.compose.foundation.layout.Row
import androidx.compose.material.icons.filled.AutoAwesome

@Composable
fun AllSetsScreen(
    sets: List<VocabularySet>,
    onBack: () -> Unit,
    onOpenSet: (String) -> Unit,
    onCreateSet: () -> Unit,
    onGenerateAiSet: () -> Unit = {}
) {
    Scaffold(
        containerColor = Canvas,
        topBar = { VocabTopBar(title = "All sets", onBack = onBack) }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(innerPadding)
                .padding(horizontal = VocabDimens.ScreenPadding, vertical = 10.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            Text(text = "Your vocabulary", style = MaterialTheme.typography.headlineSmall, color = Ink)
            Text(text = "Keep every set in one calm place.", style = MaterialTheme.typography.bodyMedium, color = Muted)
            Spacer(modifier = Modifier.height(4.dp))
            sets.forEach { set ->
                VocabularySetCard(set = set, onClick = { onOpenSet(set.id) })
            }
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                PrimaryButton(
                    text = "New Set",
                    onClick = onCreateSet,
                    modifier = Modifier.weight(1f),
                    icon = Icons.Default.Add
                )
                SecondaryButton(
                    text = "AI Studio",
                    onClick = onGenerateAiSet,
                    modifier = Modifier.weight(1f),
                    icon = Icons.Default.AutoAwesome
                )
            }
            Spacer(modifier = Modifier.height(12.dp))
        }
    }
}

@Composable
fun PracticePlaceholderScreen(
    mode: StudyMode,
    onBack: () -> Unit
) {
    Scaffold(
        containerColor = Canvas,
        topBar = { VocabTopBar(title = mode.title, onBack = onBack) }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = VocabDimens.ScreenPadding, vertical = 24.dp),
            verticalArrangement = Arrangement.Center
        ) {
            androidx.compose.foundation.layout.Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(180.dp),
                contentAlignment = androidx.compose.ui.Alignment.Center
            ) {
                androidx.compose.material3.Icon(
                    imageVector = androidx.compose.material.icons.Icons.Default.PlayArrow,
                    contentDescription = null,
                    tint = com.example.vocablearningapp.ui.theme.Accent,
                    modifier = Modifier.height(56.dp)
                )
            }
            Text(text = "${mode.title} is ready for the next iteration.", style = MaterialTheme.typography.titleLarge, color = Ink)
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "The navigation and entry point are in place. You can add the exercise logic here when the learning model is ready.",
                style = MaterialTheme.typography.bodyLarge,
                color = Muted
            )
        }
    }
}
