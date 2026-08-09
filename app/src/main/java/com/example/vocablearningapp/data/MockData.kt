package com.example.vocablearningapp.data

import com.example.vocablearningapp.domain.model.MemoryLevel
import com.example.vocablearningapp.domain.model.VocabularyItem
import com.example.vocablearningapp.domain.model.VocabularySet

object MockData {
    val vocabularySets = listOf(
        VocabularySet(
            id = "everyday-basics",
            title = "Everyday Basics",
            description = "Simple words for greetings, routines and familiar places.",
            category = "Everyday",
            level = "A1",
            words = listOf(
                word("everyday-basics", "hello", "a greeting used when meeting someone", "/həˈləʊ/", "Hello, it is nice to meet you.", MemoryLevel.REMEMBERED),
                word("everyday-basics", "family", "people who are related to you", "/ˈfæməli/", "My family lives near the city.", MemoryLevel.REMEMBERED),
                word("everyday-basics", "friend", "a person you know and like", "/frend/", "My best friend studies with me.", MemoryLevel.SOMEWHAT_REMEMBERED),
                word("everyday-basics", "morning", "the first part of the day", "/ˈmɔːnɪŋ/", "I read the news in the morning.", MemoryLevel.REMEMBERED),
                word("everyday-basics", "near", "a short distance away", "/nɪə/", "The library is near my home.", MemoryLevel.NOT_REMEMBERED),
                word("everyday-basics", "usually", "in most situations or at most times", "/ˈjuːʒuəli/", "I usually walk to class.", MemoryLevel.SOMEWHAT_REMEMBERED)
            )
        ),
        VocabularySet(
            id = "home-family",
            title = "Home & Family",
            description = "Useful A1 vocabulary for talking about home and the people around you.",
            category = "Everyday",
            level = "A1",
            words = listOf(
                word("home-family", "kitchen", "a room where food is prepared", "/ˈkɪtʃən/", "The kitchen is next to the dining room.", MemoryLevel.REMEMBERED),
                word("home-family", "bedroom", "a room used for sleeping", "/ˈbedruːm/", "My bedroom has a large window.", MemoryLevel.SOMEWHAT_REMEMBERED),
                word("home-family", "parent", "a mother or father", "/ˈpeərənt/", "One parent came to the school meeting.", MemoryLevel.NOT_REMEMBERED),
                word("home-family", "garden", "an area where plants are grown", "/ˈɡɑːdən/", "We grow herbs in the garden.", MemoryLevel.SOMEWHAT_REMEMBERED),
                word("home-family", "quiet", "making very little sound", "/ˈkwaɪət/", "This is a quiet place to read.", MemoryLevel.REMEMBERED),
                word("home-family", "borrow", "to take and use something before returning it", "/ˈbɒrəʊ/", "Can I borrow your umbrella?", MemoryLevel.NOT_REMEMBERED)
            )
        ),
        VocabularySet(
            id = "daily-life",
            title = "Daily Life",
            description = "Familiar vocabulary for everyday conversations and routines.",
            category = "Everyday",
            level = "A2",
            words = listOf(
                word("daily-life", "daily", "happening every day", "/ˈdeɪli/", "I take a short walk daily.", MemoryLevel.REMEMBERED),
                word("daily-life", "ambitious", "having a strong desire to succeed", "/æmˈbɪʃəs/", "She is ambitious and works hard for her goals.", MemoryLevel.SOMEWHAT_REMEMBERED),
                word("daily-life", "commute", "to travel regularly between home and work or school", "/kəˈmjuːt/", "My commute takes about thirty minutes.", MemoryLevel.SOMEWHAT_REMEMBERED),
                word("daily-life", "grocery", "food and other goods sold by a grocery store", "/ˈɡrəʊsəri/", "I need to buy some groceries after work.", MemoryLevel.NOT_REMEMBERED),
                word("daily-life", "habit", "something you do regularly", "/ˈhæbɪt/", "Reading before bed is a healthy habit.", MemoryLevel.REMEMBERED),
                word("daily-life", "schedule", "a plan that shows when things will happen", "/ˈʃedjuːl/", "Let me check my schedule first.", MemoryLevel.SOMEWHAT_REMEMBERED),
                word("daily-life", "errand", "a short trip to do a small task", "/ˈerənd/", "I have a few errands to run this afternoon.", MemoryLevel.NOT_REMEMBERED),
                word("daily-life", "exhausted", "extremely tired", "/ɪɡˈzɔːstɪd/", "I felt exhausted after the long trip.", MemoryLevel.REMEMBERED)
            )
        ),
        VocabularySet(
            id = "travel-basics",
            title = "Travel Basics",
            description = "Practical A2 words for planning trips and getting around.",
            category = "Travel",
            level = "A2",
            words = listOf(
                word("travel-basics", "journey", "an act of travelling from one place to another", "/ˈdʒɜːni/", "The journey took three hours by train.", MemoryLevel.SOMEWHAT_REMEMBERED),
                word("travel-basics", "luggage", "bags and cases used when travelling", "/ˈlʌɡɪdʒ/", "Please keep your luggage with you.", MemoryLevel.NOT_REMEMBERED),
                word("travel-basics", "ticket", "a document that allows you to travel or enter a place", "/ˈtɪkɪt/", "I booked my train ticket online.", MemoryLevel.REMEMBERED),
                word("travel-basics", "platform", "the raised area beside a railway track", "/ˈplætfɔːm/", "The train leaves from platform four.", MemoryLevel.SOMEWHAT_REMEMBERED),
                word("travel-basics", "local", "belonging to or connected with a particular area", "/ˈləʊkəl/", "We asked a local for restaurant advice.", MemoryLevel.REMEMBERED),
                word("travel-basics", "crowded", "full of people", "/ˈkraʊdɪd/", "The market is crowded at weekends.", MemoryLevel.NOT_REMEMBERED)
            )
        ),
        VocabularySet(
            id = "school",
            title = "School",
            description = "Useful vocabulary for classes, assignments and academic life.",
            category = "Education",
            level = "B1",
            words = listOf(
                word("school", "assignment", "a piece of work given to a student", "/əˈsaɪnmənt/", "The assignment is due on Friday.", MemoryLevel.SOMEWHAT_REMEMBERED),
                word("school", "curriculum", "the subjects taught in a school or course", "/kəˈrɪkjələm/", "The new curriculum includes more practical lessons.", MemoryLevel.NOT_REMEMBERED),
                word("school", "deadline", "the latest time by which something must be done", "/ˈdedlaɪn/", "We must finish the project before the deadline.", MemoryLevel.REMEMBERED),
                word("school", "concentrate", "to give all your attention to something", "/ˈkɒnsəntreɪt/", "It is easier to concentrate in a quiet room.", MemoryLevel.SOMEWHAT_REMEMBERED),
                word("school", "lecture", "a talk given to teach people about a subject", "/ˈlektʃə/", "Today's lecture was about climate change.", MemoryLevel.REMEMBERED),
                word("school", "scholarship", "money given to support a student's education", "/ˈskɒləʃɪp/", "She received a scholarship to study abroad.", MemoryLevel.NOT_REMEMBERED),
                word("school", "revise", "to study again for an examination", "/rɪˈvaɪz/", "I need to revise for tomorrow's exam.", MemoryLevel.REMEMBERED)
            )
        ),
        VocabularySet(
            id = "music-kpop",
            title = "Music & K-pop",
            description = "Words for talking about songs, stages and performances.",
            category = "Interests",
            level = "B1",
            words = listOf(
                word("music-kpop", "melody", "a sequence of musical notes that forms a tune", "/ˈmelədi/", "The melody stayed in my head all day.", MemoryLevel.REMEMBERED),
                word("music-kpop", "choreography", "the planned movements in a dance or performance", "/ˌkɒriˈɒɡrəfi/", "The choreography for this song is difficult.", MemoryLevel.REMEMBERED),
                word("music-kpop", "lyrics", "the words of a song", "/ˈlɪrɪks/", "I looked up the lyrics after hearing the song.", MemoryLevel.SOMEWHAT_REMEMBERED),
                word("music-kpop", "catchy", "pleasant and easy to remember", "/ˈkætʃi/", "That chorus is incredibly catchy.", MemoryLevel.REMEMBERED),
                word("music-kpop", "rehearsal", "a practice for a performance", "/rɪˈhɜːsəl/", "The dancers have rehearsal every evening.", MemoryLevel.SOMEWHAT_REMEMBERED),
                word("music-kpop", "performance", "an act of presenting music, dance or drama", "/pəˈfɔːməns/", "Their live performance was unforgettable.", MemoryLevel.REMEMBERED)
            )
        ),
        VocabularySet(
            id = "work-career",
            title = "Work & Career",
            description = "B2 vocabulary for professional conversations and workplace goals.",
            category = "Professional",
            level = "B2",
            words = listOf(
                word("work-career", "negotiate", "to discuss something in order to reach an agreement", "/nɪˈɡəʊʃieɪt/", "We need to negotiate a fair contract.", MemoryLevel.SOMEWHAT_REMEMBERED),
                word("work-career", "deadline", "the final time allowed to complete something", "/ˈdedlaɪn/", "The team met the deadline despite the changes.", MemoryLevel.REMEMBERED),
                word("work-career", "promotion", "a move to a more important job", "/prəˈməʊʃən/", "She earned a promotion after leading the project.", MemoryLevel.NOT_REMEMBERED),
                word("work-career", "efficient", "working well without wasting time or resources", "/ɪˈfɪʃənt/", "The new process is much more efficient.", MemoryLevel.SOMEWHAT_REMEMBERED),
                word("work-career", "initiative", "the ability to make decisions and act independently", "/ɪˈnɪʃətɪv/", "He showed initiative by solving the issue early.", MemoryLevel.NOT_REMEMBERED),
                word("work-career", "collaborate", "to work with others on a shared task", "/kəˈlæbəreɪt/", "Our teams collaborate across three offices.", MemoryLevel.REMEMBERED)
            )
        ),
        VocabularySet(
            id = "science-society",
            title = "Science & Society",
            description = "B2 terms for discussing research, change and public life.",
            category = "Society",
            level = "B2",
            words = listOf(
                word("science-society", "evidence", "facts or information that show something is true", "/ˈevɪdəns/", "The report provides evidence of a wider trend.", MemoryLevel.REMEMBERED),
                word("science-society", "impact", "a strong effect on someone or something", "/ˈɪmpækt/", "The policy could have a major impact on families.", MemoryLevel.SOMEWHAT_REMEMBERED),
                word("science-society", "sustainable", "able to continue without harming the future", "/səˈsteɪnəbəl/", "The city is investing in sustainable transport.", MemoryLevel.NOT_REMEMBERED),
                word("science-society", "innovation", "a new idea, method or product", "/ˌɪnəˈveɪʃən/", "Innovation can change how people communicate.", MemoryLevel.SOMEWHAT_REMEMBERED),
                word("science-society", "bias", "an unfair preference for or against someone or something", "/ˈbaɪəs/", "The researchers checked the study for bias.", MemoryLevel.NOT_REMEMBERED),
                word("science-society", "regulate", "to control an activity with rules", "/ˈreɡjəleɪt/", "Governments regulate the use of public data.", MemoryLevel.REMEMBERED)
            )
        ),
        VocabularySet(
            id = "academic-writing",
            title = "Academic Writing",
            description = "C1 vocabulary for building clear, precise arguments.",
            category = "Academic",
            level = "C1",
            words = listOf(
                word("academic-writing", "coherent", "logical and well organised", "/kəʊˈhɪərənt/", "The essay presents a coherent argument.", MemoryLevel.SOMEWHAT_REMEMBERED),
                word("academic-writing", "substantial", "large in amount or important in value", "/səbˈstænʃəl/", "The study found a substantial difference between groups.", MemoryLevel.NOT_REMEMBERED),
                word("academic-writing", "hypothesis", "an idea that can be tested by research", "/haɪˈpɒθəsɪs/", "The experiment was designed to test the hypothesis.", MemoryLevel.SOMEWHAT_REMEMBERED),
                word("academic-writing", "implication", "a possible result or consequence", "/ˌɪmplɪˈkeɪʃən/", "The findings have important implications for teachers.", MemoryLevel.NOT_REMEMBERED),
                word("academic-writing", "synthesize", "to combine ideas into a connected whole", "/ˈsɪnθəsaɪz/", "The final section synthesizes the main findings.", MemoryLevel.REMEMBERED),
                word("academic-writing", "ambiguous", "having more than one possible meaning", "/æmˈbɪɡjuəs/", "The wording is ambiguous and needs clarification.", MemoryLevel.SOMEWHAT_REMEMBERED)
            )
        ),
        VocabularySet(
            id = "advanced-nuance",
            title = "Advanced Nuance",
            description = "C2 expressions for precision, tone and complex ideas.",
            category = "Advanced",
            level = "C2",
            words = listOf(
                word("advanced-nuance", "meticulous", "very careful and precise", "/məˈtɪkjələs/", "She kept meticulous records of every decision.", MemoryLevel.SOMEWHAT_REMEMBERED),
                word("advanced-nuance", "conundrum", "a confusing and difficult problem", "/kəˈnʌndrəm/", "The committee faced a difficult ethical conundrum.", MemoryLevel.NOT_REMEMBERED),
                word("advanced-nuance", "pragmatic", "solving problems in a practical way", "/præɡˈmætɪk/", "They took a pragmatic approach to the negotiations.", MemoryLevel.SOMEWHAT_REMEMBERED),
                word("advanced-nuance", "unequivocal", "clear and leaving no doubt", "/ˌʌnɪˈkwɪvəkəl/", "The statement was an unequivocal rejection of the proposal.", MemoryLevel.NOT_REMEMBERED),
                word("advanced-nuance", "discerning", "able to judge quality and differences well", "/dɪˈsɜːnɪŋ/", "The discerning reader will notice the subtle contrast.", MemoryLevel.REMEMBERED),
                word("advanced-nuance", "pervasive", "present throughout a place or group", "/pəˈveɪsɪv/", "Digital media has a pervasive influence on public debate.", MemoryLevel.SOMEWHAT_REMEMBERED)
            )
        )
    )

    private fun word(
        setId: String,
        word: String,
        meaning: String,
        pronunciation: String,
        example: String,
        memoryLevel: MemoryLevel
    ) = VocabularyItem(
        id = "$setId-${word.lowercase().replace(" ", "-")}",
        word = word,
        meaning = meaning,
        pronunciation = pronunciation,
        exampleSentence = example,
        memoryLevel = memoryLevel,
        nextReviewDate = if (memoryLevel == MemoryLevel.NOT_REMEMBERED) "Today" else "Tomorrow"
    )
}
