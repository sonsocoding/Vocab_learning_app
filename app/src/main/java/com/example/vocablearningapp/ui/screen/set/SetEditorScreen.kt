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
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Surface
import androidx.compose.ui.Alignment
import androidx.compose.ui.text.font.FontWeight
import com.example.vocablearningapp.domain.util.IpaGenerator
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
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
import com.example.vocablearningapp.domain.model.FsrsState
import com.example.vocablearningapp.domain.model.PartOfSpeech
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
    placeholder: String = "",
    modifier: Modifier = Modifier
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = modifier.fillMaxWidth(),
        label = { Text(label) },
        placeholder = if (placeholder.isNotBlank()) { { Text(placeholder) } } else null,
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
    var wordText by remember(draft.id) { mutableStateOf(draft.word) }
    var pronunciationText by remember(draft.id) { mutableStateOf(draft.pronunciation) }
    var exampleText by remember(draft.id) { mutableStateOf(draft.exampleSentence) }

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(VocabDimens.CardRadius),
        colors = CardDefaults.cardColors(containerColor = Surface),
        border = BorderStroke(1.dp, Border)
    ) {
        Column(modifier = Modifier.padding(15.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = "Word ${index + 1}", style = MaterialTheme.typography.titleMedium, color = Ink)
                IconButton(onClick = onDelete, enabled = canDelete) {
                    Icon(imageVector = Icons.Default.Delete, contentDescription = "Delete word", tint = Muted)
                }
            }

            EditorField(
                value = wordText,
                onValueChange = { newWord ->
                    wordText = newWord
                    val autoIpa = if (pronunciationText.isBlank()) IpaGenerator.generateIpa(newWord) else pronunciationText
                    if (pronunciationText.isBlank() && autoIpa.isNotBlank()) {
                        pronunciationText = autoIpa
                    }
                    onChange(draft.copy(word = newWord, pronunciation = pronunciationText))
                },
                label = "Word",
                placeholder = ""
            )

            IpaFieldWithHelper(
                pronunciation = pronunciationText,
                word = wordText,
                onPronunciationChange = { newIpa ->
                    pronunciationText = newIpa
                    onChange(draft.copy(pronunciation = newIpa))
                }
            )

            Text(
                text = "Meanings & Parts of Speech",
                style = MaterialTheme.typography.labelLarge,
                color = Ink,
                fontWeight = FontWeight.Bold
            )

            draft.meanings.forEachIndexed { mIndex, mDraft ->
                MeaningEditorRow(
                    index = mIndex,
                    meaning = mDraft,
                    canDeleteMeaning = draft.meanings.size > 1,
                    onMeaningChange = { updatedMeaning ->
                        val updatedList = draft.meanings.toMutableList()
                        updatedList[mIndex] = updatedMeaning
                        onChange(draft.copy(meanings = updatedList))
                    },
                    onDeleteMeaning = {
                        val updatedList = draft.meanings.toMutableList()
                        updatedList.removeAt(mIndex)
                        onChange(draft.copy(meanings = updatedList))
                    }
                )
            }

            SecondaryButton(
                text = "+ Thêm nghĩa (n / v / adj)",
                onClick = {
                    val updatedList = draft.meanings + DraftMeaning()
                    onChange(draft.copy(meanings = updatedList))
                },
                modifier = Modifier.fillMaxWidth()
            )

            EditorField(
                value = exampleText,
                onValueChange = { newExample ->
                    exampleText = newExample
                    onChange(draft.copy(exampleSentence = newExample))
                },
                label = "Example sentence",
                placeholder = ""
            )
        }
    }
}

@Composable
private fun MeaningEditorRow(
    index: Int,
    meaning: DraftMeaning,
    canDeleteMeaning: Boolean,
    onMeaningChange: (DraftMeaning) -> Unit,
    onDeleteMeaning: () -> Unit
) {
    var localMeaningText by remember(meaning.id) { mutableStateOf(meaning.meaning) }

    Surface(
        modifier = Modifier.fillMaxWidth(),
        color = com.example.vocablearningapp.ui.theme.SurfaceMuted,
        shape = RoundedCornerShape(14.dp),
        border = BorderStroke(1.dp, Border)
    ) {
        Column(modifier = Modifier.padding(12.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    PartOfSpeech.entries.forEach { pos ->
                        FilterChip(
                            selected = meaning.partOfSpeech == pos,
                            onClick = { onMeaningChange(meaning.copy(partOfSpeech = pos)) },
                            label = { Text(pos.label) }
                        )
                    }
                }
                if (canDeleteMeaning) {
                    IconButton(onClick = onDeleteMeaning) {
                        Icon(
                            imageVector = Icons.Default.Delete,
                            contentDescription = "Delete meaning",
                            tint = Muted,
                            modifier = Modifier.height(18.dp)
                        )
                    }
                }
            }

            OutlinedTextField(
                value = localMeaningText,
                onValueChange = { newText ->
                    localMeaningText = newText
                    onMeaningChange(meaning.copy(meaning = newText))
                },
                modifier = Modifier.fillMaxWidth(),
                label = { Text("Nghĩa ${if (canDeleteMeaning) "${index + 1}" else ""}") },
                placeholder = { Text("ví dụ: nhận thấy, chú ý...") },
                singleLine = true,
                shape = RoundedCornerShape(12.dp)
            )
        }
    }
}

@Composable
private fun IpaFieldWithHelper(
    pronunciation: String,
    word: String,
    onPronunciationChange: (String) -> Unit
) {
    Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            OutlinedTextField(
                value = pronunciation,
                onValueChange = onPronunciationChange,
                modifier = Modifier.weight(1f),
                label = { Text("IPA pronunciation") },
                placeholder = { Text("/æmˈbɪʃ.əs/") },
                singleLine = true,
                shape = RoundedCornerShape(14.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            androidx.compose.material3.Button(
                onClick = {
                    val generated = IpaGenerator.generateIpa(word)
                    if (generated.isNotBlank()) {
                        onPronunciationChange(generated)
                    }
                },
                shape = RoundedCornerShape(14.dp),
                colors = androidx.compose.material3.ButtonDefaults.buttonColors(
                    containerColor = com.example.vocablearningapp.ui.theme.AccentSoft,
                    contentColor = com.example.vocablearningapp.ui.theme.Accent
                ),
                contentPadding = androidx.compose.foundation.layout.PaddingValues(horizontal = 10.dp, vertical = 14.dp)
            ) {
                Text(text = "✨ Auto IPA", style = MaterialTheme.typography.labelMedium, fontWeight = FontWeight.Bold)
            }
        }

        Text(
            text = "Quick IPA symbols:",
            style = MaterialTheme.typography.labelSmall,
            color = Muted
        )
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState()),
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            IpaGenerator.commonIpaSymbols.forEach { symbol ->
                Surface(
                    onClick = {
                        val updated = if (pronunciation.endsWith("/")) {
                            pronunciation.dropLast(1) + symbol + "/"
                        } else {
                            pronunciation + symbol
                        }
                        onPronunciationChange(updated)
                    },
                    shape = RoundedCornerShape(8.dp),
                    color = Surface,
                    border = BorderStroke(1.dp, Border)
                ) {
                    Text(
                        text = symbol,
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                        style = MaterialTheme.typography.bodyMedium,
                        color = Ink,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }
        }
    }
}

private data class DraftMeaning(
    val id: String = "m-${System.nanoTime()}-${kotlin.random.Random.nextInt(10000)}",
    val partOfSpeech: PartOfSpeech = PartOfSpeech.NOUN,
    val meaning: String = ""
)

private data class DraftWord(
    val id: String = "draft-${System.nanoTime()}-${kotlin.random.Random.nextInt(10000)}",
    val word: String = "",
    val meanings: List<DraftMeaning> = listOf(DraftMeaning()),
    val pronunciation: String = "",
    val exampleSentence: String = "",
    val fsrsState: FsrsState = FsrsState.NEW,
    val fsrsStep: Int? = null,
    val stability: Double = 0.0,
    val difficulty: Double = 0.0,
    val dueAtMillis: Long = 0L,
    val lastReviewAtMillis: Long? = null,
    val scheduledDays: Int = 0,
    val reviewCount: Int = 0,
    val lapseCount: Int = 0
) {
    val primaryPartOfSpeech: PartOfSpeech
        get() = meanings.firstOrNull()?.partOfSpeech ?: PartOfSpeech.NOUN

    val formattedMeaning: String
        get() {
            val valid = meanings.filter { it.meaning.isNotBlank() }
            if (valid.isEmpty()) return ""
            if (valid.size == 1) return valid[0].meaning.trim()
            return valid.joinToString("; ") { "${it.partOfSpeech.label}. ${it.meaning.trim()}" }
        }

    fun toVocabularyItem(setTitle: String, index: Int): VocabularyItem? {
        val finalMeaning = formattedMeaning
        if (word.isBlank() && finalMeaning.isBlank()) return null
        return VocabularyItem(
            id = id.ifBlank { "${setTitle.lowercase().replace(" ", "-")}-$index" },
            word = word.trim(),
            meaning = finalMeaning,
            pronunciation = pronunciation.trim(),
            partOfSpeech = primaryPartOfSpeech,
            exampleSentence = exampleSentence.trim().ifBlank {
                "This is an example sentence for ${word.trim()}."
            },
            fsrsState = fsrsState,
            fsrsStep = fsrsStep,
            stability = stability,
            difficulty = difficulty,
            dueAtMillis = dueAtMillis,
            lastReviewAtMillis = lastReviewAtMillis,
            scheduledDays = scheduledDays,
            reviewCount = reviewCount,
            lapseCount = lapseCount
        )
    }

    companion object {
        fun from(item: VocabularyItem): DraftWord {
            val parsedEntries = com.example.vocablearningapp.domain.util.MeaningParser.parse(item.meaning, item.partOfSpeech)
            val draftMeanings = if (parsedEntries.isEmpty()) {
                listOf(DraftMeaning(partOfSpeech = item.partOfSpeech, meaning = item.meaning))
            } else {
                parsedEntries.map { entry ->
                    DraftMeaning(partOfSpeech = entry.partOfSpeech, meaning = entry.meaning)
                }
            }

            return DraftWord(
                id = item.id,
                word = item.word,
                meanings = draftMeanings,
                pronunciation = item.pronunciation,
                exampleSentence = item.exampleSentence,
                fsrsState = item.fsrsState,
                fsrsStep = item.fsrsStep,
                stability = item.stability,
                difficulty = item.difficulty,
                dueAtMillis = item.dueAtMillis,
                lastReviewAtMillis = item.lastReviewAtMillis,
                scheduledDays = item.scheduledDays,
                reviewCount = item.reviewCount,
                lapseCount = item.lapseCount
            )
        }
    }
}
