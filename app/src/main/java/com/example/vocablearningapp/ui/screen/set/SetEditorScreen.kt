package com.example.vocablearningapp.ui.screen.set

import androidx.compose.foundation.BorderStroke
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
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.vocablearningapp.domain.model.MemoryLevel
import com.example.vocablearningapp.domain.model.VocabularyItem
import com.example.vocablearningapp.domain.model.VocabularySet
import com.example.vocablearningapp.ui.component.PrimaryButton
import com.example.vocablearningapp.ui.component.SecondaryButton
import com.example.vocablearningapp.ui.component.VocabDimens
import com.example.vocablearningapp.ui.component.VocabTopBar
import com.example.vocablearningapp.ui.theme.Border
import com.example.vocablearningapp.ui.theme.Canvas
import com.example.vocablearningapp.ui.theme.Ink
import com.example.vocablearningapp.ui.theme.Muted
import com.example.vocablearningapp.ui.theme.Surface

@Composable
fun SetEditorScreen(
    set: VocabularySet?,
    onBack: () -> Unit,
    onSave: (title: String, description: String, level: String, category: String, words: List<VocabularyItem>) -> Unit
) {
    var title by remember(set?.id) { mutableStateOf(set?.title.orEmpty()) }
    var description by remember(set?.id) { mutableStateOf(set?.description.orEmpty()) }
    var level by remember(set?.id) { mutableStateOf(set?.level ?: "A2") }
    var category by remember(set?.id) { mutableStateOf(set?.category ?: "General") }
    val draftWords = remember(set?.id) {
        mutableStateListOf<DraftWord>().apply {
            if (set == null || set.words.isEmpty()) {
                add(DraftWord())
            } else {
                addAll(set.words.map { DraftWord.from(it) })
            }
        }
    }

    Scaffold(
        containerColor = Canvas,
        topBar = {
            VocabTopBar(
                title = if (set == null) "Create set" else "Edit set",
                onBack = onBack,
                actions = {
                    androidx.compose.material3.TextButton(onClick = {
                        onSave(
                            title,
                            description,
                            level,
                            category,
                            draftWords.mapIndexedNotNull { index, draft -> draft.toVocabularyItem(title, index) }
                        )
                    }) {
                        Text(text = "Save", color = com.example.vocablearningapp.ui.theme.Accent)
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(innerPadding)
                .padding(horizontal = VocabDimens.ScreenPadding, vertical = 10.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            Column(verticalArrangement = Arrangement.spacedBy(13.dp)) {
                Text(text = "Set details", style = MaterialTheme.typography.titleLarge, color = Ink)
                EditorField(value = title, onValueChange = { title = it }, label = "Set name", placeholder = "e.g. Travel essentials")
                EditorField(value = description, onValueChange = { description = it }, label = "Description", placeholder = "What will you learn?")
                Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                    EditorField(
                        value = level,
                        onValueChange = { level = it },
                        label = "Level",
                        placeholder = "A2",
                        modifier = Modifier.weight(1f)
                    )
                    EditorField(
                        value = category,
                        onValueChange = { category = it },
                        label = "Category",
                        placeholder = "Everyday",
                        modifier = Modifier.weight(1f)
                    )
                }
            }

            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column {
                        Text(text = "Words", style = MaterialTheme.typography.titleLarge, color = Ink)
                        Spacer(modifier = Modifier.height(3.dp))
                        Text(text = "Add a term, meaning and context.", style = MaterialTheme.typography.bodyMedium, color = Muted)
                    }
                    Text(
                        text = "${draftWords.size}",
                        style = MaterialTheme.typography.titleLarge,
                        color = com.example.vocablearningapp.ui.theme.Accent
                    )
                }

                draftWords.forEachIndexed { index, draft ->
                    WordEditorCard(
                        index = index,
                        draft = draft,
                        canDelete = draftWords.size > 1,
                        onChange = { updated -> draftWords[index] = updated },
                        onDelete = { draftWords.removeAt(index) }
                    )
                }

                SecondaryButton(
                    text = "Add word",
                    onClick = { draftWords.add(DraftWord()) },
                    modifier = Modifier.fillMaxWidth(),
                    icon = Icons.Default.Add
                )
            }

            PrimaryButton(
                text = "Save set",
                onClick = {
                    onSave(
                        title,
                        description,
                        level,
                        category,
                        draftWords.mapIndexedNotNull { index, draft -> draft.toVocabularyItem(title, index) }
                    )
                },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Composable
private fun EditorField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    placeholder: String,
    modifier: Modifier = Modifier
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = modifier.fillMaxWidth(),
        label = { Text(label) },
        placeholder = { Text(placeholder) },
        singleLine = true,
        shape = RoundedCornerShape(14.dp)
    )
}

@Composable
private fun WordEditorCard(
    index: Int,
    draft: DraftWord,
    canDelete: Boolean,
    onChange: (DraftWord) -> Unit,
    onDelete: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(VocabDimens.CardRadius),
        colors = CardDefaults.cardColors(containerColor = Surface),
        border = BorderStroke(1.dp, Border)
    ) {
        Column(modifier = Modifier.padding(15.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(text = "Word ${index + 1}", style = MaterialTheme.typography.titleMedium, color = Ink)
                IconButton(onClick = onDelete, enabled = canDelete) {
                    Icon(imageVector = Icons.Default.Delete, contentDescription = "Delete word", tint = Muted)
                }
            }
            EditorField(value = draft.word, onValueChange = { onChange(draft.copy(word = it)) }, label = "Word", placeholder = "ambitious")
            EditorField(value = draft.meaning, onValueChange = { onChange(draft.copy(meaning = it)) }, label = "Meaning", placeholder = "having strong goals")
            EditorField(value = draft.pronunciation, onValueChange = { onChange(draft.copy(pronunciation = it)) }, label = "Pronunciation", placeholder = "/æmˈbɪʃ.əs/")
            EditorField(value = draft.exampleSentence, onValueChange = { onChange(draft.copy(exampleSentence = it)) }, label = "Example sentence", placeholder = "Use the word in context")
        }
    }
}

private data class DraftWord(
    val id: String = "",
    val word: String = "",
    val meaning: String = "",
    val pronunciation: String = "",
    val exampleSentence: String = "",
    val memoryLevel: MemoryLevel = MemoryLevel.NOT_REMEMBERED,
    val nextReviewDate: String = "Today"
) {
    fun toVocabularyItem(setTitle: String, index: Int): VocabularyItem? {
        if (word.isBlank() && meaning.isBlank()) return null
        return VocabularyItem(
            id = id.ifBlank { "${setTitle.lowercase().replace(" ", "-")}-$index" },
            word = word.trim(),
            meaning = meaning.trim(),
            pronunciation = pronunciation.trim(),
            exampleSentence = exampleSentence.trim(),
            memoryLevel = memoryLevel,
            nextReviewDate = nextReviewDate
        )
    }

    companion object {
        fun from(item: VocabularyItem) = DraftWord(
            id = item.id,
            word = item.word,
            meaning = item.meaning,
            pronunciation = item.pronunciation,
            exampleSentence = item.exampleSentence,
            memoryLevel = item.memoryLevel,
            nextReviewDate = item.nextReviewDate
        )
    }
}
