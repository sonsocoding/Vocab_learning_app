package com.example.vocablearningapp.ui.component

import android.os.Bundle
import android.speech.tts.TextToSpeech
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.VolumeUp
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import java.util.Locale

private const val TTS_VOLUME_MULTIPLIER = 2.0f

@Composable
fun SpeechButton(
    text: String,
    contentDescription: String,
    modifier: Modifier = Modifier,
    tint: Color = com.example.vocablearningapp.ui.theme.Accent
) {
    val context = LocalContext.current
    var isReady by remember { mutableStateOf(false) }
    val textToSpeech = remember(context) {
        TextToSpeech(context) { status ->
            isReady = status == TextToSpeech.SUCCESS
        }
    }

    DisposableEffect(textToSpeech) {
        onDispose {
            textToSpeech.stop()
            textToSpeech.shutdown()
        }
    }

    IconButton(
        onClick = {
            textToSpeech.language = Locale.US
            textToSpeech.speak(
                text,
                TextToSpeech.QUEUE_FLUSH,
                Bundle().apply {
                    putFloat(TextToSpeech.Engine.KEY_PARAM_VOLUME, TTS_VOLUME_MULTIPLIER)
                },
                "vocabulary-${text.hashCode()}"
            )
        },
        enabled = isReady && text.isNotBlank(),
        modifier = modifier
    ) {
        Icon(
            imageVector = Icons.AutoMirrored.Filled.VolumeUp,
            contentDescription = contentDescription,
            tint = tint
        )
    }
}
