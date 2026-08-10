package com.example.vocablearningapp.data

import com.example.vocablearningapp.domain.model.FsrsState
import com.example.vocablearningapp.domain.model.PartOfSpeech
import com.example.vocablearningapp.domain.model.VocabularyItem
import com.example.vocablearningapp.domain.model.VocabularySet

object MockData {
    private val initialLearningWordIds = setOf(
        "daily-life-ambitious",
        "school-assignment",
        "advanced-nuance-meticulous"
    )

    val vocabularySets = listOf(
        VocabularySet(
            id = "everyday-basics",
            title = "Everyday Basics",
            description = "Simple words for greetings, routines and familiar places.",
            category = "Everyday",
            level = "A1",
            words = listOf(
                word("everyday-basics", "hello", "xin chào", "/həˈləʊ/", "Hello, it is nice to meet you.", FsrsState.NEW),
                word("everyday-basics", "family", "gia đình", "/ˈfæməli/", "My family lives near the city.", FsrsState.NEW),
                word("everyday-basics", "friend", "người bạn", "/frend/", "My best friend studies with me.", FsrsState.NEW),
                word("everyday-basics", "morning", "buổi sáng", "/ˈmɔːnɪŋ/", "I read the news in the morning.", FsrsState.NEW),
                word("everyday-basics", "near", "gần", "/nɪə/", "The library is near my home.", FsrsState.NEW),
                word("everyday-basics", "usually", "thường xuyên", "/ˈjuːʒuəli/", "I usually walk to class.", FsrsState.NEW)
            )
        ),
        VocabularySet(
            id = "home-family",
            title = "Home & Family",
            description = "Useful A1 vocabulary for talking about home and the people around you.",
            category = "Everyday",
            level = "A1",
            words = listOf(
                word("home-family", "kitchen", "nhà bếp", "/ˈkɪtʃən/", "The kitchen is next to the dining room.", FsrsState.NEW),
                word("home-family", "bedroom", "phòng ngủ", "/ˈbedruːm/", "My bedroom has a large window.", FsrsState.NEW),
                word("home-family", "parent", "cha mẹ; phụ huynh", "/ˈpeərənt/", "One parent came to the school meeting.", FsrsState.NEW),
                word("home-family", "garden", "khu vườn", "/ˈɡɑːdən/", "We grow herbs in the garden.", FsrsState.NEW),
                word("home-family", "quiet", "yên tĩnh", "/ˈkwaɪət/", "This is a quiet place to read.", FsrsState.NEW),
                word("home-family", "borrow", "mượn", "/ˈbɒrəʊ/", "Can I borrow your umbrella?", FsrsState.NEW)
            )
        ),
        VocabularySet(
            id = "daily-life",
            title = "Daily Life",
            description = "Familiar vocabulary for everyday conversations and routines.",
            category = "Everyday",
            level = "A2",
            words = listOf(
                word("daily-life", "daily", "hằng ngày", "/ˈdeɪli/", "I take a short walk daily.", FsrsState.NEW),
                word("daily-life", "ambitious", "tham vọng", "/æmˈbɪʃəs/", "She is ambitious and works hard for her goals.", FsrsState.NEW),
                word("daily-life", "commute", "đi lại giữa nhà và nơi làm việc hoặc trường học", "/kəˈmjuːt/", "My commute takes about thirty minutes.", FsrsState.NEW),
                word("daily-life", "grocery", "thực phẩm; hàng tạp hóa", "/ˈɡrəʊsəri/", "I need to buy some groceries after work.", FsrsState.NEW),
                word("daily-life", "habit", "thói quen", "/ˈhæbɪt/", "Reading before bed is a healthy habit.", FsrsState.NEW),
                word("daily-life", "schedule", "lịch trình", "/ˈʃedjuːl/", "Let me check my schedule first.", FsrsState.NEW),
                word("daily-life", "errand", "việc vặt cần làm", "/ˈerənd/", "I have a few errands to run this afternoon.", FsrsState.NEW),
                word("daily-life", "exhausted", "kiệt sức", "/ɪɡˈzɔːstɪd/", "I felt exhausted after the long trip.", FsrsState.NEW)
            )
        ),
        VocabularySet(
            id = "travel-basics",
            title = "Travel Basics",
            description = "Practical A2 words for planning trips and getting around.",
            category = "Travel",
            level = "A2",
            words = listOf(
                word("travel-basics", "journey", "hành trình", "/ˈdʒɜːni/", "The journey took three hours by train.", FsrsState.NEW),
                word("travel-basics", "luggage", "hành lý", "/ˈlʌɡɪdʒ/", "Please keep your luggage with you.", FsrsState.NEW),
                word("travel-basics", "ticket", "vé", "/ˈtɪkɪt/", "I booked my train ticket online.", FsrsState.NEW),
                word("travel-basics", "platform", "sân ga", "/ˈplætfɔːm/", "The train leaves from platform four.", FsrsState.NEW),
                word("travel-basics", "local", "địa phương; người địa phương", "/ˈləʊkəl/", "We asked a local for restaurant advice.", FsrsState.NEW),
                word("travel-basics", "crowded", "đông đúc", "/ˈkraʊdɪd/", "The market is crowded at weekends.", FsrsState.NEW)
            )
        ),
        VocabularySet(
            id = "school",
            title = "School",
            description = "Useful vocabulary for classes, assignments and academic life.",
            category = "Education",
            level = "B1",
            words = listOf(
                word("school", "assignment", "bài tập được giao", "/əˈsaɪnmənt/", "The assignment is due on Friday.", FsrsState.NEW),
                word("school", "curriculum", "chương trình học", "/kəˈrɪkjələm/", "The new curriculum includes more practical lessons.", FsrsState.NEW),
                word("school", "deadline", "hạn chót", "/ˈdedlaɪn/", "We must finish the project before the deadline.", FsrsState.NEW),
                word("school", "concentrate", "tập trung", "/ˈkɒnsəntreɪt/", "It is easier to concentrate in a quiet room.", FsrsState.NEW),
                word("school", "lecture", "bài giảng", "/ˈlektʃə/", "Today's lecture was about climate change.", FsrsState.NEW),
                word("school", "scholarship", "học bổng", "/ˈskɒləʃɪp/", "She received a scholarship to study abroad.", FsrsState.NEW),
                word("school", "revise", "ôn tập; chỉnh sửa", "/rɪˈvaɪz/", "I need to revise for tomorrow's exam.", FsrsState.NEW)
            )
        ),
        VocabularySet(
            id = "music-kpop",
            title = "Music & K-pop",
            description = "Words for talking about songs, stages and performances.",
            category = "Interests",
            level = "B1",
            words = listOf(
                word("music-kpop", "melody", "giai điệu", "/ˈmelədi/", "The melody stayed in my head all day.", FsrsState.NEW),
                word("music-kpop", "choreography", "vũ đạo", "/ˌkɒriˈɒɡrəfi/", "The choreography for this song is difficult.", FsrsState.NEW),
                word("music-kpop", "lyrics", "lời bài hát", "/ˈlɪrɪks/", "I looked up the lyrics after hearing the song.", FsrsState.NEW),
                word("music-kpop", "catchy", "bắt tai; dễ nhớ", "/ˈkætʃi/", "That chorus is incredibly catchy.", FsrsState.NEW),
                word("music-kpop", "rehearsal", "buổi tập dượt", "/rɪˈhɜːsəl/", "The dancers have rehearsal every evening.", FsrsState.NEW),
                word("music-kpop", "performance", "màn trình diễn", "/pəˈfɔːməns/", "Their live performance was unforgettable.", FsrsState.NEW)
            )
        ),
        VocabularySet(
            id = "work-career",
            title = "Work & Career",
            description = "B2 vocabulary for professional conversations and workplace goals.",
            category = "Professional",
            level = "B2",
            words = listOf(
                word("work-career", "negotiate", "đàm phán; thương lượng", "/nɪˈɡəʊʃieɪt/", "We need to negotiate a fair contract.", FsrsState.NEW),
                word("work-career", "deadline", "hạn chót", "/ˈdedlaɪn/", "The team met the deadline despite the changes.", FsrsState.NEW),
                word("work-career", "promotion", "sự thăng chức", "/prəˈməʊʃən/", "She earned a promotion after leading the project.", FsrsState.NEW),
                word("work-career", "efficient", "hiệu quả", "/ɪˈfɪʃənt/", "The new process is much more efficient.", FsrsState.NEW),
                word("work-career", "initiative", "tính chủ động", "/ɪˈnɪʃətɪv/", "He showed initiative by solving the issue early.", FsrsState.NEW),
                word("work-career", "collaborate", "hợp tác", "/kəˈlæbəreɪt/", "Our teams collaborate across three offices.", FsrsState.NEW)
            )
        ),
        VocabularySet(
            id = "science-society",
            title = "Science & Society",
            description = "B2 terms for discussing research, change and public life.",
            category = "Society",
            level = "B2",
            words = listOf(
                word("science-society", "evidence", "bằng chứng", "/ˈevɪdəns/", "The report provides evidence of a wider trend.", FsrsState.NEW),
                word("science-society", "impact", "tác động", "/ˈɪmpækt/", "The policy could have a major impact on families.", FsrsState.NEW),
                word("science-society", "sustainable", "bền vững", "/səˈsteɪnəbəl/", "The city is investing in sustainable transport.", FsrsState.NEW),
                word("science-society", "innovation", "sự đổi mới", "/ˌɪnəˈveɪʃən/", "Innovation can change how people communicate.", FsrsState.NEW),
                word("science-society", "bias", "thiên kiến", "/ˈbaɪəs/", "The researchers checked the study for bias.", FsrsState.NEW),
                word("science-society", "regulate", "điều chỉnh; quản lý bằng quy định", "/ˈreɡjəleɪt/", "Governments regulate the use of public data.", FsrsState.NEW)
            )
        ),
        VocabularySet(
            id = "academic-writing",
            title = "Academic Writing",
            description = "C1 vocabulary for building clear, precise arguments.",
            category = "Academic",
            level = "C1",
            words = listOf(
                word("academic-writing", "coherent", "mạch lạc", "/kəʊˈhɪərənt/", "The essay presents a coherent argument.", FsrsState.NEW),
                word("academic-writing", "substantial", "đáng kể; lớn", "/səbˈstænʃəl/", "The study found a substantial difference between groups.", FsrsState.NEW),
                word("academic-writing", "hypothesis", "giả thuyết", "/haɪˈpɒθəsɪs/", "The experiment was designed to test the hypothesis.", FsrsState.NEW),
                word("academic-writing", "implication", "hệ quả; hàm ý", "/ˌɪmplɪˈkeɪʃən/", "The findings have important implications for teachers.", FsrsState.NEW),
                word("academic-writing", "synthesize", "tổng hợp", "/ˈsɪnθəsaɪz/", "The final section synthesizes the main findings.", FsrsState.NEW),
                word("academic-writing", "ambiguous", "mơ hồ; có nhiều nghĩa", "/æmˈbɪɡjuəs/", "The wording is ambiguous and needs clarification.", FsrsState.NEW)
            )
        ),
        VocabularySet(
            id = "advanced-nuance",
            title = "Advanced Nuance",
            description = "C2 expressions for precision, tone and complex ideas.",
            category = "Advanced",
            level = "C2",
            words = listOf(
                word("advanced-nuance", "meticulous", "tỉ mỉ; cẩn thận", "/məˈtɪkjələs/", "She kept meticulous records of every decision.", FsrsState.NEW),
                word("advanced-nuance", "conundrum", "vấn đề nan giải", "/kəˈnʌndrəm/", "The committee faced a difficult ethical conundrum.", FsrsState.NEW),
                word("advanced-nuance", "pragmatic", "thực tế; thực dụng", "/præɡˈmætɪk/", "They took a pragmatic approach to the negotiations.", FsrsState.NEW),
                word("advanced-nuance", "unequivocal", "rõ ràng; dứt khoát", "/ˌʌnɪˈkwɪvəkəl/", "The statement was an unequivocal rejection of the proposal.", FsrsState.NEW),
                word("advanced-nuance", "discerning", "sành sỏi; tinh tường", "/dɪˈsɜːnɪŋ/", "The discerning reader will notice the subtle contrast.", FsrsState.NEW),
                word("advanced-nuance", "pervasive", "phổ biến rộng khắp", "/pəˈveɪsɪv/", "Digital media has a pervasive influence on public debate.", FsrsState.NEW)
            )
        )
    )

    // These words are generated separately from study sets for the Daily Words feature.
    val dailyWordsByLevel = mapOf(
        "A1" to listOf(
            word("daily-a1", "clock", "đồng hồ", "/klɒk/", "The clock is on the wall.", FsrsState.NEW),
            word("daily-a1", "smile", "mỉm cười", "/smaɪl/", "She smiled at the child.", FsrsState.NEW),
            word("daily-a1", "early", "sớm", "/ˈɜːli/", "I woke up early today.", FsrsState.NEW),
            word("daily-a1", "street", "đường phố", "/striːt/", "The cafe is on the next street.", FsrsState.NEW),
            word("daily-a1", "carry", "mang; xách", "/ˈkæri/", "Can you carry this bag?", FsrsState.NEW)
        ),
        "A2" to listOf(
            word("daily-a2", "curious", "tò mò", "/ˈkjʊəriəs/", "The curious student asked another question.", FsrsState.NEW),
            word("daily-a2", "repair", "sửa chữa", "/rɪˈpeə/", "The shop can repair your phone.", FsrsState.NEW),
            word("daily-a2", "reliable", "đáng tin cậy", "/rɪˈlaɪəbəl/", "We need a reliable internet connection.", FsrsState.NEW),
            word("daily-a2", "recently", "gần đây", "/ˈriːsəntli/", "Have you seen any good films recently?", FsrsState.NEW),
            word("daily-a2", "improve", "cải thiện", "/ɪmˈpruːv/", "Practice will improve your speaking.", FsrsState.NEW)
        ),
        "B1" to listOf(
            word("daily-b1", "adapt", "thích nghi", "/əˈdæpt/", "Animals adapt to changes in their environment.", FsrsState.NEW),
            word("daily-b1", "feature", "đặc điểm; tính năng", "/ˈfiːtʃə/", "The app has a useful search feature.", FsrsState.NEW),
            word("daily-b1", "outcome", "kết quả", "/ˈaʊtkʌm/", "The outcome was better than expected.", FsrsState.NEW),
            word("daily-b1", "influence", "ảnh hưởng", "/ˈɪnfluəns/", "Music can influence our mood.", FsrsState.NEW),
            word("daily-b1", "approach", "cách tiếp cận", "/əˈprəʊtʃ/", "We need a different approach to the problem.", FsrsState.NEW)
        ),
        "B2" to listOf(
            word("daily-b2", "compelling", "thuyết phục; hấp dẫn", "/kəmˈpelɪŋ/", "The speaker made a compelling argument.", FsrsState.NEW),
            word("daily-b2", "allocate", "phân bổ", "/ˈæləkeɪt/", "The manager allocated more time to training.", FsrsState.NEW),
            word("daily-b2", "perspective", "góc nhìn; quan điểm", "/pəˈspektɪv/", "Travel gave her a new perspective.", FsrsState.NEW),
            word("daily-b2", "contribute", "đóng góp", "/kənˈtrɪbjuːt/", "Several factors contributed to the success.", FsrsState.NEW),
            word("daily-b2", "justify", "biện minh; chứng minh là hợp lý", "/ˈdʒʌstɪfaɪ/", "The data does not justify that conclusion.", FsrsState.NEW)
        ),
        "C1" to listOf(
            word("daily-c1", "alleviate", "làm giảm; xoa dịu", "/əˈliːvieɪt/", "The new policy may alleviate pressure on families.", FsrsState.NEW),
            word("daily-c1", "articulate", "diễn đạt rõ ràng", "/ɑːˈtɪkjələt/", "She gave an articulate response to the question.", FsrsState.NEW),
            word("daily-c1", "discern", "nhận ra; phân biệt", "/dɪˈsɜːn/", "It is difficult to discern the pattern at first.", FsrsState.NEW),
            word("daily-c1", "criterion", "tiêu chí", "/kraɪˈtɪəriən/", "Cost was the main criterion for the decision.", FsrsState.NEW),
            word("daily-c1", "resilient", "kiên cường; có khả năng phục hồi", "/rɪˈzɪliənt/", "The resilient community rebuilt after the storm.", FsrsState.NEW)
        ),
        "C2" to listOf(
            word("daily-c2", "inexorable", "không thể ngăn cản", "/ɪnˈeksərəbəl/", "The inexorable advance of technology changed the industry.", FsrsState.NEW),
            word("daily-c2", "juxtapose", "đặt cạnh nhau để so sánh", "/ˌdʒʌkstəˈpəʊz/", "The article juxtaposes two opposing viewpoints.", FsrsState.NEW),
            word("daily-c2", "tenuous", "mong manh; không chắc chắn", "/ˈtenjuəs/", "The connection between the events remains tenuous.", FsrsState.NEW),
            word("daily-c2", "perfunctory", "qua loa; chiếu lệ", "/pəˈfʌŋktəri/", "He offered a perfunctory apology and left.", FsrsState.NEW),
            word("daily-c2", "ubiquitous", "phổ biến ở khắp nơi", "/juːˈbɪkwɪtəs/", "Smartphones are ubiquitous in modern cities.", FsrsState.NEW)
        )
    )

    private val partOfSpeechByWord: Map<String, PartOfSpeech>
        get() = mapOf(
        "hello" to PartOfSpeech.NOUN,
        "near" to PartOfSpeech.ADJECTIVE,
        "usually" to PartOfSpeech.ADJECTIVE,
        "quiet" to PartOfSpeech.ADJECTIVE,
        "borrow" to PartOfSpeech.VERB,
        "daily" to PartOfSpeech.ADJECTIVE,
        "ambitious" to PartOfSpeech.ADJECTIVE,
        "commute" to PartOfSpeech.VERB,
        "exhausted" to PartOfSpeech.ADJECTIVE,
        "local" to PartOfSpeech.ADJECTIVE,
        "crowded" to PartOfSpeech.ADJECTIVE,
        "concentrate" to PartOfSpeech.VERB,
        "revise" to PartOfSpeech.VERB,
        "catchy" to PartOfSpeech.ADJECTIVE,
        "negotiate" to PartOfSpeech.VERB,
        "efficient" to PartOfSpeech.ADJECTIVE,
        "collaborate" to PartOfSpeech.VERB,
        "sustainable" to PartOfSpeech.ADJECTIVE,
        "regulate" to PartOfSpeech.VERB,
        "coherent" to PartOfSpeech.ADJECTIVE,
        "substantial" to PartOfSpeech.ADJECTIVE,
        "synthesize" to PartOfSpeech.VERB,
        "ambiguous" to PartOfSpeech.ADJECTIVE,
        "meticulous" to PartOfSpeech.ADJECTIVE,
        "pragmatic" to PartOfSpeech.ADJECTIVE,
        "unequivocal" to PartOfSpeech.ADJECTIVE,
        "discerning" to PartOfSpeech.ADJECTIVE,
        "pervasive" to PartOfSpeech.ADJECTIVE,
        "smile" to PartOfSpeech.VERB,
        "early" to PartOfSpeech.ADJECTIVE,
        "carry" to PartOfSpeech.VERB,
        "curious" to PartOfSpeech.ADJECTIVE,
        "repair" to PartOfSpeech.VERB,
        "reliable" to PartOfSpeech.ADJECTIVE,
        "recently" to PartOfSpeech.ADJECTIVE,
        "improve" to PartOfSpeech.VERB,
        "adapt" to PartOfSpeech.VERB,
        "influence" to PartOfSpeech.VERB,
        "approach" to PartOfSpeech.VERB,
        "compelling" to PartOfSpeech.ADJECTIVE,
        "allocate" to PartOfSpeech.VERB,
        "contribute" to PartOfSpeech.VERB,
        "justify" to PartOfSpeech.VERB,
        "alleviate" to PartOfSpeech.VERB,
        "articulate" to PartOfSpeech.VERB,
        "discern" to PartOfSpeech.VERB,
        "resilient" to PartOfSpeech.ADJECTIVE,
        "inexorable" to PartOfSpeech.ADJECTIVE,
        "juxtapose" to PartOfSpeech.VERB,
        "tenuous" to PartOfSpeech.ADJECTIVE,
        "perfunctory" to PartOfSpeech.ADJECTIVE,
        "ubiquitous" to PartOfSpeech.ADJECTIVE
    )

    private fun word(
        setId: String,
        word: String,
        meaning: String,
        pronunciation: String,
        example: String,
        _seedState: FsrsState
    ): VocabularyItem {
        val id = "$setId-${word.lowercase().replace(" ", "-")}"
        val seededState = if (id in initialLearningWordIds) {
            FsrsState.LEARNING
        } else {
            _seedState
        }
        val nowMillis = System.currentTimeMillis()
        return VocabularyItem(
            id = id,
            word = word,
            meaning = meaning,
            pronunciation = pronunciation,
            partOfSpeech = partOfSpeechByWord[word] ?: PartOfSpeech.NOUN,
            exampleSentence = example,
            fsrsState = seededState,
            fsrsStep = if (seededState == FsrsState.NEW) null else 0,
            dueAtMillis = if (seededState == FsrsState.NEW) {
                0L
            } else {
                nowMillis
            },
            scheduledDays = 0
        )
    }
}
