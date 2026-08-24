package com.example.vocablearningapp.ui.screen.ai

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Chat
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.vocablearningapp.data.ai.AiActionPayload
import com.example.vocablearningapp.data.ai.AiChatMessage
import com.example.vocablearningapp.data.ai.AiGeneratedSet
import com.example.vocablearningapp.data.ai.AiGeneratedWord
import com.example.vocablearningapp.data.ai.AiPreferences
import com.example.vocablearningapp.data.ai.GeminiApiClient
import com.example.vocablearningapp.data.ai.MessageSender
import com.example.vocablearningapp.domain.model.VocabularySet
import com.example.vocablearningapp.ui.component.PrimaryButton
import com.example.vocablearningapp.ui.component.SpeechButton
import com.example.vocablearningapp.ui.component.VocabDimens
import com.example.vocablearningapp.ui.component.VocabTopBar
import com.example.vocablearningapp.ui.theme.Accent
import com.example.vocablearningapp.ui.theme.AccentDark
import com.example.vocablearningapp.ui.theme.AccentSoft
import com.example.vocablearningapp.ui.theme.Border
import com.example.vocablearningapp.ui.theme.Canvas
import com.example.vocablearningapp.ui.theme.Ink
import com.example.vocablearningapp.ui.theme.Mastered
import com.example.vocablearningapp.ui.theme.MasteredSoft
import com.example.vocablearningapp.ui.theme.Muted
import com.example.vocablearningapp.ui.theme.Surface
import com.example.vocablearningapp.ui.theme.SurfaceMuted
import androidx.compose.material.icons.filled.Key
import kotlinx.coroutines.launch

@Composable
fun AiChatScreen(
    initialTab: Int = 0,
    onBack: () -> Unit,
    onSaveSet: (VocabularySet) -> Unit,
    onOpenSet: (String) -> Unit
) {
    val context = LocalContext.current
    val aiPreferences = remember { AiPreferences(context) }
    val geminiClient = remember { GeminiApiClient(aiPreferences) }

    var selectedTab by remember { mutableIntStateOf(initialTab.coerceIn(0, 1)) }
    var showApiKeyDialog by remember { mutableStateOf(false) }
    var showDiscardWarningDialog by remember { mutableStateOf(false) }
    var apiKeyInput by remember { mutableStateOf(aiPreferences.apiKey) }
    var selectedModel by remember { mutableStateOf(aiPreferences.model) }
    var hasLiveKey by remember { mutableStateOf(aiPreferences.hasApiKey) }

    fun handleDismissSettings() {
        val hasChanges = apiKeyInput.trim() != aiPreferences.apiKey.trim() || selectedModel != aiPreferences.model
        if (hasChanges) {
            showDiscardWarningDialog = true
        } else {
            showApiKeyDialog = false
        }
    }

    Scaffold(
        containerColor = Canvas,
        topBar = {
            Column {
                VocabTopBar(
                    title = "AI Tutor",
                    onBack = onBack,
                    actions = {
                        Surface(
                            color = if (hasLiveKey) AccentSoft else SurfaceMuted,
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier.padding(end = 4.dp)
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.AutoAwesome,
                                    contentDescription = null,
                                    tint = if (hasLiveKey) AccentDark else Muted,
                                    modifier = Modifier.size(14.dp)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = if (hasLiveKey) "Gemini Live" else "Demo Mock",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = if (hasLiveKey) AccentDark else Muted,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }

                        IconButton(onClick = {
                            apiKeyInput = aiPreferences.apiKey
                            selectedModel = aiPreferences.model
                            showApiKeyDialog = true
                        }) {
                            Icon(
                                imageVector = Icons.Default.Key,
                                contentDescription = "API Key Settings",
                                tint = Ink
                            )
                        }
                    }
                )

                // Top Tab Switcher: Chat Tutor & Set Generator
                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    color = Surface,
                    border = BorderStroke(1.dp, Border)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 6.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Surface(
                            onClick = { selectedTab = 0 },
                            shape = RoundedCornerShape(12.dp),
                            color = if (selectedTab == 0) AccentSoft else Canvas,
                            border = BorderStroke(1.dp, if (selectedTab == 0) Accent else Border),
                            modifier = Modifier.weight(1f)
                        ) {
                            Row(
                                modifier = Modifier.padding(vertical = 10.dp),
                                horizontalArrangement = Arrangement.Center,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = Icons.AutoMirrored.Filled.Chat,
                                    contentDescription = null,
                                    tint = if (selectedTab == 0) AccentDark else Muted,
                                    modifier = Modifier.size(16.dp)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = "Chat Tutor",
                                    style = MaterialTheme.typography.labelLarge,
                                    fontWeight = if (selectedTab == 0) FontWeight.Bold else FontWeight.Medium,
                                    color = if (selectedTab == 0) AccentDark else Ink
                                )
                            }
                        }

                        Surface(
                            onClick = { selectedTab = 1 },
                            shape = RoundedCornerShape(12.dp),
                            color = if (selectedTab == 1) AccentSoft else Canvas,
                            border = BorderStroke(1.dp, if (selectedTab == 1) Accent else Border),
                            modifier = Modifier.weight(1f)
                        ) {
                            Row(
                                modifier = Modifier.padding(vertical = 10.dp),
                                horizontalArrangement = Arrangement.Center,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Bolt,
                                    contentDescription = null,
                                    tint = if (selectedTab == 1) AccentDark else Muted,
                                    modifier = Modifier.size(16.dp)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = "Create Study Set",
                                    style = MaterialTheme.typography.labelLarge,
                                    fontWeight = if (selectedTab == 1) FontWeight.Bold else FontWeight.Medium,
                                    color = if (selectedTab == 1) AccentDark else Ink
                                )
                            }
                        }
                    }
                }
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            when (selectedTab) {
                0 -> AiChatTab(
                    geminiClient = geminiClient,
                    onSaveSet = onSaveSet,
                    onOpenSet = onOpenSet
                )
                1 -> AiSetGeneratorTab(
                    geminiClient = geminiClient,
                    onSaveSet = onSaveSet,
                    onOpenSet = onOpenSet
                )
            }
        }
    }

    // API Key Settings Dialog
    if (showApiKeyDialog) {
        AlertDialog(
            onDismissRequest = { handleDismissSettings() },
            title = {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Key, contentDescription = null, tint = Accent)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("AI Tutor Settings")
                }
            },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Text(
                        text = "Enter your Google Gemini API key to enable live AI responses. Leave blank to use pre-compiled offline Demo Mock mode.",
                        style = MaterialTheme.typography.bodySmall,
                        color = Muted
                    )

                    OutlinedTextField(
                        value = apiKeyInput,
                        onValueChange = { apiKeyInput = it },
                        label = { Text("Gemini API Key") },
                        placeholder = { Text("••••••••••••••••••••") },
                        singleLine = true,
                        visualTransformation = PasswordVisualTransformation(),
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Password,
                            imeAction = ImeAction.Done
                        ),
                        modifier = Modifier.fillMaxWidth()
                    )

                    Text(
                        text = "Model:",
                        style = MaterialTheme.typography.labelMedium,
                        fontWeight = FontWeight.Bold,
                        color = Ink
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        listOf("gemini-2.5-flash", "gemini-2.0-flash", "gemini-1.5-flash-latest").forEach { model ->
                            val isSelected = selectedModel == model
                            Surface(
                                onClick = { selectedModel = model },
                                shape = RoundedCornerShape(8.dp),
                                color = if (isSelected) AccentSoft else SurfaceMuted,
                                border = BorderStroke(1.dp, if (isSelected) Accent else Border),
                                modifier = Modifier.weight(1f)
                            ) {
                                Text(
                                    text = model.removePrefix("gemini-"),
                                    style = MaterialTheme.typography.labelSmall,
                                    color = if (isSelected) AccentDark else Ink,
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                    modifier = Modifier.padding(vertical = 6.dp, horizontal = 4.dp),
                                    textAlign = androidx.compose.ui.text.style.TextAlign.Center
                                )
                            }
                        }
                    }

                    Text(
                        text = "💡 Get a free API key at: aistudio.google.com",
                        style = MaterialTheme.typography.labelSmall,
                        color = AccentDark,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            },
            confirmButton = {
                PrimaryButton(
                    text = "Save",
                    onClick = {
                        aiPreferences.apiKey = apiKeyInput.trim()
                        aiPreferences.model = selectedModel
                        hasLiveKey = aiPreferences.hasApiKey
                        showApiKeyDialog = false
                    }
                )
            },
            dismissButton = {
                TextButton(onClick = { handleDismissSettings() }) {
                    Text("Cancel")
                }
            }
        )
    }

    // Warning Dialog when discarding unsaved API key changes
    if (showDiscardWarningDialog) {
        AlertDialog(
            onDismissRequest = { showDiscardWarningDialog = false },
            title = {
                Text(
                    text = "Discard Unsaved Changes?",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
            },
            text = {
                Text(
                    text = "You have unsaved changes to your API key or model settings. Do you want to discard them without saving?",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Muted
                )
            },
            confirmButton = {
                PrimaryButton(
                    text = "Discard",
                    onClick = {
                        apiKeyInput = aiPreferences.apiKey
                        selectedModel = aiPreferences.model
                        showDiscardWarningDialog = false
                        showApiKeyDialog = false
                    }
                )
            },
            dismissButton = {
                TextButton(onClick = { showDiscardWarningDialog = false }) {
                    Text("Keep Editing")
                }
            }
        )
    }
}

// -------------------------------------------------------------
// TAB 1: AI CHAT TUTOR
// -------------------------------------------------------------
@Composable
private fun AiChatTab(
    geminiClient: GeminiApiClient,
    onSaveSet: (VocabularySet) -> Unit,
    onOpenSet: (String) -> Unit
) {
    val coroutineScope = rememberCoroutineScope()
    val messages = remember {
        mutableStateListOf(
            AiChatMessage(
                sender = MessageSender.AI,
                text = "Hello! I am your **AI Tutor** ✨.\n\nI can help answer any English grammar questions, explain confusing vocabulary, practice conversations, or assist with learning strategies. Feel free to ask me anything!"
            )
        )
    }

    var inputText by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }
    val listState = rememberLazyListState()

    val quickQuestions = listOf(
        "🔍 Explain affect vs effect",
        "💡 Tips for IELTS Speaking",
        "📝 Say vs Tell vs Speak vs Talk",
        "🧠 How FSRS spaced repetition works",
        "☕ Practice ordering coffee dialogue",
        "💼 Professional email closing phrases"
    )

    fun sendUserMessage(text: String) {
        val prompt = text.trim()
        if (prompt.isBlank() || isLoading) return

        messages.add(AiChatMessage(sender = MessageSender.USER, text = prompt))
        inputText = ""
        isLoading = true

        coroutineScope.launch {
            listState.animateScrollToItem(messages.size - 1)
            val result = geminiClient.sendMessage(prompt, messages)
            result.onSuccess { aiMsg ->
                messages.add(aiMsg)
                listState.animateScrollToItem(messages.size - 1)
            }.onFailure { err ->
                messages.add(
                    AiChatMessage(
                        sender = MessageSender.AI,
                        text = "Sorry, I encountered an error: ${err.localizedMessage}. Please check your API key or network connection."
                    )
                )
            }
            isLoading = false
        }
    }

    Column(modifier = Modifier.fillMaxSize()) {
        // Quick Questions Row
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState())
                .padding(horizontal = 16.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            quickQuestions.forEach { prompt ->
                Surface(
                    onClick = { sendUserMessage(prompt) },
                    shape = RoundedCornerShape(20.dp),
                    color = Surface,
                    border = BorderStroke(1.dp, Border)
                ) {
                    Text(
                        text = prompt,
                        style = MaterialTheme.typography.bodySmall,
                        color = Ink,
                        fontWeight = FontWeight.Medium,
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                    )
                }
            }
        }

        // Message List
        LazyColumn(
            state = listState,
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            items(messages, key = { it.id }) { message ->
                ChatMessageItem(
                    message = message,
                    onSaveSet = { set ->
                        val vocabSet = set.toVocabularySet()
                        onSaveSet(vocabSet)
                        (message.actionPayload as? AiActionPayload.CreateSetPayload)?.isSaved = true
                    },
                    onOpenSavedSet = { set ->
                        val vocabSet = set.toVocabularySet()
                        onOpenSet(vocabSet.id)
                    }
                )
            }

            if (isLoading) {
                item {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Surface(
                            shape = CircleShape,
                            color = AccentSoft,
                            modifier = Modifier.size(32.dp)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Icon(
                                    Icons.Default.AutoAwesome,
                                    contentDescription = null,
                                    tint = Accent,
                                    modifier = Modifier.size(18.dp)
                                )
                            }
                        }
                        CircularProgressIndicator(
                            modifier = Modifier.size(20.dp),
                            strokeWidth = 2.dp,
                            color = Accent
                        )
                        Text(
                            text = "AI Tutor is thinking...",
                            style = MaterialTheme.typography.bodySmall,
                            color = Muted,
                            fontStyle = FontStyle.Italic
                        )
                    }
                }
            }
        }

        // Bottom Input Bar
        Surface(
            modifier = Modifier.fillMaxWidth(),
            color = Surface,
            tonalElevation = 3.dp,
            border = BorderStroke(1.dp, Border)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                OutlinedTextField(
                    value = inputText,
                    onValueChange = { inputText = it },
                    placeholder = { Text("Ask AI Tutor any English question...") },
                    modifier = Modifier.weight(1f),
                    singleLine = false,
                    maxLines = 3,
                    shape = RoundedCornerShape(20.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Accent,
                        unfocusedBorderColor = Border
                    ),
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Send),
                    keyboardActions = KeyboardActions(onSend = { sendUserMessage(inputText) })
                )

                IconButton(
                    onClick = { sendUserMessage(inputText) },
                    enabled = inputText.isNotBlank() && !isLoading,
                    modifier = Modifier
                        .background(if (inputText.isNotBlank() && !isLoading) Accent else SurfaceMuted, CircleShape)
                        .size(44.dp)
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.Send,
                        contentDescription = "Send",
                        tint = if (inputText.isNotBlank() && !isLoading) Surface else Muted,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }
        }
    }
}

// -------------------------------------------------------------
// TAB 2: AI STUDY SET GENERATOR STUDIO
// -------------------------------------------------------------
@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun AiSetGeneratorTab(
    geminiClient: GeminiApiClient,
    onSaveSet: (VocabularySet) -> Unit,
    onOpenSet: (String) -> Unit
) {
    val coroutineScope = rememberCoroutineScope()

    var topicInput by remember { mutableStateOf("Job Interview") }
    var selectedLevel by remember { mutableStateOf("B2") }
    var selectedWordCountPreset by remember { mutableStateOf("10") }
    var customWordCountInput by remember { mutableStateOf("10") }
    var selectedCategory by remember { mutableStateOf("Work") }
    var selectedFocus by remember { mutableStateOf("Mixed") }
    var customInstructions by remember { mutableStateOf("") }

    var isGenerating by remember { mutableStateOf(false) }
    var generatedSet by remember { mutableStateOf<AiGeneratedSet?>(null) }
    var isSavedLocally by remember { mutableStateOf(false) }
    var savedSetId by remember { mutableStateOf<String?>(null) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    val quickTopics = listOf(
        "💼 Job Interview",
        "✈️ Travel & Airport",
        "💻 Tech & Programming",
        "🎓 IELTS Academic",
        "☕ Daily Conversation",
        "🏥 Health & Hospital",
        "🛒 Shopping & Retail",
        "🍽️ Food & Dining",
        "🏨 Hotel & Hospitality",
        "🌍 Environment & Climate"
    )

    val levels = listOf("A1", "A2", "B1", "B2", "C1", "C2")
    val wordCountPresets = listOf("5", "10", "15", "20", "Custom")
    val categories = listOf("General", "Work", "Travel", "Academic", "Interests", "Education", "Society", "Everyday")
    val focusOptions = listOf("Mixed", "Nouns", "Action Verbs", "Adjectives", "Phrasal Verbs & Idioms", "Collocations")

    fun triggerGenerate() {
        val topic = topicInput.trim()
        if (topic.isBlank() || isGenerating) return

        val count = if (selectedWordCountPreset == "Custom") {
            customWordCountInput.toIntOrNull()?.coerceIn(3, 30) ?: 10
        } else {
            selectedWordCountPreset.toIntOrNull() ?: 10
        }

        isGenerating = true
        errorMessage = null
        isSavedLocally = false
        savedSetId = null

        coroutineScope.launch {
            val result = geminiClient.generateVocabularySet(
                topic = topic,
                level = selectedLevel,
                wordCount = count,
                category = selectedCategory,
                focus = selectedFocus,
                customInstructions = customInstructions.trim()
            )

            result.onSuccess { set ->
                generatedSet = set
            }.onFailure { err ->
                errorMessage = err.localizedMessage ?: "Failed to generate set"
            }
            isGenerating = false
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Banner Header
        Surface(
            shape = RoundedCornerShape(16.dp),
            color = AccentDark,
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.AutoAwesome,
                        contentDescription = null,
                        tint = AccentSoft,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "AI Study Set Studio",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = Canvas
                    )
                }
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Design tailored vocabulary decks with authentic examples, CEFR calibration, and 1-tap library saving.",
                    style = MaterialTheme.typography.bodySmall,
                    color = AccentSoft
                )
            }
        }

        // Section 1: Topic Input & Quick Suggestions
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = Surface),
            border = BorderStroke(1.dp, Border)
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Text(
                    text = "1. Topic or Theme",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold,
                    color = Ink
                )

                OutlinedTextField(
                    value = topicInput,
                    onValueChange = { topicInput = it },
                    placeholder = { Text("e.g. Artificial Intelligence, Coffee shop, Medical...") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Accent,
                        unfocusedBorderColor = Border
                    ),
                    trailingIcon = {
                        if (topicInput.isNotBlank()) {
                            IconButton(onClick = { topicInput = "" }) {
                                Icon(Icons.Default.Close, contentDescription = "Clear", tint = Muted)
                            }
                        }
                    }
                )

                Text(
                    text = "Quick Topic Suggestions:",
                    style = MaterialTheme.typography.labelSmall,
                    color = Muted
                )

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .horizontalScroll(rememberScrollState()),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    quickTopics.forEach { topicLabel ->
                        val cleanName = topicLabel.substringAfter(" ")
                        val isSelected = topicInput.equals(cleanName, ignoreCase = true)
                        Surface(
                            onClick = {
                                topicInput = cleanName
                                if (cleanName.contains("Interview") || cleanName.contains("Work")) selectedCategory = "Work"
                                if (cleanName.contains("Travel") || cleanName.contains("Airport") || cleanName.contains("Hotel")) selectedCategory = "Travel"
                                if (cleanName.contains("Academic") || cleanName.contains("IELTS")) selectedCategory = "Academic"
                            },
                            shape = RoundedCornerShape(16.dp),
                            color = if (isSelected) AccentSoft else Canvas,
                            border = BorderStroke(1.dp, if (isSelected) Accent else Border)
                        ) {
                            Text(
                                text = topicLabel,
                                style = MaterialTheme.typography.bodySmall,
                                color = if (isSelected) AccentDark else Ink,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                            )
                        }
                    }
                }
            }
        }

        // Section 2: CEFR Level & Word Count
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = Surface),
            border = BorderStroke(1.dp, Border)
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // CEFR Level
                Text(
                    text = "2. Target CEFR Level",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold,
                    color = Ink
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    levels.forEach { lvl ->
                        val isSelected = selectedLevel == lvl
                        Surface(
                            onClick = { selectedLevel = lvl },
                            shape = RoundedCornerShape(10.dp),
                            color = if (isSelected) AccentSoft else Canvas,
                            border = BorderStroke(1.dp, if (isSelected) Accent else Border),
                            modifier = Modifier.weight(1f)
                        ) {
                            Column(
                                modifier = Modifier.padding(vertical = 8.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text(
                                    text = lvl,
                                    style = MaterialTheme.typography.labelLarge,
                                    fontWeight = FontWeight.Bold,
                                    color = if (isSelected) AccentDark else Ink
                                )
                                Text(
                                    text = when (lvl) {
                                        "A1" -> "Beg"
                                        "A2" -> "Elem"
                                        "B1" -> "Int"
                                        "B2" -> "Upper"
                                        "C1" -> "Adv"
                                        else -> "Mast"
                                    },
                                    style = MaterialTheme.typography.labelSmall,
                                    color = if (isSelected) AccentDark else Muted,
                                    fontSize = 9.sp
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(2.dp))

                // Word Count
                Text(
                    text = "3. Number of Words",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold,
                    color = Ink
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    wordCountPresets.forEach { countPreset ->
                        val isSelected = selectedWordCountPreset == countPreset
                        Surface(
                            onClick = { selectedWordCountPreset = countPreset },
                            shape = RoundedCornerShape(10.dp),
                            color = if (isSelected) AccentSoft else Canvas,
                            border = BorderStroke(1.dp, if (isSelected) Accent else Border),
                            modifier = Modifier.weight(1f)
                        ) {
                            Text(
                                text = countPreset,
                                style = MaterialTheme.typography.labelMedium,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                color = if (isSelected) AccentDark else Ink,
                                modifier = Modifier.padding(vertical = 8.dp),
                                textAlign = androidx.compose.ui.text.style.TextAlign.Center
                            )
                        }
                    }
                }

                if (selectedWordCountPreset == "Custom") {
                    OutlinedTextField(
                        value = customWordCountInput,
                        onValueChange = { customWordCountInput = it },
                        label = { Text("Exact Word Count (3 - 30)") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(10.dp)
                    )
                }
            }
        }

        // Section 3: Category & Focus Nuance
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = Surface),
            border = BorderStroke(1.dp, Border)
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // Category
                Text(
                    text = "4. Category",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold,
                    color = Ink
                )

                FlowRow(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    verticalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    categories.forEach { cat ->
                        val isSelected = selectedCategory == cat
                        Surface(
                            onClick = { selectedCategory = cat },
                            shape = RoundedCornerShape(14.dp),
                            color = if (isSelected) AccentSoft else Canvas,
                            border = BorderStroke(1.dp, if (isSelected) Accent else Border)
                        ) {
                            Text(
                                text = cat,
                                style = MaterialTheme.typography.labelMedium,
                                color = if (isSelected) AccentDark else Ink,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(2.dp))

                // Focus / Word Nuance
                Text(
                    text = "5. Linguistic Focus (Optional)",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold,
                    color = Ink
                )

                FlowRow(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    verticalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    focusOptions.forEach { f ->
                        val isSelected = selectedFocus == f
                        Surface(
                            onClick = { selectedFocus = f },
                            shape = RoundedCornerShape(14.dp),
                            color = if (isSelected) AccentSoft else Canvas,
                            border = BorderStroke(1.dp, if (isSelected) Accent else Border)
                        ) {
                            Text(
                                text = f,
                                style = MaterialTheme.typography.labelMedium,
                                color = if (isSelected) AccentDark else Ink,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(2.dp))

                // Custom User Prompt
                Text(
                    text = "6. Custom Instructions (Optional)",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold,
                    color = Ink
                )

                OutlinedTextField(
                    value = customInstructions,
                    onValueChange = { customInstructions = it },
                    placeholder = { Text("e.g. Focus on formal expressions for IELTS writing task 2...") },
                    modifier = Modifier.fillMaxWidth(),
                    maxLines = 3,
                    shape = RoundedCornerShape(10.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Accent,
                        unfocusedBorderColor = Border
                    )
                )
            }
        }

        // Generate Action Button
        PrimaryButton(
            text = if (isGenerating) "Generating with AI Tutor..." else "✨ Generate Study Set",
            onClick = { triggerGenerate() },
            modifier = Modifier.fillMaxWidth(),
            enabled = topicInput.isNotBlank() && !isGenerating
        )

        if (isGenerating) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 12.dp),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                CircularProgressIndicator(
                    modifier = Modifier.size(24.dp),
                    strokeWidth = 2.dp,
                    color = Accent
                )
                Spacer(modifier = Modifier.width(10.dp))
                Text(
                    text = "AI Tutor is crafting your custom vocabulary deck...",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Muted,
                    fontStyle = FontStyle.Italic
                )
            }
        }

        if (errorMessage != null) {
            Surface(
                color = com.example.vocablearningapp.ui.theme.ForgotSoft,
                shape = RoundedCornerShape(12.dp),
                border = BorderStroke(1.dp, com.example.vocablearningapp.ui.theme.Forgot),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = "⚠️ $errorMessage",
                    style = MaterialTheme.typography.bodySmall,
                    color = com.example.vocablearningapp.ui.theme.Forgot,
                    modifier = Modifier.padding(12.dp)
                )
            }
        }

        // Generated Set Preview Card
        generatedSet?.let { set ->
            var isWordListExpanded by remember { mutableStateOf(true) }

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = Surface),
                border = BorderStroke(1.5.dp, if (isSavedLocally) Mastered else Accent)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(18.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    // Header of Generated Set
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.Top
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = set.title,
                                style = MaterialTheme.typography.titleLarge,
                                fontWeight = FontWeight.Bold,
                                color = Ink
                            )
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                text = "${set.words.size} words · ${set.category}",
                                style = MaterialTheme.typography.bodyMedium,
                                color = Muted
                            )
                        }

                        Surface(
                            color = AccentSoft,
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Text(
                                text = set.level,
                                style = MaterialTheme.typography.labelMedium,
                                fontWeight = FontWeight.Bold,
                                color = AccentDark,
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp)
                            )
                        }
                    }

                    Text(
                        text = set.description,
                        style = MaterialTheme.typography.bodyMedium,
                        color = Ink
                    )

                    // Word List Header
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Words in Set (${set.words.size})",
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.Bold,
                            color = Ink
                        )
                        TextButton(onClick = { isWordListExpanded = !isWordListExpanded }) {
                            Text(
                                text = if (isWordListExpanded) "Collapse" else "Expand all",
                                color = Accent
                            )
                            Icon(
                                imageVector = if (isWordListExpanded) Icons.Default.ExpandLess else Icons.Default.ExpandMore,
                                contentDescription = null,
                                tint = Accent,
                                modifier = Modifier.size(18.dp)
                            )
                        }
                    }

                    // Expandable Words
                    AnimatedVisibility(visible = isWordListExpanded) {
                        Column(
                            modifier = Modifier.fillMaxWidth(),
                            verticalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            set.words.forEachIndexed { idx, wordItem ->
                                Surface(
                                    shape = RoundedCornerShape(12.dp),
                                    color = Canvas,
                                    border = BorderStroke(1.dp, Border),
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Column(modifier = Modifier.padding(12.dp)) {
                                        Row(
                                            modifier = Modifier.fillMaxWidth(),
                                            horizontalArrangement = Arrangement.SpaceBetween,
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            Row(
                                                verticalAlignment = Alignment.CenterVertically,
                                                modifier = Modifier.weight(1f)
                                            ) {
                                                Text(
                                                    text = "${idx + 1}. ${wordItem.word}",
                                                    style = MaterialTheme.typography.titleSmall,
                                                    fontWeight = FontWeight.Bold,
                                                    color = Ink
                                                )
                                                Spacer(modifier = Modifier.width(6.dp))
                                                Text(
                                                    text = wordItem.ipa,
                                                    style = MaterialTheme.typography.bodySmall,
                                                    color = Muted
                                                )
                                            }

                                            Row(verticalAlignment = Alignment.CenterVertically) {
                                                SpeechButton(
                                                    text = wordItem.word,
                                                    contentDescription = "Speak ${wordItem.word}",
                                                    modifier = Modifier.size(32.dp)
                                                )
                                                Spacer(modifier = Modifier.width(4.dp))
                                                Surface(
                                                    color = AccentSoft,
                                                    shape = RoundedCornerShape(6.dp)
                                                ) {
                                                    Text(
                                                        text = wordItem.partOfSpeech.label,
                                                        style = MaterialTheme.typography.labelSmall,
                                                        color = AccentDark,
                                                        fontWeight = FontWeight.Bold,
                                                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                                    )
                                                }
                                            }
                                        }

                                        Spacer(modifier = Modifier.height(4.dp))
                                        Text(
                                            text = wordItem.meaning,
                                            style = MaterialTheme.typography.bodyMedium,
                                            fontWeight = FontWeight.Medium,
                                            color = Ink
                                        )

                                        if (wordItem.exampleSentence.isNotBlank()) {
                                            Spacer(modifier = Modifier.height(4.dp))
                                            Text(
                                                text = "“${wordItem.exampleSentence}”",
                                                style = MaterialTheme.typography.bodySmall.copy(fontStyle = FontStyle.Italic),
                                                color = Muted
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(6.dp))

                    // Action Buttons (Save / Open)
                    if (!isSavedLocally) {
                        PrimaryButton(
                            text = "💾 Save to My Sets",
                            onClick = {
                                val vocabSet = set.toVocabularySet()
                                onSaveSet(vocabSet)
                                isSavedLocally = true
                                savedSetId = vocabSet.id
                            },
                            modifier = Modifier.fillMaxWidth()
                        )
                    } else {
                        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                            Surface(
                                color = MasteredSoft,
                                shape = RoundedCornerShape(14.dp),
                                border = BorderStroke(1.dp, Mastered),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Row(
                                    modifier = Modifier.padding(vertical = 12.dp, horizontal = 12.dp),
                                    horizontalArrangement = Arrangement.Center,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(
                                        Icons.Default.Check,
                                        contentDescription = null,
                                        tint = Mastered,
                                        modifier = Modifier.size(20.dp)
                                    )
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text(
                                        text = "Saved in Your Library!",
                                        style = MaterialTheme.typography.labelLarge,
                                        fontWeight = FontWeight.Bold,
                                        color = Mastered
                                    )
                                }
                            }

                            savedSetId?.let { sId ->
                                PrimaryButton(
                                    text = "▶ Study This Set Now",
                                    onClick = { onOpenSet(sId) },
                                    modifier = Modifier.fillMaxWidth()
                                )
                            }
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))
    }
}

// -------------------------------------------------------------
// CHAT MESSAGE BUBBLE ITEM
// -------------------------------------------------------------
@Composable
private fun ChatMessageItem(
    message: AiChatMessage,
    onSaveSet: (AiGeneratedSet) -> Unit,
    onOpenSavedSet: (AiGeneratedSet) -> Unit
) {
    val isUser = message.sender == MessageSender.USER

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = if (isUser) Arrangement.End else Arrangement.Start
    ) {
        if (!isUser) {
            Surface(
                shape = CircleShape,
                color = AccentSoft,
                modifier = Modifier
                    .padding(top = 4.dp, end = 8.dp)
                    .size(30.dp)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(
                        imageVector = Icons.Default.AutoAwesome,
                        contentDescription = "AI",
                        tint = Accent,
                        modifier = Modifier.size(16.dp)
                    )
                }
            }
        }

        Column(
            modifier = Modifier.fillMaxWidth(if (isUser) 0.85f else 0.9f),
            horizontalAlignment = if (isUser) Alignment.End else Alignment.Start
        ) {
            // Text Bubble
            Surface(
                shape = RoundedCornerShape(
                    topStart = 18.dp,
                    topEnd = 18.dp,
                    bottomStart = if (isUser) 18.dp else 4.dp,
                    bottomEnd = if (isUser) 4.dp else 18.dp
                ),
                color = if (isUser) Accent else Surface,
                border = if (isUser) null else BorderStroke(1.dp, Border),
                shadowElevation = 1.dp
            ) {
                Text(
                    text = message.text,
                    style = MaterialTheme.typography.bodyMedium.copy(lineHeight = 22.sp),
                    color = if (isUser) Surface else Ink,
                    modifier = Modifier.padding(14.dp)
                )
            }

            // Embedded Action Card (if AI generated a Vocabulary Set during chat)
            if (message.actionPayload is AiActionPayload.CreateSetPayload) {
                val payload = message.actionPayload
                val generatedSet = payload.generatedSet
                var isExpanded by remember { mutableStateOf(false) }
                var savedLocally by remember { mutableStateOf(payload.isSaved) }

                Spacer(modifier = Modifier.height(8.dp))

                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = SurfaceMuted),
                    border = BorderStroke(1.dp, if (savedLocally) Mastered else Accent)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(14.dp),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = generatedSet.title,
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold,
                                    color = Ink
                                )
                                Text(
                                    text = "${generatedSet.words.size} words · ${generatedSet.category}",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = Muted
                                )
                            }

                            Surface(
                                color = AccentSoft,
                                shape = RoundedCornerShape(8.dp)
                            ) {
                                Text(
                                    text = generatedSet.level,
                                    style = MaterialTheme.typography.labelSmall,
                                    fontWeight = FontWeight.Bold,
                                    color = AccentDark,
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                )
                            }
                        }

                        // Toggle Preview
                        TextButton(
                            onClick = { isExpanded = !isExpanded },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = if (isExpanded) "Hide word list" else "Preview ${generatedSet.words.size} words",
                                    style = MaterialTheme.typography.labelMedium,
                                    color = Accent
                                )
                                Icon(
                                    imageVector = if (isExpanded) Icons.Default.ExpandLess else Icons.Default.ExpandMore,
                                    contentDescription = null,
                                    tint = Accent,
                                    modifier = Modifier.size(18.dp)
                                )
                            }
                        }

                        // Expanded Word List
                        AnimatedVisibility(visible = isExpanded) {
                            Column(
                                modifier = Modifier.fillMaxWidth(),
                                verticalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                generatedSet.words.forEachIndexed { index, w ->
                                    Surface(
                                        shape = RoundedCornerShape(10.dp),
                                        color = Surface,
                                        border = BorderStroke(1.dp, Border)
                                    ) {
                                        Column(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .padding(10.dp)
                                        ) {
                                            Row(
                                                verticalAlignment = Alignment.CenterVertically,
                                                horizontalArrangement = Arrangement.SpaceBetween,
                                                modifier = Modifier.fillMaxWidth()
                                            ) {
                                                Row(verticalAlignment = Alignment.CenterVertically) {
                                                    Text(
                                                        text = "${index + 1}. ${w.word}",
                                                        style = MaterialTheme.typography.bodyMedium,
                                                        fontWeight = FontWeight.Bold,
                                                        color = Ink
                                                    )
                                                    Spacer(modifier = Modifier.width(6.dp))
                                                    Text(
                                                        text = w.ipa,
                                                        style = MaterialTheme.typography.bodySmall,
                                                        color = Muted
                                                    )
                                                }
                                                Surface(
                                                    color = AccentSoft,
                                                    shape = RoundedCornerShape(4.dp)
                                                ) {
                                                    Text(
                                                        text = w.partOfSpeech.label,
                                                        style = MaterialTheme.typography.labelSmall,
                                                        color = AccentDark,
                                                        modifier = Modifier.padding(horizontal = 4.dp, vertical = 2.dp)
                                                    )
                                                }
                                            }
                                            Spacer(modifier = Modifier.height(2.dp))
                                            Text(
                                                text = w.meaning,
                                                style = MaterialTheme.typography.bodySmall,
                                                color = Ink
                                            )
                                            Spacer(modifier = Modifier.height(2.dp))
                                            Text(
                                                text = "“${w.exampleSentence}”",
                                                style = MaterialTheme.typography.bodySmall.copy(fontStyle = FontStyle.Italic),
                                                color = Muted
                                            )
                                        }
                                    }
                                }
                            }
                        }

                        // Action Buttons
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            if (!savedLocally) {
                                PrimaryButton(
                                    text = "💾 Save to My Sets",
                                    onClick = {
                                        onSaveSet(generatedSet)
                                        savedLocally = true
                                    },
                                    modifier = Modifier.weight(1f)
                                )
                            } else {
                                Surface(
                                    color = MasteredSoft,
                                    shape = RoundedCornerShape(14.dp),
                                    border = BorderStroke(1.dp, Mastered),
                                    modifier = Modifier.weight(1f)
                                ) {
                                    Row(
                                        modifier = Modifier.padding(vertical = 12.dp, horizontal = 8.dp),
                                        horizontalArrangement = Arrangement.Center,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Icon(
                                            Icons.Default.Check,
                                            contentDescription = null,
                                            tint = Mastered,
                                            modifier = Modifier.size(18.dp)
                                        )
                                        Spacer(modifier = Modifier.width(6.dp))
                                        Text(
                                            text = "Saved in Library",
                                            style = MaterialTheme.typography.labelLarge,
                                            fontWeight = FontWeight.Bold,
                                            color = Mastered
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

