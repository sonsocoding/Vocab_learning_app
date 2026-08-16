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
                word("everyday-basics", "hello", "xin chào", "/həˈləʊ/", "Hello, it is nice to meet you.", FsrsState.NEW),
                word("everyday-basics", "book", "n. cuốn sách; v. đặt chỗ trước", "/bʊk/", "n. I read an interesting book. | v. We need to book a hotel room.", FsrsState.NEW, partOfSpeech = PartOfSpeech.NOUN),
                word("everyday-basics", "family", "gia đình", "/ˈfæməli/", "My family lives near the city.", FsrsState.LEARNING, reviewCount = 1, stability = 1.5, dueAtMillis = now),
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
                word("daily-life", "notice", "n. thông báo, tờ yết thị; v. nhận thấy, chú ý", "/ˈnəʊtɪs/", "n. There was a notice on the bulletin board. | v. Did you notice his new haircut?", FsrsState.NEW, partOfSpeech = PartOfSpeech.NOUN),
                word("daily-life", "ambitious", "tham vọng", "/æmˈbɪʃəs/", "She is ambitious and works hard for her goals.", FsrsState.LEARNING, reviewCount = 2, stability = 2.5, dueAtMillis = now),
                word("daily-life", "commute", "v. đi lại giữa nhà và nơi làm việc; n. quãng đường đi làm", "/kəˈmjuːt/", "v. She commutes to work by subway. | n. My daily commute takes thirty minutes.", FsrsState.NEW, partOfSpeech = PartOfSpeech.VERB),
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
                word("school", "match", "n. trận đấu; v. nối, hợp với", "/mætʃ/", "n. The football match starts at six. | v. Your tie matches your shirt.", FsrsState.NEW, partOfSpeech = PartOfSpeech.NOUN),
                word("school", "assignment", "bài tập được giao", "/əˈsaɪnmənt/", "The assignment is due on Friday.", FsrsState.NEW),
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
                word("work-career", "lead", "v. dẫn dắt; n. sự dẫn đầu, dây dẫn", "/liːd/", "v. She will lead the design team. | n. Our team took the lead in the second half.", FsrsState.NEW, partOfSpeech = PartOfSpeech.VERB),
                word("work-career", "negotiate", "đàm phán; thương lượng", "/nɪˈɡəʊʃieɪt/", "We need to negotiate a fair contract.", FsrsState.NEW),
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
            word("daily-a1", "water", "nước uống", "/ˈwɔːtər/", "Drink a glass of water every morning.", FsrsState.NEW),
            word("daily-a1", "family", "gia đình", "/ˈfæməli/", "My family lives in a quiet town.", FsrsState.NEW),
            word("daily-a1", "school", "trường học", "/skuːl/", "She walks to school with her friends.", FsrsState.NEW),
            word("daily-a1", "happy", "vui vẻ; hạnh phúc", "/ˈhæpi/", "They were very happy to see us.", FsrsState.NEW, partOfSpeech = PartOfSpeech.ADJECTIVE),
            word("daily-a1", "friend", "người bạn", "/frend/", "My best friend helps me study English.", FsrsState.NEW),
            word("daily-a1", "book", "n. cuốn sách; v. đặt chỗ", "/bʊk/", "n. I read an interesting book last night. | v. We need to book a hotel.", FsrsState.NEW),
            word("daily-a1", "music", "âm nhạc", "/ˈmjuːzɪk/", "Listening to music relaxes my mind.", FsrsState.NEW),
            word("daily-a1", "today", "hôm nay", "/təˈdeɪ/", "What are your plans for today?", FsrsState.NEW),
            word("daily-a1", "apple", "quả táo", "/ˈæpəl/", "An apple a day keeps the doctor away.", FsrsState.NEW),
            word("daily-a1", "weather", "thời tiết", "/ˈweðər/", "The weather is bright and sunny today.", FsrsState.NEW)
        ),
        "A2" to listOf(
            word("daily-a2", "notice", "n. thông báo; v. nhận thấy", "/ˈnəʊtɪs/", "n. There is a notice on the door. | v. Did you notice the sign?", FsrsState.NEW),
            word("daily-a2", "repair", "v. sửa chữa; n. sự sửa chữa", "/rɪˈpeər/", "v. The shop can repair your phone. | n. The bike is in for repair.", FsrsState.NEW, partOfSpeech = PartOfSpeech.VERB),
            word("daily-a2", "reliable", "đáng tin cậy", "/rɪˈlaɪəbəl/", "We need a reliable internet connection.", FsrsState.NEW, partOfSpeech = PartOfSpeech.ADJECTIVE),
            word("daily-a2", "improve", "cải thiện; nâng cao", "/ɪmˈpruːv/", "Daily practice will improve your speaking.", FsrsState.NEW, partOfSpeech = PartOfSpeech.VERB),
            word("daily-a2", "commute", "v. đi lại làm việc; n. đường đi làm", "/kəˈmjuːt/", "v. She commutes to work by subway. | n. My daily commute takes 30 minutes.", FsrsState.NEW, partOfSpeech = PartOfSpeech.VERB),
            word("daily-a2", "habit", "thói quen", "/ˈhæbɪt/", "Reading before bed is a healthy habit.", FsrsState.NEW),
            word("daily-a2", "schedule", "n. lịch trình; v. lên lịch", "/ˈʃedjuːl/", "n. Let me check my schedule first. | v. We scheduled a meeting.", FsrsState.NEW),
            word("daily-a2", "errand", "việc vặt cần làm", "/ˈerənd/", "I have a few errands to run this afternoon.", FsrsState.NEW),
            word("daily-a2", "exhausted", "kiệt sức", "/ɪɡˈzɔːstɪd/", "I felt exhausted after running five miles.", FsrsState.NEW, partOfSpeech = PartOfSpeech.ADJECTIVE),
            word("daily-a2", "journey", "hành trình", "/ˈdʒɜːni/", "The train journey across the valley was beautiful.", FsrsState.NEW)
        ),
        "B1" to listOf(
            word("daily-b1", "feature", "n. đặc điểm, tính năng; v. có sự tham gia của", "/ˈfiːtʃər/", "n. The app has a useful search feature. | v. The film features top actors.", FsrsState.NEW),
            word("daily-b1", "approach", "n. cách tiếp cận; v. tiếp cận", "/əˈprəʊtʃ/", "n. We need a new approach. | v. The train is approaching the station.", FsrsState.NEW),
            word("daily-b1", "benefit", "n. lợi ích; v. hưởng lợi", "/ˈbenɪfɪt/", "n. Regular exercise has many benefits. | v. Patients benefit from treatment.", FsrsState.NEW),
            word("daily-b1", "assignment", "bài tập được giao", "/əˈsaɪnmənt/", "The history assignment is due next Tuesday.", FsrsState.NEW),
            word("daily-b1", "concentrate", "tập trung", "/ˈkɒnsəntreɪt/", "Turn off the TV so you can concentrate.", FsrsState.NEW, partOfSpeech = PartOfSpeech.VERB),
            word("daily-b1", "deadline", "hạn chót", "/ˈdedlaɪn/", "Our team met the project deadline.", FsrsState.NEW),
            word("daily-b1", "curriculum", "chương trình học", "/kəˈrɪkjələm/", "The school updated its science curriculum.", FsrsState.NEW),
            word("daily-b1", "recommend", "giới thiệu; tiến cử", "/ˌrekəˈmend/", "Can you recommend a good book?", FsrsState.NEW, partOfSpeech = PartOfSpeech.VERB),
            word("daily-b1", "solution", "giải pháp", "/səˈluːʃən/", "They found a practical solution to the conflict.", FsrsState.NEW),
            word("daily-b1", "opportunity", "cơ hội", "/ˌɒpəˈtjuːnəti/", "This job offer is a great career opportunity.", FsrsState.NEW)
        ),
        "B2" to listOf(
            word("daily-b2", "perspective", "góc nhìn; quan điểm", "/pəˈspektɪv/", "Travel gave her a fresh perspective on life.", FsrsState.NEW),
            word("daily-b2", "enhance", "nâng cao; tăng cường", "/ɪnˈhɑːns/", "The update will enhance user experience.", FsrsState.NEW, partOfSpeech = PartOfSpeech.VERB),
            word("daily-b2", "negotiate", "đàm phán; thương lượng", "/nɪˈɡəʊʃieɪt/", "The two companies negotiated a fair contract.", FsrsState.NEW, partOfSpeech = PartOfSpeech.VERB),
            word("daily-b2", "promotion", "sự thăng chức; sự quảng bá", "/prəˈməʊʃən/", "Her hard work earned her a promotion.", FsrsState.NEW),
            word("daily-b2", "initiative", "tính chủ động; sáng kiến", "/ɪˈnɪʃətɪv/", "He showed initiative by solving the issue early.", FsrsState.NEW),
            word("daily-b2", "collaborate", "hợp tác", "/kəˈlæbəreɪt/", "Designers and engineers collaborate closely.", FsrsState.NEW, partOfSpeech = PartOfSpeech.VERB),
            word("daily-b2", "sustainable", "bền vững", "/səˈsteɪnəbəl/", "Renewable energy is vital for sustainable growth.", FsrsState.NEW, partOfSpeech = PartOfSpeech.ADJECTIVE),
            word("daily-b2", "innovation", "sự đổi mới; sáng tạo", "/ˌɪnəˈveɪʃən/", "Constant innovation keeps the firm competitive.", FsrsState.NEW),
            word("daily-b2", "evidence", "bằng chứng", "/ˈevɪdəns/", "The report provides clear evidence for the claim.", FsrsState.NEW),
            word("daily-b2", "regulate", "điều chỉnh; quản lý", "/ˈreɡjuːleɪt/", "Governments regulate public transport rates.", FsrsState.NEW, partOfSpeech = PartOfSpeech.VERB)
        ),
        "C1" to listOf(
            word("daily-c1", "articulate", "diễn đạt lưu loát; rõ ràng", "/ɑːˈtɪkjəleɪt/", "She is an articulate speaker who conveys ideas well.", FsrsState.NEW, partOfSpeech = PartOfSpeech.ADJECTIVE),
            word("daily-c1", "comprehensive", "toàn diện; bao quát", "/ˌkɒmprɪˈhensɪv/", "The report gives a comprehensive review.", FsrsState.NEW, partOfSpeech = PartOfSpeech.ADJECTIVE),
            word("daily-c1", "discrepancy", "sự khác biệt; sự bất đồng", "/dɪˈskrepənsi/", "There was a discrepancy between financial records.", FsrsState.NEW),
            word("daily-c1", "imperative", "bắt buộc; cấp thiết", "/ɪmˈperətɪv/", "It is imperative that we act immediately.", FsrsState.NEW, partOfSpeech = PartOfSpeech.ADJECTIVE),
            word("daily-c1", "prevalent", "phổ biến rộng rãi", "/ˈprevələnt/", "Flu is prevalent during winter months.", FsrsState.NEW, partOfSpeech = PartOfSpeech.ADJECTIVE),
            word("daily-c1", "resilient", "kiên cường; phục hồi nhanh", "/rɪˈzɪliənt/", "The local economy proved resilient.", FsrsState.NEW, partOfSpeech = PartOfSpeech.ADJECTIVE),
            word("daily-c1", "scrutinize", "xem xét kỹ lưỡng", "/ˈskruːtənaɪz/", "Auditors scrutinize every transaction.", FsrsState.NEW, partOfSpeech = PartOfSpeech.VERB),
            word("daily-c1", "unprecedented", "chưa từng có", "/ʌnˈpresɪdentɪd/", "The project achieved unprecedented success.", FsrsState.NEW, partOfSpeech = PartOfSpeech.ADJECTIVE),
            word("daily-c1", "coherent", "mạch lạc; chặt chẽ", "/kəʊˈhɪərənt/", "He gave a coherent presentation to investors.", FsrsState.NEW, partOfSpeech = PartOfSpeech.ADJECTIVE),
            word("daily-c1", "feasible", "khả thi", "/ˈfiːzəbəl/", "The proposed plan seems highly feasible.", FsrsState.NEW, partOfSpeech = PartOfSpeech.ADJECTIVE)
        ),
        "C2" to listOf(
            word("daily-c2", "acumen", "sự nhạy bén; sự sắc sảo", "/ˈækjuːmən/", "His business acumen guided the startup.", FsrsState.NEW),
            word("daily-c2", "benchmark", "tiêu chuẩn đối sánh", "/ˈbentʃmɑːk/", "This paper serves as a benchmark for researchers.", FsrsState.NEW),
            word("daily-c2", "ephemeral", "phù du; ngắn ngủi", "/ɪˈfemərəl/", "Fame can be ephemeral, but craft endures.", FsrsState.NEW, partOfSpeech = PartOfSpeech.ADJECTIVE),
            word("daily-c2", "ubiquitous", "có mặt ở khắp nơi", "/juːˈbɪkwɪtəs/", "Smartphones are ubiquitous in modern life.", FsrsState.NEW, partOfSpeech = PartOfSpeech.ADJECTIVE),
            word("daily-c2", "quintessential", "tinh túy; điển hình", "/ˌkwɪntɪˈsenʃəl/", "He is the quintessential scholar.", FsrsState.NEW, partOfSpeech = PartOfSpeech.ADJECTIVE),
            word("daily-c2", "meticulous", "tỉ mỉ; cẩn thận", "/məˈtɪkjələs/", "Her meticulous design avoided errors.", FsrsState.NEW, partOfSpeech = PartOfSpeech.ADJECTIVE),
            word("daily-c2", "paradigm", "mô hình; hình mẫu", "/ˈpærədaɪm/", "The discovery created a new scientific paradigm.", FsrsState.NEW),
            word("daily-c2", "pragmatic", "thực tế; thực dụng", "/præɡˈmætɪk/", "We need a pragmatic strategy today.", FsrsState.NEW, partOfSpeech = PartOfSpeech.ADJECTIVE),
            word("daily-c2", "synergy", "sự cộng hưởng", "/ˈsɪnədʒi/", "The deal created synergy across teams.", FsrsState.NEW),
            word("daily-c2", "juxtapose", "đặt cạnh nhau để so sánh", "/ˌdʒʌkstəˈpəʊz/", "The exhibit juxtaposes classic and contemporary works.", FsrsState.NEW, partOfSpeech = PartOfSpeech.VERB)
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
