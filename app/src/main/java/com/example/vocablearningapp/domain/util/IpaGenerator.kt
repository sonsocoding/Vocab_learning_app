package com.example.vocablearningapp.domain.util

import com.example.vocablearningapp.data.DictionaryRepository

object IpaGenerator {
    val commonIpaSymbols = listOf(
        "ˈ", "ˌ", "ː", "æ", "ə", "ɪ", "ʊ", "θ", "ð", "ʃ", "ʒ", "ŋ", "ʌ", "ɔː", "iː", "uː", "eɪ", "aɪ", "ɔɪ", "əʊ", "aʊ", "ɪə", "eə", "/"
    )

    fun generateIpa(rawWord: String): String {
        val clean = rawWord.trim().lowercase()
        if (clean.isBlank()) return ""

        DictionaryRepository.lookupWord(clean)?.ipa?.let { return it }

        var ipa = clean
            .replace("tion", "ʃən")
            .replace("sion", "ʒən")
            .replace("tial", "ʃəl")
            .replace("ing", "ɪŋ")
            .replace("ph", "f")
            .replace("th", "θ")
            .replace("sh", "ʃ")
            .replace("ch", "tʃ")
            .replace("ck", "k")
            .replace("qu", "kw")
            .replace("ee", "iː")
            .replace("ea", "iː")
            .replace("oo", "uː")
            .replace("ou", "aʊ")
            .replace("ow", "əʊ")
            .replace("ay", "eɪ")
            .replace("ai", "eɪ")
            .replace("igh", "aɪ")
            .replace("oa", "əʊ")
            .replace("ar", "ɑː")
            .replace("or", "ɔː")
            .replace("er", "ər")
            .replace("ir", "ɜː")
            .replace("ur", "ɜː")
            .replace("al", "ɔːl")
            .replace("all", "ɔːl")

        ipa = ipa
            .replace("a", "æ")
            .replace("e", "e")
            .replace("i", "ɪ")
            .replace("o", "ɒ")
            .replace("u", "ʌ")
            .replace("y", "i")

        return "/ˈ$ipa/"
    }
}
