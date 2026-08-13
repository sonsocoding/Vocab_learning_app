package com.example.vocablearningapp.domain.util

object IpaGenerator {
    private val dictionary = mapOf(
        "hello" to "/həˈləʊ/",
        "family" to "/ˈfæməli/",
        "friend" to "/frend/",
        "morning" to "/ˈmɔːnɪŋ/",
        "near" to "/nɪə/",
        "usually" to "/ˈjuːʒuəli/",
        "kitchen" to "/ˈkɪtʃən/",
        "bedroom" to "/ˈbedruːm/",
        "parent" to "/ˈpeərənt/",
        "garden" to "/ˈɡɑːdən/",
        "quiet" to "/ˈkwaɪət/",
        "borrow" to "/ˈbɒrəʊ/",
        "daily" to "/ˈdeɪli/",
        "ambitious" to "/æmˈbɪʃ.əs/",
        "commute" to "/kəˈmjuːt/",
        "grocery" to "/ˈɡrəʊsəri/",
        "habit" to "/ˈhæbɪt/",
        "schedule" to "/ˈʃedjuːl/",
        "errand" to "/ˈerənd/",
        "exhausted" to "/ɪɡˈzɔːstɪd/",
        "journey" to "/ˈdʒɜːni/",
        "luggage" to "/ˈlʌɡɪdʒ/",
        "ticket" to "/ˈtɪkɪt/",
        "platform" to "/ˈplætfɔːm/",
        "local" to "/ˈləʊkəl/",
        "crowded" to "/ˈkraʊdɪd/",
        "assignment" to "/əˈsaɪnmənt/",
        "curriculum" to "/kəˈrɪkjələm/",
        "deadline" to "/ˈdedlaɪn/",
        "concentrate" to "/ˈkɒnsəntreɪt/",
        "lecture" to "/ˈlektʃə/",
        "scholarship" to "/ˈskɒləʃɪp/",
        "revise" to "/rɪˈvaɪz/",
        "melody" to "/ˈmelədi/",
        "choreography" to "/ˌkɒriˈɒɡrəfi/",
        "lyrics" to "/ˈlɪrɪks/",
        "catchy" to "/ˈkætʃi/",
        "rehearsal" to "/rɪˈhɜːsəl/",
        "performance" to "/pəˈfɔːməns/",
        "negotiate" to "/nɪˈɡəʊʃieɪt/",
        "promotion" to "/prəˈməʊʃən/",
        "efficient" to "/ɪˈfɪʃənt/",
        "initiative" to "/ɪˈnɪʃətɪv/",
        "collaborate" to "/kəˈlæbəreɪt/",
        "evidence" to "/ˈevɪdəns/",
        "impact" to "/ˈɪmpækt/",
        "sustainable" to "/səˈsteɪnəbəl/",
        "innovation" to "/ˌɪnəˈveɪʃən/",
        "bias" to "/ˈbaɪəs/",
        "regulate" to "/ˈreɡjəleɪt/",
        "coherent" to "/kəʊˈhɪərənt/",
        "substantial" to "/səbˈstænʃəl/",
        "hypothesis" to "/haɪˈpɒθəsɪs/",
        "implication" to "/ˌɪmplɪˈkeɪʃən/",
        "synthesize" to "/ˈsɪnθəsaɪz/",
        "ambiguous" to "/æmˈbɪɡjuəs/",
        "meticulous" to "/məˈtɪkjələs/",
        "conundrum" to "/kəˈnʌndrəm/",
        "pragmatic" to "/præɡˈmætɪk/",
        "unequivocal" to "/ˌʌnɪˈkwɪvəkəl/",
        "discerning" to "/dɪˈsɜːnɪŋ/",
        "pervasive" to "/pəˈveɪsɪv/",
        "computer" to "/kəmˈpjuː.tər/",
        "student" to "/ˈstjuː.dənt/",
        "teacher" to "/ˈtiː.tʃər/",
        "language" to "/ˈlæŋ.ɡwɪdʒ/",
        "vocabulary" to "/vəˈkæb.jə.lər.i/",
        "learning" to "/ˈlɜː.nɪŋ/",
        "system" to "/ˈsɪs.təm/",
        "beautiful" to "/ˈbjuː.tɪ.fəl/",
        "important" to "/ɪmˈpɔː.tənt/",
        "challenge" to "/ˈtʃæl.ɪndʒ/",
        "success" to "/səkˈses/",
        "future" to "/ˈfjuː.tʃər/",
        "practice" to "/ˈpræk.tɪs/",
        "knowledge" to "/ˈnɒl.ɪdʒ/"
    )

    val commonIpaSymbols = listOf(
        "ˈ", "ˌ", "ː", "æ", "ə", "ɪ", "ʊ", "θ", "ð", "ʃ", "ʒ", "ŋ", "ʌ", "ɔː", "iː", "uː", "eɪ", "aɪ", "ɔɪ", "əʊ", "aʊ", "ɪə", "eə", "/"
    )

    fun generateIpa(rawWord: String): String {
        val clean = rawWord.trim().lowercase()
        if (clean.isBlank()) return ""

        dictionary[clean]?.let { return it }

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
