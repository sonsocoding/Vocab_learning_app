package com.example.vocablearningapp.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.vocablearningapp.ui.theme.Accent
import com.example.vocablearningapp.ui.theme.AccentDark
import com.example.vocablearningapp.ui.theme.AccentSoft
import com.example.vocablearningapp.ui.theme.Border
import com.example.vocablearningapp.ui.theme.Ink
import com.example.vocablearningapp.ui.theme.Muted
import com.example.vocablearningapp.ui.theme.SurfaceMuted

/**
 * Lightweight, zero-dependency Markdown renderer for Jetpack Compose.
 * Specifically optimized for chat dialogues, vocabulary explanations, and grammar guides.
 */
@Composable
fun MarkdownText(
    markdown: String,
    modifier: Modifier = Modifier,
    color: Color = Ink,
    style: TextStyle = MaterialTheme.typography.bodyMedium.copy(lineHeight = 22.sp),
    isUser: Boolean = false
) {
    val blocks = remember(markdown) { parseMarkdownBlocks(markdown) }

    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        blocks.forEach { block ->
            when (block) {
                is MarkdownBlock.Header -> {
                    val headerStyle = when (block.level) {
                        1 -> MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold, color = color)
                        2 -> MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold, color = color)
                        else -> MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.SemiBold, color = color)
                    }
                    Text(
                        text = parseInlineMarkdown(block.text, isUser = isUser, defaultColor = color),
                        style = headerStyle,
                        modifier = Modifier.padding(top = 4.dp, bottom = 2.dp)
                    )
                }

                is MarkdownBlock.BulletItem -> {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.Top
                    ) {
                        Text(
                            text = block.prefix,
                            style = style.copy(
                                fontWeight = FontWeight.Bold,
                                color = if (isUser) color else Accent
                            ),
                            modifier = Modifier.padding(end = 6.dp)
                        )
                        Text(
                            text = parseInlineMarkdown(block.text, isUser = isUser, defaultColor = color),
                            style = style,
                            color = color,
                            modifier = Modifier.weight(1f)
                        )
                    }
                }

                is MarkdownBlock.CodeBlock -> {
                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = if (isUser) AccentDark.copy(alpha = 0.3f) else SurfaceMuted,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = block.code,
                            style = MaterialTheme.typography.bodySmall.copy(
                                fontFamily = FontFamily.Monospace,
                                lineHeight = 18.sp
                            ),
                            color = if (isUser) Color.White else Ink,
                            modifier = Modifier.padding(10.dp)
                        )
                    }
                }

                is MarkdownBlock.Quote -> {
                    Row(modifier = Modifier.fillMaxWidth()) {
                        Box(
                            modifier = Modifier
                                .width(3.dp)
                                .height(22.dp)
                                .background(if (isUser) Color.White.copy(alpha = 0.6f) else Accent, RoundedCornerShape(2.dp))
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = parseInlineMarkdown(block.text, isUser = isUser, defaultColor = color),
                            style = style.copy(fontStyle = FontStyle.Italic),
                            color = if (isUser) color else Muted
                        )
                    }
                }

                is MarkdownBlock.Paragraph -> {
                    if (block.text.isNotBlank()) {
                        Text(
                            text = parseInlineMarkdown(block.text, isUser = isUser, defaultColor = color),
                            style = style,
                            color = color
                        )
                    }
                }
            }
        }
    }
}

/**
 * Structured Markdown Block representations.
 */
sealed class MarkdownBlock {
    data class Header(val level: Int, val text: String) : MarkdownBlock()
    data class BulletItem(val prefix: String, val text: String) : MarkdownBlock()
    data class CodeBlock(val language: String?, val code: String) : MarkdownBlock()
    data class Quote(val text: String) : MarkdownBlock()
    data class Paragraph(val text: String) : MarkdownBlock()
}

/**
 * Parses raw markdown into structured blocks (Headers, Lists, Code blocks, Quotes, Paragraphs).
 */
fun parseMarkdownBlocks(markdown: String): List<MarkdownBlock> {
    val lines = markdown.lines()
    val blocks = mutableListOf<MarkdownBlock>()

    var inCodeBlock = false
    var codeLang: String? = null
    val codeBuffer = StringBuilder()

    var i = 0
    while (i < lines.size) {
        val line = lines[i]
        val trimmed = line.trim()

        // Handle Code Blocks (``` ... ```)
        if (trimmed.startsWith("```")) {
            if (inCodeBlock) {
                blocks.add(MarkdownBlock.CodeBlock(codeLang, codeBuffer.toString().trimEnd()))
                codeBuffer.clear()
                inCodeBlock = false
                codeLang = null
            } else {
                inCodeBlock = true
                codeLang = trimmed.removePrefix("```").trim().takeIf { it.isNotBlank() }
            }
            i++
            continue
        }

        if (inCodeBlock) {
            if (codeBuffer.isNotEmpty()) codeBuffer.append("\n")
            codeBuffer.append(line)
            i++
            continue
        }

        // Empty lines
        if (trimmed.isBlank()) {
            i++
            continue
        }

        // Headers: #, ##, ###
        if (trimmed.startsWith("### ")) {
            blocks.add(MarkdownBlock.Header(3, trimmed.removePrefix("### ").trim()))
            i++
            continue
        } else if (trimmed.startsWith("## ")) {
            blocks.add(MarkdownBlock.Header(2, trimmed.removePrefix("## ").trim()))
            i++
            continue
        } else if (trimmed.startsWith("# ")) {
            blocks.add(MarkdownBlock.Header(1, trimmed.removePrefix("# ").trim()))
            i++
            continue
        }

        // Quotes: >
        if (trimmed.startsWith("> ")) {
            blocks.add(MarkdownBlock.Quote(trimmed.removePrefix("> ").trim()))
            i++
            continue
        }

        // Bullet lists: *, -, +, •
        val bulletMatch = Regex("""^([*+\-•])\s+(.+)""").find(trimmed)
        if (bulletMatch != null) {
            val content = bulletMatch.groupValues[2]
            blocks.add(MarkdownBlock.BulletItem("•", content))
            i++
            continue
        }

        // Numbered lists: 1. , 2. , etc.
        val numberedMatch = Regex("""^(\d+\.)\s+(.+)""").find(trimmed)
        if (numberedMatch != null) {
            val numPrefix = numberedMatch.groupValues[1]
            val content = numberedMatch.groupValues[2]
            blocks.add(MarkdownBlock.BulletItem(numPrefix, content))
            i++
            continue
        }

        // Regular paragraph line
        blocks.add(MarkdownBlock.Paragraph(trimmed))
        i++
    }

    if (inCodeBlock && codeBuffer.isNotEmpty()) {
        blocks.add(MarkdownBlock.CodeBlock(codeLang, codeBuffer.toString().trimEnd()))
    }

    return blocks
}

/**
 * Parses inline formatting tags:
 * - Bold: **text** or __text__
 * - Italic: *text* or _text_
 * - Bold + Italic: ***text***
 * - Inline Code: `text`
 * - Strikethrough: ~~text~~
 */
fun parseInlineMarkdown(
    text: String,
    isUser: Boolean = false,
    defaultColor: Color = Ink
): AnnotatedString {
    return buildAnnotatedString {
        var cursor = 0
        val length = text.length

        while (cursor < length) {
            // Check for Inline Code: `code`
            if (text[cursor] == '`') {
                val endBacktick = text.indexOf('`', cursor + 1)
                if (endBacktick != -1) {
                    val codeContent = text.substring(cursor + 1, endBacktick)
                    withStyle(
                        SpanStyle(
                            fontFamily = FontFamily.Monospace,
                            fontWeight = FontWeight.SemiBold,
                            background = if (isUser) Color.Black.copy(alpha = 0.15f) else AccentSoft.copy(alpha = 0.6f),
                            color = if (isUser) Color.White else AccentDark,
                            fontSize = 13.sp
                        )
                    ) {
                        append(" $codeContent ")
                    }
                    cursor = endBacktick + 1
                    continue
                }
            }

            // Check for Bold Italic: ***text***
            if (cursor + 2 < length && text.substring(cursor, cursor + 3) == "***") {
                val endTriple = text.indexOf("***", cursor + 3)
                if (endTriple != -1) {
                    val boldItalicContent = text.substring(cursor + 3, endTriple)
                    withStyle(SpanStyle(fontWeight = FontWeight.Bold, fontStyle = FontStyle.Italic)) {
                        append(boldItalicContent)
                    }
                    cursor = endTriple + 3
                    continue
                }
            }

            // Check for Bold: **text**
            if (cursor + 1 < length && text.substring(cursor, cursor + 2) == "**") {
                val endDouble = text.indexOf("**", cursor + 2)
                if (endDouble != -1) {
                    val boldContent = text.substring(cursor + 2, endDouble)
                    withStyle(SpanStyle(fontWeight = FontWeight.Bold)) {
                        append(boldContent)
                    }
                    cursor = endDouble + 2
                    continue
                }
            }

            // Check for Bold: __text__
            if (cursor + 1 < length && text.substring(cursor, cursor + 2) == "__") {
                val endDouble = text.indexOf("__", cursor + 2)
                if (endDouble != -1) {
                    val boldContent = text.substring(cursor + 2, endDouble)
                    withStyle(SpanStyle(fontWeight = FontWeight.Bold)) {
                        append(boldContent)
                    }
                    cursor = endDouble + 2
                    continue
                }
            }

            // Check for Strikethrough: ~~text~~
            if (cursor + 1 < length && text.substring(cursor, cursor + 2) == "~~") {
                val endTilde = text.indexOf("~~", cursor + 2)
                if (endTilde != -1) {
                    val strikeContent = text.substring(cursor + 2, endTilde)
                    withStyle(SpanStyle(textDecoration = TextDecoration.LineThrough)) {
                        append(strikeContent)
                    }
                    cursor = endTilde + 2
                    continue
                }
            }

            // Check for Italic: *text* (ensure it's not starting ** which was handled above)
            if (text[cursor] == '*' && (cursor == 0 || text[cursor - 1] != '*')) {
                val endSingle = text.indexOf('*', cursor + 1)
                if (endSingle != -1 && (endSingle + 1 >= length || text[endSingle + 1] != '*')) {
                    val italicContent = text.substring(cursor + 1, endSingle)
                    withStyle(SpanStyle(fontStyle = FontStyle.Italic)) {
                        append(italicContent)
                    }
                    cursor = endSingle + 1
                    continue
                }
            }

            // Check for Italic: _text_
            if (text[cursor] == '_' && (cursor == 0 || text[cursor - 1] != '_')) {
                val endSingle = text.indexOf('_', cursor + 1)
                if (endSingle != -1 && (endSingle + 1 >= length || text[endSingle + 1] != '_')) {
                    val italicContent = text.substring(cursor + 1, endSingle)
                    withStyle(SpanStyle(fontStyle = FontStyle.Italic)) {
                        append(italicContent)
                    }
                    cursor = endSingle + 1
                    continue
                }
            }

            // Default regular character
            append(text[cursor])
            cursor++
        }
    }
}
