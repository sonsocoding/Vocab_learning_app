package com.example.vocablearningapp.ui.screen.ai

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material.icons.filled.Key
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Send
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.vocablearningapp.data.ai.AiActionPayload
import com.example.vocablearningapp.data.ai.AiChatMessage
import com.example.vocablearningapp.data.ai.AiGeneratedSet
import com.example.vocablearningapp.data.ai.AiPreferences
import com.example.vocablearningapp.data.ai.GeminiApiClient
import com.example.vocablearningapp.data.ai.MessageSender
import com.example.vocablearningapp.domain.model.VocabularySet
import com.example.vocablearningapp.ui.component.PrimaryButton
import com.example.vocablearningapp.ui.component.SecondaryButton
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
import kotlinx.coroutines.launch

@Composable
fun AiChatScreen(
    onBack: () -> Unit,
    onSaveSet: (VocabularySet) -> Unit,
    onOpenSet: (String) -> Unit
) {
    val context = LocalContext.current
    val aiPreferences = remember { AiPreferences(context) }
    val geminiClient = remember { GeminiApiClient(aiPreferences) }
    val coroutineScope = rememberCoroutineScope()

    var showApiKeyDialog by remember { mutableStateOf(false) }
    var isApiKeyVisible by remember { mutableStateOf(false) }
    var apiKeyInput by remember { mutableStateOf(aiPreferences.apiKey) }
    var selectedModel by remember { mutableStateOf(aiPreferences.model) }
    var hasLiveKey by remember { mutableStateOf(aiPreferences.hasApiKey) }

    val messages = remember {
        mutableStateListOf(
            AiChatMessage(
                sender = MessageSender.AI,
                text = "Hello! I am your **VocabAI Coach** ✨.\n\nAsk me any English question, or tell me a topic and I will automatically create custom vocabulary sets and exercises for you!"
            )
        )
    }

    var inputText by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }
    val listState = rememberLazyListState()

    val quickPrompts = listOf(
        "✨ Create B2 Job Interview Set",
        "🌍 Travel & Tourism Vocab (B1)",
        "🔍 Explain affect vs effect",
        "🧠 How does FSRS algorithm work?",
        "💼 Business Negotiation Terms"
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

    Scaffold(
        containerColor = Canvas,
        topBar = {
            VocabTopBar(
                title = "AI Vocab Coach",
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
                        isApiKeyVisible = false
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
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            // Quick Prompts Row
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState())
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                quickPrompts.forEach { prompt ->
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
                                text = "VocabAI Coach is thinking...",
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
                        placeholder = { Text("Ask AI or say: 'Create a B2 set about...'") },
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
                            imageVector = Icons.Default.Send,
                            contentDescription = "Send",
                            tint = if (inputText.isNotBlank() && !isLoading) Surface else Muted,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }
            }
        }
    }

    // API Key Settings Dialog
    if (showApiKeyDialog) {
        AlertDialog(
            onDismissRequest = { showApiKeyDialog = false },
            title = {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Key, contentDescription = null, tint = Accent)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("AI Engine Settings")
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
                        placeholder = { Text("AIzaSy...") },
                        singleLine = true,
                        visualTransformation = if (isApiKeyVisible) VisualTransformation.None else PasswordVisualTransformation(),
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Password,
                            imeAction = ImeAction.Done
                        ),
                        trailingIcon = {
                            IconButton(onClick = { isApiKeyVisible = !isApiKeyVisible }) {
                                Icon(
                                    imageVector = if (isApiKeyVisible) Icons.Filled.VisibilityOff else Icons.Filled.Visibility,
                                    contentDescription = if (isApiKeyVisible) "Hide API Key" else "Show API Key",
                                    tint = Muted
                                )
                            }
                        },
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
                        aiPreferences.apiKey = apiKeyInput
                        aiPreferences.model = selectedModel
                        hasLiveKey = aiPreferences.hasApiKey
                        showApiKeyDialog = false
                    }
                )
            },
            dismissButton = {
                Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                    if (aiPreferences.hasApiKey) {
                        TextButton(onClick = {
                            apiKeyInput = ""
                            aiPreferences.apiKey = ""
                            hasLiveKey = false
                            showApiKeyDialog = false
                        }) {
                            Text("Clear", color = com.example.vocablearningapp.ui.theme.Forgot)
                        }
                    }
                    TextButton(onClick = { showApiKeyDialog = false }) {
                        Text("Cancel")
                    }
                }
            }
        )
    }
}

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

            // Embedded Action Card (if AI generated a Vocabulary Set)
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
