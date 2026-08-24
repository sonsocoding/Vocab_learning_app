package com.example.vocablearningapp

import com.example.vocablearningapp.ui.component.MarkdownBlock
import com.example.vocablearningapp.ui.component.parseInlineMarkdown
import com.example.vocablearningapp.ui.component.parseMarkdownBlocks
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class MarkdownParserTest {

    @Test
    fun parseMarkdownBlocks_headers() {
        val markdown = """
            # Title Header
            ## Section Subtitle
            ### Minor Header
        """.trimIndent()

        val blocks = parseMarkdownBlocks(markdown)
        assertEquals(3, blocks.size)
        assertTrue(blocks[0] is MarkdownBlock.Header && (blocks[0] as MarkdownBlock.Header).level == 1)
        assertEquals("Title Header", (blocks[0] as MarkdownBlock.Header).text)

        assertTrue(blocks[1] is MarkdownBlock.Header && (blocks[1] as MarkdownBlock.Header).level == 2)
        assertEquals("Section Subtitle", (blocks[1] as MarkdownBlock.Header).text)

        assertTrue(blocks[2] is MarkdownBlock.Header && (blocks[2] as MarkdownBlock.Header).level == 3)
        assertEquals("Minor Header", (blocks[2] as MarkdownBlock.Header).text)
    }

    @Test
    fun parseMarkdownBlocks_listsAndBullets() {
        val markdown = """
            * Bullet one
            - Bullet two
            1. Numbered first
            2. Numbered second
        """.trimIndent()

        val blocks = parseMarkdownBlocks(markdown)
        assertEquals(4, blocks.size)
        assertTrue(blocks[0] is MarkdownBlock.BulletItem && (blocks[0] as MarkdownBlock.BulletItem).prefix == "•")
        assertEquals("Bullet one", (blocks[0] as MarkdownBlock.BulletItem).text)

        assertTrue(blocks[1] is MarkdownBlock.BulletItem && (blocks[1] as MarkdownBlock.BulletItem).prefix == "•")
        assertEquals("Bullet two", (blocks[1] as MarkdownBlock.BulletItem).text)

        assertTrue(blocks[2] is MarkdownBlock.BulletItem && (blocks[2] as MarkdownBlock.BulletItem).prefix == "1.")
        assertEquals("Numbered first", (blocks[2] as MarkdownBlock.BulletItem).text)

        assertTrue(blocks[3] is MarkdownBlock.BulletItem && (blocks[3] as MarkdownBlock.BulletItem).prefix == "2.")
        assertEquals("Numbered second", (blocks[3] as MarkdownBlock.BulletItem).text)
    }

    @Test
    fun parseMarkdownBlocks_codeBlocksAndQuotes() {
        val markdown = """
            > This is an important quote
            ```kotlin
            val x = 10
            val y = 20
            ```
        """.trimIndent()

        val blocks = parseMarkdownBlocks(markdown)
        assertEquals(2, blocks.size)

        assertTrue(blocks[0] is MarkdownBlock.Quote)
        assertEquals("This is an important quote", (blocks[0] as MarkdownBlock.Quote).text)

        assertTrue(blocks[1] is MarkdownBlock.CodeBlock)
        val codeBlock = blocks[1] as MarkdownBlock.CodeBlock
        assertEquals("kotlin", codeBlock.language)
        assertEquals("val x = 10\nval y = 20", codeBlock.code)
    }

    @Test
    fun parseInlineMarkdown_stripsFormattingMarkersCorrectly() {
        val raw = "Hello **World** and *Italic* with `val x = 1` and ***BoldItalic***!"
        val annotated = parseInlineMarkdown(raw)

        // The text should have markers stripped in displayed representation
        assertEquals("Hello World and Italic with  val x = 1  and BoldItalic!", annotated.text)
        assertTrue(annotated.spanStyles.isNotEmpty())
    }
}
