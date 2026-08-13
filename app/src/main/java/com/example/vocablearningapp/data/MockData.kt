package com.example.vocablearningapp.data

import com.example.vocablearningapp.domain.model.FsrsState
import com.example.vocablearningapp.domain.model.PartOfSpeech
import com.example.vocablearningapp.domain.model.VocabularyItem
import com.example.vocablearningapp.domain.model.VocabularySet

object MockData {
    private val now = System.currentTimeMillis()
    private val oneDay = 86_400_000L

    val vocabularySets = listOf(
        VocabularySet(
            id = "everyday-basics",
            title = "Everyday Basics",
            description = "Simple words for greetings, routines and familiar places.",
            category = "Everyday",
            level = "A1",
            words = listOf(
                word("everyday-basics", "hello", "xin chào", "/həˈləʊ/", "Hello, it is nice to meet you.", FsrsState.REVIEW, reviewCount = 4, stability = 12.0, dueAtMillis = now - oneDay),
                word("everyday-basics", "book", "n. cuốn sách; v. đặt chỗ trước", "/bʊk/", "n. I read an interesting book. | v. We need to book a hotel room.", FsrsState.REVIEW, partOfSpeech = PartOfSpeech.NOUN, reviewCount = 3, stability = 8.5, dueAtMillis = now + 2 * oneDay),
                word("everyday-basics", "family", "gia đình", "/ˈfæməli/", "My family lives near the city.", FsrsState.LEARNING, reviewCount = 1, stability = 1.5, dueAtMillis = now),
                word("everyday-basics", "friend", "người bạn", "/frend/", "My best friend studies with me.", FsrsState.REVIEW, reviewCount = 2, stability = 5.0, dueAtMillis = now + oneDay),
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
                word("daily-life", "daily", "hằng ngày", "/ˈdeɪli/", "I take a short walk daily.", FsrsState.REVIEW, reviewCount = 5, stability = 15.0, dueAtMillis = now - oneDay),
                word("daily-life", "notice", "n. thông báo, tờ yết thị; v. nhận thấy, chú ý", "/ˈnəʊtɪs/", "n. There was a notice on the bulletin board. | v. Did you notice his new haircut?", FsrsState.REVIEW, partOfSpeech = PartOfSpeech.NOUN, reviewCount = 3, stability = 7.0, dueAtMillis = now + oneDay),
                word("daily-life", "ambitious", "tham vọng", "/æmˈbɪʃəs/", "She is ambitious and works hard for her goals.", FsrsState.LEARNING, reviewCount = 2, stability = 2.5, dueAtMillis = now),
                word("daily-life", "commute", "v. đi lại giữa nhà và nơi làm việc; n. quãng đường đi làm", "/kəˈmjuːt/", "v. She commutes to work by subway. | n. My daily commute takes thirty minutes.", FsrsState.REVIEW, partOfSpeech = PartOfSpeech.VERB, reviewCount = 4, stability = 10.0, dueAtMillis = now + 3 * oneDay),
                word("daily-life", "grocery", "thực phẩm; hàng tạp hóa", "/ˈɡrəʊsəri/", "I need to buy some groceries after work.", FsrsState.NEW),
                word("daily-life", "habit", "thói quen", "/ˈhæbɪt/", "Reading before bed is a healthy habit.", FsrsState.NEW),
                word("daily-life", "schedule", "n. lịch trình; v. lên lịch", "/ˈʃedjuːl/", "n. Let me check my schedule first. | v. We scheduled a meeting for Monday.", FsrsState.NEW, partOfSpeech = PartOfSpeech.NOUN),
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
                word("travel-basics", "local", "adj. địa phương; n. người địa phương", "/ˈləʊkəl/", "adj. We enjoyed the local cuisine. | n. We asked a local for restaurant advice.", FsrsState.NEW, partOfSpeech = PartOfSpeech.ADJECTIVE),
                word("travel-basics", "crowded", "đông đúc", "/ˈkraʊdɪd/", "The market is crowded at weekends.", FsrsState.NEW)
            )
        ),
        VocabularySet(
            id = "school",
            title = "School & Studies",
            description = "Useful vocabulary for classes, assignments and academic life.",
            category = "Education",
            level = "B1",
            words = listOf(
                word("school", "match", "n. trận đấu; v. nối, hợp với", "/mætʃ/", "n. The football match starts at six. | v. Your tie matches your shirt.", FsrsState.REVIEW, partOfSpeech = PartOfSpeech.NOUN, reviewCount = 3, stability = 9.0, dueAtMillis = now - oneDay),
                word("school", "assignment", "bài tập được giao", "/əˈsaɪnmənt/", "The assignment is due on Friday.", FsrsState.REVIEW, reviewCount = 2, stability = 6.0, dueAtMillis = now + 2 * oneDay),
                word("school", "curriculum", "chương trình học", "/kəˈrɪkjələm/", "The new curriculum includes more practical lessons.", FsrsState.LEARNING, reviewCount = 1, stability = 2.0, dueAtMillis = now),
                word("school", "deadline", "hạn chót", "/ˈdedlaɪn/", "We must finish the project before the deadline.", FsrsState.NEW),
                word("school", "concentrate", "tập trung", "/ˈkɒnsəntreɪt/", "It is easier to concentrate in a quiet room.", FsrsState.NEW),
                word("school", "lecture", "n. bài giảng; v. giảng bài", "/ˈlektʃə/", "n. Today's lecture was about climate change. | v. He lectured on medieval history.", FsrsState.NEW, partOfSpeech = PartOfSpeech.NOUN),
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
                word("work-career", "lead", "v. dẫn dắt; n. sự dẫn đầu, dây dẫn", "/liːd/", "v. She will lead the design team. | n. Our team took the lead in the second half.", FsrsState.REVIEW, partOfSpeech = PartOfSpeech.VERB, reviewCount = 4, stability = 14.0, dueAtMillis = now - oneDay),
                word("work-career", "negotiate", "đàm phán; thương lượng", "/nɪˈɡəʊʃieɪt/", "We need to negotiate a fair contract.", FsrsState.REVIEW, reviewCount = 3, stability = 8.0, dueAtMillis = now + 4 * oneDay),
                word("work-career", "promotion", "sự thăng chức", "/prəˈməʊʃən/", "She earned a promotion after leading the project.", FsrsState.LEARNING, reviewCount = 1, stability = 1.8, dueAtMillis = now),
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
                word("science-society", "impact", "n. tác động; v. ảnh hưởng", "/ˈɪmpækt/", "n. The policy could have a major impact on families. | v. Lower interest rates impact housing prices.", FsrsState.NEW, partOfSpeech = PartOfSpeech.NOUN),
                word("science-society", "sustainable", "bền vững", "/səˈsteɪnəbəl/", "The city is investing in sustainable transport.", FsrsState.NEW),
                word("science-society", "innovation", "sự đổi mới", "/ˌɪnəˈveɪʃən/", "Innovation can change how people communicate.", FsrsState.NEW),
                word("science-society", "bias", "thiên kiến", "/ˈbaɪəs/", "The researchers checked the study for bias.", FsrsState.NEW),
                word("science-society", "regulate", "điều chỉnh; quản lý bằng quy định", "/ˈreɡjəleɪt/", "Governments regulate the use of public data.", FsrsState.NEW)
            )
        )
    )

    val dailyWordsByLevel = mapOf(
        "A1" to listOf(
            word("daily-a1", "clock", "đồng hồ", "/klɒk/", "The clock is on the wall.", FsrsState.NEW),
            word("daily-a1", "smile", "v. mỉm cười; n. nụ cười", "/smaɪl/", "v. She smiled at the child. | n. He has a warm smile.", FsrsState.NEW, partOfSpeech = PartOfSpeech.VERB),
            word("daily-a1", "early", "sớm", "/ˈɜːli/", "I woke up early today.", FsrsState.NEW),
            word("daily-a1", "street", "đường phố", "/striːt/", "The cafe is on the next street.", FsrsState.NEW),
            word("daily-a1", "carry", "mang; xách", "/ˈkæri/", "Can you carry this bag?", FsrsState.NEW)
        ),
        "A2" to listOf(
            word("daily-a2", "notice", "n. thông báo; v. nhận thấy", "/ˈnəʊtɪs/", "n. There is a notice on the door. | v. Did you notice the sign?", FsrsState.NEW, partOfSpeech = PartOfSpeech.NOUN),
            word("daily-a2", "repair", "v. sửa chữa; n. sự sửa chữa", "/rɪˈpeə/", "v. The shop can repair your phone. | n. The bike is in for repair.", FsrsState.NEW, partOfSpeech = PartOfSpeech.VERB),
            word("daily-a2", "reliable", "đáng tin cậy", "/rɪˈlaɪəbəl/", "We need a reliable internet connection.", FsrsState.NEW),
            word("daily-a2", "improve", "cải thiện", "/ɪmˈpruːv/", "Practice will improve your speaking.", FsrsState.NEW)
        ),
        "B1" to listOf(
            word("daily-b1", "feature", "n. đặc điểm; v. có sự tham gia của", "/ˈfiːtʃə/", "n. The app has a useful search feature. | v. The film features top actors.", FsrsState.NEW, partOfSpeech = PartOfSpeech.NOUN),
            word("daily-b1", "approach", "n. cách tiếp cận; v. tiếp cận", "/əˈprəʊtʃ/", "n. We need a new approach. | v. The train is approaching the station.", FsrsState.NEW, partOfSpeech = PartOfSpeech.NOUN),
            word("daily-b1", "benefit", "n. lợi ích; v. được hưởng lợi", "/ˈbenɪfɪt/", "n. Regular exercise has many benefits. | v. Patients will benefit from treatment.", FsrsState.NEW, partOfSpeech = PartOfSpeech.NOUN)
        ),
        "B2" to listOf(
            word("daily-b2", "perspective", "góc nhìn; quan điểm", "/pəˈspektɪv/", "Travel gave her a new perspective.", FsrsState.NEW),
            word("daily-b2", "enhance", "nâng cao; tăng cường", "/ɪnˈhɑːns/", "The update will enhance the user experience.", FsrsState.NEW)
        )
    )

    private fun word(
        setId: String,
        word: String,
        meaning: String,
        pronunciation: String,
        exampleSentence: String,
        fsrsState: FsrsState,
        partOfSpeech: PartOfSpeech = PartOfSpeech.NOUN,
        reviewCount: Int = 0,
        stability: Double = 0.0,
        dueAtMillis: Long = 0L
    ): VocabularyItem {
        return VocabularyItem(
            id = "$setId-$word",
            word = word,
            meaning = meaning,
            pronunciation = pronunciation,
            partOfSpeech = partOfSpeech,
            exampleSentence = exampleSentence,
            fsrsState = fsrsState,
            reviewCount = reviewCount,
            stability = stability,
            dueAtMillis = dueAtMillis
        )
    }
}
