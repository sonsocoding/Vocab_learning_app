package com.example.vocablearningapp.data

import com.example.vocablearningapp.domain.model.MemoryLevel
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
                word("everyday-basics", "hello", "xin chào", "/həˈləʊ/", "Hello, it is nice to meet you.", MemoryLevel.FORGOT),
                word("everyday-basics", "family", "gia đình", "/ˈfæməli/", "My family lives near the city.", MemoryLevel.FORGOT),
                word("everyday-basics", "friend", "người bạn", "/frend/", "My best friend studies with me.", MemoryLevel.FORGOT),
                word("everyday-basics", "morning", "buổi sáng", "/ˈmɔːnɪŋ/", "I read the news in the morning.", MemoryLevel.FORGOT),
                word("everyday-basics", "near", "gần", "/nɪə/", "The library is near my home.", MemoryLevel.FORGOT),
                word("everyday-basics", "usually", "thường xuyên", "/ˈjuːʒuəli/", "I usually walk to class.", MemoryLevel.FORGOT)
            )
        ),
        VocabularySet(
            id = "home-family",
            title = "Home & Family",
            description = "Useful A1 vocabulary for talking about home and the people around you.",
            category = "Everyday",
            level = "A1",
            words = listOf(
                word("home-family", "kitchen", "nhà bếp", "/ˈkɪtʃən/", "The kitchen is next to the dining room.", MemoryLevel.FORGOT),
                word("home-family", "bedroom", "phòng ngủ", "/ˈbedruːm/", "My bedroom has a large window.", MemoryLevel.FORGOT),
                word("home-family", "parent", "cha mẹ; phụ huynh", "/ˈpeərənt/", "One parent came to the school meeting.", MemoryLevel.FORGOT),
                word("home-family", "garden", "khu vườn", "/ˈɡɑːdən/", "We grow herbs in the garden.", MemoryLevel.FORGOT),
                word("home-family", "quiet", "yên tĩnh", "/ˈkwaɪət/", "This is a quiet place to read.", MemoryLevel.FORGOT),
                word("home-family", "borrow", "mượn", "/ˈbɒrəʊ/", "Can I borrow your umbrella?", MemoryLevel.FORGOT)
            )
        ),
        VocabularySet(
            id = "daily-life",
            title = "Daily Life",
            description = "Familiar vocabulary for everyday conversations and routines.",
            category = "Everyday",
            level = "A2",
            words = listOf(
                word("daily-life", "daily", "hằng ngày", "/ˈdeɪli/", "I take a short walk daily.", MemoryLevel.FORGOT),
                word("daily-life", "ambitious", "tham vọng", "/æmˈbɪʃəs/", "She is ambitious and works hard for her goals.", MemoryLevel.FORGOT),
                word("daily-life", "commute", "đi lại giữa nhà và nơi làm việc hoặc trường học", "/kəˈmjuːt/", "My commute takes about thirty minutes.", MemoryLevel.FORGOT),
                word("daily-life", "grocery", "thực phẩm; hàng tạp hóa", "/ˈɡrəʊsəri/", "I need to buy some groceries after work.", MemoryLevel.FORGOT),
                word("daily-life", "habit", "thói quen", "/ˈhæbɪt/", "Reading before bed is a healthy habit.", MemoryLevel.FORGOT),
                word("daily-life", "schedule", "lịch trình", "/ˈʃedjuːl/", "Let me check my schedule first.", MemoryLevel.FORGOT),
                word("daily-life", "errand", "việc vặt cần làm", "/ˈerənd/", "I have a few errands to run this afternoon.", MemoryLevel.FORGOT),
                word("daily-life", "exhausted", "kiệt sức", "/ɪɡˈzɔːstɪd/", "I felt exhausted after the long trip.", MemoryLevel.FORGOT)
            )
        ),
        VocabularySet(
            id = "travel-basics",
            title = "Travel Basics",
            description = "Practical A2 words for planning trips and getting around.",
            category = "Travel",
            level = "A2",
            words = listOf(
                word("travel-basics", "journey", "hành trình", "/ˈdʒɜːni/", "The journey took three hours by train.", MemoryLevel.FORGOT),
                word("travel-basics", "luggage", "hành lý", "/ˈlʌɡɪdʒ/", "Please keep your luggage with you.", MemoryLevel.FORGOT),
                word("travel-basics", "ticket", "vé", "/ˈtɪkɪt/", "I booked my train ticket online.", MemoryLevel.FORGOT),
                word("travel-basics", "platform", "sân ga", "/ˈplætfɔːm/", "The train leaves from platform four.", MemoryLevel.FORGOT),
                word("travel-basics", "local", "địa phương; người địa phương", "/ˈləʊkəl/", "We asked a local for restaurant advice.", MemoryLevel.FORGOT),
                word("travel-basics", "crowded", "đông đúc", "/ˈkraʊdɪd/", "The market is crowded at weekends.", MemoryLevel.FORGOT)
            )
        ),
        VocabularySet(
            id = "school",
            title = "School",
            description = "Useful vocabulary for classes, assignments and academic life.",
            category = "Education",
            level = "B1",
            words = listOf(
                word("school", "assignment", "bài tập được giao", "/əˈsaɪnmənt/", "The assignment is due on Friday.", MemoryLevel.FORGOT),
                word("school", "curriculum", "chương trình học", "/kəˈrɪkjələm/", "The new curriculum includes more practical lessons.", MemoryLevel.FORGOT),
                word("school", "deadline", "hạn chót", "/ˈdedlaɪn/", "We must finish the project before the deadline.", MemoryLevel.FORGOT),
                word("school", "concentrate", "tập trung", "/ˈkɒnsəntreɪt/", "It is easier to concentrate in a quiet room.", MemoryLevel.FORGOT),
                word("school", "lecture", "bài giảng", "/ˈlektʃə/", "Today's lecture was about climate change.", MemoryLevel.FORGOT),
                word("school", "scholarship", "học bổng", "/ˈskɒləʃɪp/", "She received a scholarship to study abroad.", MemoryLevel.FORGOT),
                word("school", "revise", "ôn tập; chỉnh sửa", "/rɪˈvaɪz/", "I need to revise for tomorrow's exam.", MemoryLevel.FORGOT)
            )
        ),
        VocabularySet(
            id = "music-kpop",
            title = "Music & K-pop",
            description = "Words for talking about songs, stages and performances.",
            category = "Interests",
            level = "B1",
            words = listOf(
                word("music-kpop", "melody", "giai điệu", "/ˈmelədi/", "The melody stayed in my head all day.", MemoryLevel.FORGOT),
                word("music-kpop", "choreography", "vũ đạo", "/ˌkɒriˈɒɡrəfi/", "The choreography for this song is difficult.", MemoryLevel.FORGOT),
                word("music-kpop", "lyrics", "lời bài hát", "/ˈlɪrɪks/", "I looked up the lyrics after hearing the song.", MemoryLevel.FORGOT),
                word("music-kpop", "catchy", "bắt tai; dễ nhớ", "/ˈkætʃi/", "That chorus is incredibly catchy.", MemoryLevel.FORGOT),
                word("music-kpop", "rehearsal", "buổi tập dượt", "/rɪˈhɜːsəl/", "The dancers have rehearsal every evening.", MemoryLevel.FORGOT),
                word("music-kpop", "performance", "màn trình diễn", "/pəˈfɔːməns/", "Their live performance was unforgettable.", MemoryLevel.FORGOT)
            )
        ),
        VocabularySet(
            id = "work-career",
            title = "Work & Career",
            description = "B2 vocabulary for professional conversations and workplace goals.",
            category = "Professional",
            level = "B2",
            words = listOf(
                word("work-career", "negotiate", "đàm phán; thương lượng", "/nɪˈɡəʊʃieɪt/", "We need to negotiate a fair contract.", MemoryLevel.FORGOT),
                word("work-career", "deadline", "hạn chót", "/ˈdedlaɪn/", "The team met the deadline despite the changes.", MemoryLevel.FORGOT),
                word("work-career", "promotion", "sự thăng chức", "/prəˈməʊʃən/", "She earned a promotion after leading the project.", MemoryLevel.FORGOT),
                word("work-career", "efficient", "hiệu quả", "/ɪˈfɪʃənt/", "The new process is much more efficient.", MemoryLevel.FORGOT),
                word("work-career", "initiative", "tính chủ động", "/ɪˈnɪʃətɪv/", "He showed initiative by solving the issue early.", MemoryLevel.FORGOT),
                word("work-career", "collaborate", "hợp tác", "/kəˈlæbəreɪt/", "Our teams collaborate across three offices.", MemoryLevel.FORGOT)
            )
        ),
        VocabularySet(
            id = "science-society",
            title = "Science & Society",
            description = "B2 terms for discussing research, change and public life.",
            category = "Society",
            level = "B2",
            words = listOf(
                word("science-society", "evidence", "bằng chứng", "/ˈevɪdəns/", "The report provides evidence of a wider trend.", MemoryLevel.FORGOT),
                word("science-society", "impact", "tác động", "/ˈɪmpækt/", "The policy could have a major impact on families.", MemoryLevel.FORGOT),
                word("science-society", "sustainable", "bền vững", "/səˈsteɪnəbəl/", "The city is investing in sustainable transport.", MemoryLevel.FORGOT),
                word("science-society", "innovation", "sự đổi mới", "/ˌɪnəˈveɪʃən/", "Innovation can change how people communicate.", MemoryLevel.FORGOT),
                word("science-society", "bias", "thiên kiến", "/ˈbaɪəs/", "The researchers checked the study for bias.", MemoryLevel.FORGOT),
                word("science-society", "regulate", "điều chỉnh; quản lý bằng quy định", "/ˈreɡjəleɪt/", "Governments regulate the use of public data.", MemoryLevel.FORGOT)
            )
        ),
        VocabularySet(
            id = "academic-writing",
            title = "Academic Writing",
            description = "C1 vocabulary for building clear, precise arguments.",
            category = "Academic",
            level = "C1",
            words = listOf(
                word("academic-writing", "coherent", "mạch lạc", "/kəʊˈhɪərənt/", "The essay presents a coherent argument.", MemoryLevel.FORGOT),
                word("academic-writing", "substantial", "đáng kể; lớn", "/səbˈstænʃəl/", "The study found a substantial difference between groups.", MemoryLevel.FORGOT),
                word("academic-writing", "hypothesis", "giả thuyết", "/haɪˈpɒθəsɪs/", "The experiment was designed to test the hypothesis.", MemoryLevel.FORGOT),
                word("academic-writing", "implication", "hệ quả; hàm ý", "/ˌɪmplɪˈkeɪʃən/", "The findings have important implications for teachers.", MemoryLevel.FORGOT),
                word("academic-writing", "synthesize", "tổng hợp", "/ˈsɪnθəsaɪz/", "The final section synthesizes the main findings.", MemoryLevel.FORGOT),
                word("academic-writing", "ambiguous", "mơ hồ; có nhiều nghĩa", "/æmˈbɪɡjuəs/", "The wording is ambiguous and needs clarification.", MemoryLevel.FORGOT)
            )
        ),
        VocabularySet(
            id = "advanced-nuance",
            title = "Advanced Nuance",
            description = "C2 expressions for precision, tone and complex ideas.",
            category = "Advanced",
            level = "C2",
            words = listOf(
                word("advanced-nuance", "meticulous", "tỉ mỉ; cẩn thận", "/məˈtɪkjələs/", "She kept meticulous records of every decision.", MemoryLevel.FORGOT),
                word("advanced-nuance", "conundrum", "vấn đề nan giải", "/kəˈnʌndrəm/", "The committee faced a difficult ethical conundrum.", MemoryLevel.FORGOT),
                word("advanced-nuance", "pragmatic", "thực tế; thực dụng", "/præɡˈmætɪk/", "They took a pragmatic approach to the negotiations.", MemoryLevel.FORGOT),
                word("advanced-nuance", "unequivocal", "rõ ràng; dứt khoát", "/ˌʌnɪˈkwɪvəkəl/", "The statement was an unequivocal rejection of the proposal.", MemoryLevel.FORGOT),
                word("advanced-nuance", "discerning", "sành sỏi; tinh tường", "/dɪˈsɜːnɪŋ/", "The discerning reader will notice the subtle contrast.", MemoryLevel.FORGOT),
                word("advanced-nuance", "pervasive", "phổ biến rộng khắp", "/pəˈveɪsɪv/", "Digital media has a pervasive influence on public debate.", MemoryLevel.FORGOT)
            )
        )
    )

    // These words are generated separately from study sets for the Daily Words feature.
    val dailyWordsByLevel = mapOf(
        "A1" to listOf(
            word("daily-a1", "clock", "đồng hồ", "/klɒk/", "The clock is on the wall.", MemoryLevel.FORGOT),
            word("daily-a1", "smile", "mỉm cười", "/smaɪl/", "She smiled at the child.", MemoryLevel.FORGOT),
            word("daily-a1", "early", "sớm", "/ˈɜːli/", "I woke up early today.", MemoryLevel.FORGOT),
            word("daily-a1", "street", "đường phố", "/striːt/", "The cafe is on the next street.", MemoryLevel.FORGOT),
            word("daily-a1", "carry", "mang; xách", "/ˈkæri/", "Can you carry this bag?", MemoryLevel.FORGOT)
        ),
        "A2" to listOf(
            word("daily-a2", "curious", "tò mò", "/ˈkjʊəriəs/", "The curious student asked another question.", MemoryLevel.FORGOT),
            word("daily-a2", "repair", "sửa chữa", "/rɪˈpeə/", "The shop can repair your phone.", MemoryLevel.FORGOT),
            word("daily-a2", "reliable", "đáng tin cậy", "/rɪˈlaɪəbəl/", "We need a reliable internet connection.", MemoryLevel.FORGOT),
            word("daily-a2", "recently", "gần đây", "/ˈriːsəntli/", "Have you seen any good films recently?", MemoryLevel.FORGOT),
            word("daily-a2", "improve", "cải thiện", "/ɪmˈpruːv/", "Practice will improve your speaking.", MemoryLevel.FORGOT)
        ),
        "B1" to listOf(
            word("daily-b1", "adapt", "thích nghi", "/əˈdæpt/", "Animals adapt to changes in their environment.", MemoryLevel.FORGOT),
            word("daily-b1", "feature", "đặc điểm; tính năng", "/ˈfiːtʃə/", "The app has a useful search feature.", MemoryLevel.FORGOT),
            word("daily-b1", "outcome", "kết quả", "/ˈaʊtkʌm/", "The outcome was better than expected.", MemoryLevel.FORGOT),
            word("daily-b1", "influence", "ảnh hưởng", "/ˈɪnfluəns/", "Music can influence our mood.", MemoryLevel.FORGOT),
            word("daily-b1", "approach", "cách tiếp cận", "/əˈprəʊtʃ/", "We need a different approach to the problem.", MemoryLevel.FORGOT)
        ),
        "B2" to listOf(
            word("daily-b2", "compelling", "thuyết phục; hấp dẫn", "/kəmˈpelɪŋ/", "The speaker made a compelling argument.", MemoryLevel.FORGOT),
            word("daily-b2", "allocate", "phân bổ", "/ˈæləkeɪt/", "The manager allocated more time to training.", MemoryLevel.FORGOT),
            word("daily-b2", "perspective", "góc nhìn; quan điểm", "/pəˈspektɪv/", "Travel gave her a new perspective.", MemoryLevel.FORGOT),
            word("daily-b2", "contribute", "đóng góp", "/kənˈtrɪbjuːt/", "Several factors contributed to the success.", MemoryLevel.FORGOT),
            word("daily-b2", "justify", "biện minh; chứng minh là hợp lý", "/ˈdʒʌstɪfaɪ/", "The data does not justify that conclusion.", MemoryLevel.FORGOT)
        ),
        "C1" to listOf(
            word("daily-c1", "alleviate", "làm giảm; xoa dịu", "/əˈliːvieɪt/", "The new policy may alleviate pressure on families.", MemoryLevel.FORGOT),
            word("daily-c1", "articulate", "diễn đạt rõ ràng", "/ɑːˈtɪkjələt/", "She gave an articulate response to the question.", MemoryLevel.FORGOT),
            word("daily-c1", "discern", "nhận ra; phân biệt", "/dɪˈsɜːn/", "It is difficult to discern the pattern at first.", MemoryLevel.FORGOT),
            word("daily-c1", "criterion", "tiêu chí", "/kraɪˈtɪəriən/", "Cost was the main criterion for the decision.", MemoryLevel.FORGOT),
            word("daily-c1", "resilient", "kiên cường; có khả năng phục hồi", "/rɪˈzɪliənt/", "The resilient community rebuilt after the storm.", MemoryLevel.FORGOT)
        ),
        "C2" to listOf(
            word("daily-c2", "inexorable", "không thể ngăn cản", "/ɪnˈeksərəbəl/", "The inexorable advance of technology changed the industry.", MemoryLevel.FORGOT),
            word("daily-c2", "juxtapose", "đặt cạnh nhau để so sánh", "/ˌdʒʌkstəˈpəʊz/", "The article juxtaposes two opposing viewpoints.", MemoryLevel.FORGOT),
            word("daily-c2", "tenuous", "mong manh; không chắc chắn", "/ˈtenjuəs/", "The connection between the events remains tenuous.", MemoryLevel.FORGOT),
            word("daily-c2", "perfunctory", "qua loa; chiếu lệ", "/pəˈfʌŋktəri/", "He offered a perfunctory apology and left.", MemoryLevel.FORGOT),
            word("daily-c2", "ubiquitous", "phổ biến ở khắp nơi", "/juːˈbɪkwɪtəs/", "Smartphones are ubiquitous in modern cities.", MemoryLevel.FORGOT)
        )
    )

    private fun word(
        setId: String,
        word: String,
        meaning: String,
        pronunciation: String,
        example: String,
        _seedLevel: MemoryLevel
    ): VocabularyItem {
        val id = "$setId-${word.lowercase().replace(" ", "-")}"
        val seededMemoryLevel = if (id in initialLearningWordIds) MemoryLevel.LEARNING else _seedLevel
        val nowMillis = System.currentTimeMillis()
        return VocabularyItem(
            id = id,
            word = word,
            meaning = meaning,
            pronunciation = pronunciation,
            exampleSentence = example,
            memoryLevel = seededMemoryLevel,
            nextReviewAtMillis = if (seededMemoryLevel == MemoryLevel.FORGOT) {
                0L
            } else {
                nowMillis + 24 * 60 * 60 * 1_000L
            },
            reviewIntervalDays = if (seededMemoryLevel == MemoryLevel.LEARNING) 1 else 0
        )
    }

}
