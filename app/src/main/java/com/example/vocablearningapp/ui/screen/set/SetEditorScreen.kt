package com.example.vocablearningapp.ui.screen.set

import androidx.compose.foundation.BorderStroke
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
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import com.example.vocablearningapp.data.DictionaryRepository
import com.example.vocablearningapp.data.DictionaryWord
import com.example.vocablearningapp.domain.model.FsrsState
import com.example.vocablearningapp.domain.model.PartOfSpeech
import com.example.vocablearningapp.domain.model.VocabularyItem
import com.example.vocablearningapp.domain.model.VocabularySet
import com.example.vocablearningapp.domain.util.IpaGenerator
import com.example.vocablearningapp.domain.util.MeaningParser
import com.example.vocablearningapp.ui.component.PrimaryButton
import com.example.vocablearningapp.ui.component.SecondaryButton
import com.example.vocablearningapp.ui.component.VocabDimens
import com.example.vocablearningapp.ui.component.VocabTopBar
import com.example.vocablearningapp.ui.theme.Accent
import com.example.vocablearningapp.ui.theme.AccentDark
import com.example.vocablearningapp.ui.theme.AccentSoft
import com.example.vocablearningapp.ui.theme.Border
import com.example.vocablearningapp.ui.theme.Canvas
import com.example.vocablearningapp.ui.theme.Ink
import com.example.vocablearningapp.ui.theme.Muted
import com.example.vocablearningapp.ui.theme.Surface
import com.example.vocablearningapp.ui.theme.SurfaceMuted
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow

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
        mutableStateListOf<DraftWordState>().apply {
            if (set == null || set.words.isEmpty()) {
                add(DraftWordState())
            } else {
                addAll(set.words.map { DraftWordState.from(it) })
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
                    TextButton(onClick = {
                        onSave(
                            title,
                            description,
                            level,
                            category,
                            draftWords.mapIndexedNotNull { index, draft -> draft.toVocabularyItem(title, index) }
                        )
                    }) {
                        Text(text = "Save", color = Accent)
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
                        placeholder = "General",
                        modifier = Modifier.weight(1f)
                    )
                }
            }

            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(text = "Words", style = MaterialTheme.typography.titleLarge, color = Ink)
                        Spacer(modifier = Modifier.height(3.dp))
                        Text(text = "Add a term, meanings and examples.", style = MaterialTheme.typography.bodyMedium, color = Muted)
                    }
                    Text(
                        text = "${draftWords.size}",
                        style = MaterialTheme.typography.titleLarge,
                        color = Accent
                    )
                }

                draftWords.forEachIndexed { index, draft ->
                    WordEditorCard(
                        index = index,
                        draft = draft,
                        canDelete = draftWords.size > 1,
                        onDelete = { draftWords.removeAt(index) }
                    )
                }

                SecondaryButton(
                    text = "+ Add word",
                    onClick = { draftWords.add(DraftWordState()) },
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
    var textFieldValue by remember {
        mutableStateOf(TextFieldValue(text = value, selection = TextRange(value.length)))
    }

    LaunchedEffect(value) {
        if (value != textFieldValue.text) {
            textFieldValue = TextFieldValue(text = value, selection = TextRange(value.length))
        }
    }

    OutlinedTextField(
        value = textFieldValue,
        onValueChange = { newValue ->
            textFieldValue = newValue
            onValueChange(newValue.text)
        },
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
    draft: DraftWordState,
    canDelete: Boolean,
    onDelete: () -> Unit
) {
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

            val suggestions = remember(draft.word) {
                if (draft.word.trim().length >= 1) {
                    DictionaryRepository.searchWords(draft.word, limit = 4)
                } else emptyList()
            }
            val hasExactMatch = suggestions.any { it.word.equals(draft.word.trim(), ignoreCase = true) && draft.meanings.any { m -> m.meaning.isNotBlank() } }

            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                EditorField(
                    value = draft.word,
                    onValueChange = { newWord ->
                        draft.word = newWord
                        if (draft.pronunciation.isBlank()) {
                            draft.pronunciation = IpaGenerator.generateIpa(newWord)
                        }
                    },
                    label = "Word",
                    placeholder = "e.g. key, money, commute..."
                )

                if (suggestions.isNotEmpty() && !hasExactMatch) {
                    Column(verticalArrangement = Arrangement.spacedBy(5.dp)) {
                        Text(
                            text = "💡 Dictionary Suggestions (tap to auto-fill):",
                            style = MaterialTheme.typography.labelSmall,
                            color = Accent,
                            fontWeight = FontWeight.Bold
                        )
                        suggestions.forEach { dictWord ->
                            Surface(
                                onClick = {
                                    draft.applyDictionaryWord(dictWord)
                                },
                                shape = RoundedCornerShape(10.dp),
                                color = AccentSoft,
                                border = BorderStroke(1.dp, Color(0xFFC8E6C9)),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Row(
                                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 7.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = dictWord.word,
                                        style = MaterialTheme.typography.labelLarge,
                                        color = AccentDark,
                                        fontWeight = FontWeight.Bold
                                    )
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text(
                                        text = dictWord.ipa,
                                        style = MaterialTheme.typography.bodySmall,
                                        color = Muted
                                    )
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text(
                                        text = "· ${dictWord.formattedMeaning}",
                                        style = MaterialTheme.typography.bodySmall,
                                        color = Ink,
                                        maxLines = 1,
                                        overflow = TextOverflow.Ellipsis,
                                        modifier = Modifier.weight(1f)
                                    )
                                }
                            }
                        }
                    }
                }
            }

            IpaFieldWithHelper(
                pronunciation = draft.pronunciation,
                word = draft.word,
                onPronunciationChange = { draft.pronunciation = it }
            )

            Text(
                text = "Meanings & Examples",
                style = MaterialTheme.typography.labelLarge,
                color = Ink,
                fontWeight = FontWeight.Bold
            )

            draft.meanings.forEachIndexed { mIndex, mDraft ->
                MeaningEditorRow(
                    index = mIndex,
                    meaningState = mDraft,
                    canDeleteMeaning = draft.meanings.size > 1,
                    onDeleteMeaning = { draft.meanings.removeAt(mIndex) }
                )
            }

            SecondaryButton(
                text = "+ Add another meaning",
                onClick = { draft.meanings.add(DraftMeaningState()) },
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

@Composable
private fun MeaningEditorRow(
    index: Int,
    meaningState: DraftMeaningState,
    canDeleteMeaning: Boolean,
    onDeleteMeaning: () -> Unit
) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        color = SurfaceMuted,
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
                            selected = meaningState.partOfSpeech == pos,
                            onClick = { meaningState.partOfSpeech = pos },
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

            EditorField(
                value = meaningState.meaning,
                onValueChange = { meaningState.meaning = it },
                label = "Meaning ${if (canDeleteMeaning) "${index + 1}" else ""}",
                placeholder = "e.g. notice, attention..."
            )

            EditorField(
                value = meaningState.exampleSentence,
                onValueChange = { meaningState.exampleSentence = it },
                label = "Example sentence",
                placeholder = "e.g. Did you notice his new haircut?"
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
            EditorField(
                value = pronunciation,
                onValueChange = onPronunciationChange,
                label = "IPA pronunciation",
                placeholder = "/æmˈbɪʃ.əs/",
                modifier = Modifier.weight(1f)
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
                    containerColor = AccentSoft,
                    contentColor = Accent
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

private class DraftMeaningState(
    val id: String = "m-${System.nanoTime()}-${kotlin.random.Random.nextInt(10000)}",
    initialPos: PartOfSpeech = PartOfSpeech.NOUN,
    initialMeaning: String = "",
    initialExample: String = ""
) {
    var partOfSpeech by mutableStateOf(initialPos)
    var meaning by mutableStateOf(initialMeaning)
    var exampleSentence by mutableStateOf(initialExample)
}

private class DraftWordState(
    val id: String = "draft-${System.nanoTime()}-${kotlin.random.Random.nextInt(10000)}",
    initialWord: String = "",
    initialPronunciation: String = "",
    initialMeanings: List<DraftMeaningState> = emptyList(),
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
    var word by mutableStateOf(initialWord)
    var pronunciation by mutableStateOf(initialPronunciation)
    val meanings = mutableStateListOf<DraftMeaningState>().apply {
        if (initialMeanings.isEmpty()) add(DraftMeaningState()) else addAll(initialMeanings)
    }

    val primaryPartOfSpeech: PartOfSpeech
        get() = meanings.firstOrNull()?.partOfSpeech ?: PartOfSpeech.NOUN

    fun applyDictionaryWord(dictWord: DictionaryWord) {
        word = dictWord.word
        pronunciation = dictWord.ipa
        meanings.clear()
        if (dictWord.meanings.isEmpty()) {
            meanings.add(DraftMeaningState())
        } else {
            dictWord.meanings.forEach { m ->
                meanings.add(
                    DraftMeaningState(
                        initialPos = m.partOfSpeech,
                        initialMeaning = m.meaning,
                        initialExample = m.exampleSentence
                    )
                )
            }
        }
    }

    val formattedMeaning: String
        get() {
            val valid = meanings.filter { it.meaning.isNotBlank() }
            if (valid.isEmpty()) return ""
            if (valid.size == 1) return valid[0].meaning.trim()
            return valid.joinToString("; ") { "${it.partOfSpeech.label}. ${it.meaning.trim()}" }
        }

    val formattedExample: String
        get() {
            val valid = meanings.filter { it.exampleSentence.isNotBlank() }
            if (valid.isEmpty()) return ""
            if (valid.size == 1) return valid[0].exampleSentence.trim()
            return valid.joinToString(" | ") { "${it.partOfSpeech.label}. ${it.exampleSentence.trim()}" }
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
            exampleSentence = formattedExample.ifBlank {
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
        fun from(item: VocabularyItem): DraftWordState {
            val parsedEntries = MeaningParser.parse(
                rawMeaning = item.meaning,
                rawExample = item.exampleSentence,
                fallbackPos = item.partOfSpeech
            )
            val draftMeanings = if (parsedEntries.isEmpty()) {
                listOf(DraftMeaningState(initialPos = item.partOfSpeech, initialMeaning = item.meaning, initialExample = item.exampleSentence))
            } else {
                parsedEntries.map { entry ->
                    DraftMeaningState(initialPos = entry.partOfSpeech, initialMeaning = entry.meaning, initialExample = entry.exampleSentence)
                }
            }

            return DraftWordState(
                id = item.id,
                initialWord = item.word,
                initialPronunciation = item.pronunciation,
                initialMeanings = draftMeanings,
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
