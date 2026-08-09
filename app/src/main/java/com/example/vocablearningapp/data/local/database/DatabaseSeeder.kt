package com.example.vocablearningapp.data.local.database

import com.example.vocablearningapp.data.local.dao.DeckDao
import com.example.vocablearningapp.data.local.dao.LevelDao
import com.example.vocablearningapp.data.local.dao.TopicDao
import com.example.vocablearningapp.data.local.dao.VocabularyDao
import com.example.vocablearningapp.data.local.entity.DeckEntity
import com.example.vocablearningapp.data.local.entity.LevelEntity
import com.example.vocablearningapp.data.local.entity.TopicEntity
import com.example.vocablearningapp.data.local.entity.VocabularyEntity

object DatabaseSeeder {

    suspend fun seedDatabase(
        levelDao: LevelDao,
        topicDao: TopicDao,
        deckDao: DeckDao,
        vocabularyDao: VocabularyDao
    ) {
        // 1. Seed Levels
        val levels = listOf(
            LevelEntity("A1", "A1", "A1 - Cơ bản", "Từ thông dụng, giao tiếp hàng ngày cơ bản", 1),
            LevelEntity("A2", "A2", "A2 - Sơ trung cấp", "Mở rộng giao tiếp cá nhân và sinh hoạt", 2),
            LevelEntity("B1", "B1", "B1 - Trung cấp", "Từ vựng theo ngữ cảnh học tập, công việc", 3),
            LevelEntity("B2", "B2", "B2 - Trung cao cấp", "Từ trừu tượng, collocation và chuyên sâu", 4),
            LevelEntity("C1", "C1", "C1 - Nâng cao", "Từ học thuật, chuyên ngành và giao tiếp tinh tế", 5)
        )
        levelDao.insertLevels(levels)

        // 2. Seed Topics
        val topics = listOf(
            // A1 Topics
            TopicEntity(id = 1, levelId = "A1", title = "Daily Life", description = "Từ vựng cơ bản về cuộc sống hàng ngày"),
            TopicEntity(id = 2, levelId = "A1", title = "School & Education", description = "Môi trường học tập và các môn học"),
            TopicEntity(id = 3, levelId = "A1", title = "Travel & Transport", description = "Phương tiện đi lại và di chuyển"),
            
            // A2 Topics
            TopicEntity(id = 4, levelId = "A2", title = "Workplace & Jobs", description = "Công việc, nghề nghiệp và môi trường văn phòng"),
            TopicEntity(id = 5, levelId = "A2", title = "Hobbies & Shopping", description = "Sở thích cá nhân, thể thao và mua sắm"),
            TopicEntity(id = 6, levelId = "A2", title = "Nature & Animals", description = "Thời tiết, thiên nhiên và động vật"),

            // B1 Topics
            TopicEntity(id = 7, levelId = "B1", title = "Health & Lifestyle", description = "Sức khỏe, cơ thể con người và thể thao"),
            TopicEntity(id = 8, levelId = "B1", title = "Food & Dining", description = "Ẩm thực, nhà hàng và nấu nướng"),

            // B2 Topics
            TopicEntity(id = 9, levelId = "B2", title = "Technology & Media", description = "Công nghệ, Internet và mạng xã hội"),
            TopicEntity(id = 10, levelId = "B2", title = "Business & Finance", description = "Tài chính, ngân hàng và tiếp thị"),

            // C1 Topics
            TopicEntity(id = 11, levelId = "C1", title = "Academic & Science", description = "Nghiên cứu khoa học và triết học"),
            TopicEntity(id = 12, levelId = "C1", title = "Society & Culture", description = "Văn hóa, nghệ thuật, luật pháp và chính trị")
        )
        topicDao.insertTopics(topics)

        // 3. Seed Decks
        val decks = listOf(
            // Topic 1: Daily Life (A1)
            DeckEntity(id = 1, topicId = 1, title = "Morning Routine", description = "Thói quen và hoạt động buổi sáng", sourceType = "SYSTEM"),
            DeckEntity(id = 2, topicId = 1, title = "Family & House", description = "Thành viên gia đình và đồ dùng trong nhà", sourceType = "SYSTEM"),
            
            // Topic 2: School & Education (A1)
            DeckEntity(id = 3, topicId = 2, title = "Classroom Items", description = "Đồ dùng học tập và thiết bị lớp học", sourceType = "SYSTEM"),
            DeckEntity(id = 4, topicId = 2, title = "Study Activities", description = "Kỹ năng và hoạt động học tập", sourceType = "SYSTEM"),

            // Topic 3: Travel & Transport (A1)
            DeckEntity(id = 5, topicId = 3, title = "Vehicles", description = "Các phương tiện giao thông thông dụng", sourceType = "SYSTEM"),
            DeckEntity(id = 6, topicId = 3, title = "Places in City", description = "Các địa điểm trong thành phố", sourceType = "SYSTEM"),

            // Topic 4: Workplace & Jobs (A2)
            DeckEntity(id = 7, topicId = 4, title = "Office Life", description = "Thiết bị văn phòng và cuộc họp", sourceType = "SYSTEM"),
            DeckEntity(id = 8, topicId = 4, title = "Common Occupations", description = "Các ngành nghề phổ biến", sourceType = "SYSTEM"),

            // Topic 5: Hobbies & Shopping (A2)
            DeckEntity(id = 9, topicId = 5, title = "Shopping & Clothes", description = "Mua sắm, quần áo và giá cả", sourceType = "SYSTEM"),
            DeckEntity(id = 10, topicId = 5, title = "Sports & Games", description = "Các môn thể thao và trò chơi giải trí", sourceType = "SYSTEM"),

            // Topic 6: Nature & Animals (A2)
            DeckEntity(id = 11, topicId = 6, title = "Weather & Seasons", description = "Thời tiết và các mùa trong năm", sourceType = "SYSTEM"),
            DeckEntity(id = 12, topicId = 6, title = "Wild & Domestic Animals", description = "Động vật hoang dã và vật nuôi", sourceType = "SYSTEM"),

            // Topic 7: Health & Lifestyle (B1)
            DeckEntity(id = 13, topicId = 7, title = "Body & Health", description = "Cơ thể con người và tình trạng sức khỏe", sourceType = "SYSTEM"),
            DeckEntity(id = 14, topicId = 7, title = "Medical & Care", description = "Y tế, bệnh viện và thuốc men", sourceType = "SYSTEM"),

            // Topic 8: Food & Dining (B1)
            DeckEntity(id = 15, topicId = 8, title = "Food & Ingredients", description = "Thực phẩm và nguyên liệu nấu ăn", sourceType = "SYSTEM"),
            DeckEntity(id = 16, topicId = 8, title = "Restaurant & Dining", description = "Đi ăn nhà hàng và gọi món", sourceType = "SYSTEM"),

            // Topic 9: Technology & Media (B2)
            DeckEntity(id = 917, topicId = 9, title = "Software & Internet", description = "Phần mềm, mạng Internet và máy tính", sourceType = "SYSTEM"),
            DeckEntity(id = 918, topicId = 9, title = "Social Media & Mobile", description = "Mạng xã hội và thiết bị di động", sourceType = "SYSTEM"),

            // Topic 10: Business & Finance (B2)
            DeckEntity(id = 1019, topicId = 10, title = "Banking & Money", description = "Ngân hàng, giao dịch và tiền tệ", sourceType = "SYSTEM"),
            DeckEntity(id = 1020, topicId = 10, title = "Marketing & Sales", description = "Tiếp thị, bán hàng và chiến lược", sourceType = "SYSTEM"),

            // Topic 11: Academic & Science (C1)
            DeckEntity(id = 1121, topicId = 11, title = "Scientific Research", description = "Nghiên cứu, phát minh và phương pháp luận", sourceType = "SYSTEM"),
            
            // Topic 12: Society & Culture (C1)
            DeckEntity(id = 1222, topicId = 12, title = "Culture & Philosophy", description = "Triết học, tư tưởng và di sản văn hóa", sourceType = "SYSTEM")
        )
        deckDao.insertDecks(decks)

        // 4. Seed Vocabularies
        val vocabularies = listOf(
            // Deck 1: Morning Routine
            VocabularyEntity(deckId = 1, word = "wake up", meaning = "thức dậy", phonetic = "/weɪk ʌp/", example = "I wake up at 6 AM every day.", partOfSpeech = "phrasal verb"),
            VocabularyEntity(deckId = 1, word = "brush teeth", meaning = "đánh răng", phonetic = "/brʌʃ tiːθ/", example = "Remember to brush your teeth after meals.", partOfSpeech = "phrase"),
            VocabularyEntity(deckId = 1, word = "wash face", meaning = "rửa mặt", phonetic = "/wɒʃ feɪs/", example = "I wash my face with cold water.", partOfSpeech = "phrase"),
            VocabularyEntity(deckId = 1, word = "breakfast", meaning = "bữa ăn sáng", phonetic = "/ˈbrekfəst/", example = "Breakfast is the most important meal of the day.", partOfSpeech = "noun"),
            VocabularyEntity(deckId = 1, word = "shower", meaning = "tắm vòi hoa sen", phonetic = "/ˈʃaʊər/", example = "I take a quick shower in the morning.", partOfSpeech = "noun"),
            VocabularyEntity(deckId = 1, word = "get dressed", meaning = "mặc quần áo", phonetic = "/ɡet drest/", example = "She gets dressed quickly for work.", partOfSpeech = "phrase"),
            VocabularyEntity(deckId = 1, word = "comb hair", meaning = "chải tóc", phonetic = "/kəʊm heər/", example = "He combs his hair before going out.", partOfSpeech = "phrase"),
            VocabularyEntity(deckId = 1, word = "alarm", meaning = "chuông báo thức", phonetic = "/əˈlɑːm/", example = "The alarm rings at 5:30 AM.", partOfSpeech = "noun"),
            VocabularyEntity(deckId = 1, word = "coffee", meaning = "cà phê", phonetic = "/ˈkɒfi/", example = "I love drinking hot coffee in the morning.", partOfSpeech = "noun"),
            VocabularyEntity(deckId = 1, word = "exercise", meaning = "tập thể dục", phonetic = "/ˈeksəsaɪz/", example = "Morning exercise helps keep you fit.", partOfSpeech = "noun"),

            // Deck 2: Family & House
            VocabularyEntity(deckId = 2, word = "parents", meaning = "bố mẹ", phonetic = "/ˈpeərənts/", example = "My parents live in Hanoi.", partOfSpeech = "noun"),
            VocabularyEntity(deckId = 2, word = "sibling", meaning = "anh chị em ruột", phonetic = "/ˈsɪblɪŋ/", example = "Do you have any siblings?", partOfSpeech = "noun"),
            VocabularyEntity(deckId = 2, word = "living room", meaning = "phòng khách", phonetic = "/ˈlɪvɪŋ ruːm/", example = "We watch TV in the living room.", partOfSpeech = "noun"),
            VocabularyEntity(deckId = 2, word = "kitchen", meaning = "nhà bếp", phonetic = "/ˈkɪtʃɪn/", example = "Mom is cooking in the kitchen.", partOfSpeech = "noun"),
            VocabularyEntity(deckId = 2, word = "bedroom", meaning = "phòng ngủ", phonetic = "/ˈbedruːm/", example = "My bedroom is upstairs.", partOfSpeech = "noun"),
            VocabularyEntity(deckId = 2, word = "furniture", meaning = "đồ nội thất", phonetic = "/ˈfɜːnɪtʃər/", example = "They bought new furniture for the apartment.", partOfSpeech = "noun"),
            VocabularyEntity(deckId = 2, word = "balcony", meaning = "ban công", phonetic = "/ˈbælkəni/", example = "There are flowers on the balcony.", partOfSpeech = "noun"),
            VocabularyEntity(deckId = 2, word = "roof", meaning = "mái nhà", phonetic = "/ruːf/", example = "Birds are sitting on the roof.", partOfSpeech = "noun"),

            // Deck 3: Classroom Items
            VocabularyEntity(deckId = 3, word = "teacher", meaning = "giáo viên", phonetic = "/ˈtiːtʃər/", example = "The teacher explains the lesson clearly.", partOfSpeech = "noun"),
            VocabularyEntity(deckId = 3, word = "student", meaning = "học sinh, sinh viên", phonetic = "/ˈstjuːdnt/", example = "Every student must complete their homework.", partOfSpeech = "noun"),
            VocabularyEntity(deckId = 3, word = "desk", meaning = "bàn học", phonetic = "/desk/", example = "There is a laptop on the desk.", partOfSpeech = "noun"),
            VocabularyEntity(deckId = 3, word = "whiteboard", meaning = "bảng trắng", phonetic = "/ˈwaɪtbɔːd/", example = "The teacher writes on the whiteboard.", partOfSpeech = "noun"),
            VocabularyEntity(deckId = 3, word = "backpack", meaning = "ba lô", phonetic = "/ˈbækpæk/", example = "He packed his books into his backpack.", partOfSpeech = "noun"),
            VocabularyEntity(deckId = 3, word = "ruler", meaning = "thước kẻ", phonetic = "/ˈruːlər/", example = "Use a ruler to draw a straight line.", partOfSpeech = "noun"),
            VocabularyEntity(deckId = 3, word = "eraser", meaning = "cục tẩy", phonetic = "/ɪˈreɪzər/", example = "Can I borrow your eraser?", partOfSpeech = "noun"),

            // Deck 5: Vehicles
            VocabularyEntity(deckId = 5, word = "bicycle", meaning = "xe đạp", phonetic = "/ˈbaɪsɪkl/", example = "I ride my bicycle to school.", partOfSpeech = "noun"),
            VocabularyEntity(deckId = 5, word = "motorbike", meaning = "xe máy", phonetic = "/ˈməʊtəbaɪk/", example = "Motorbikes are common in Vietnam.", partOfSpeech = "noun"),
            VocabularyEntity(deckId = 5, word = "bus stop", meaning = "trạm xe buýt", phonetic = "/ˈbʌs stɒp/", example = "Wait for me at the bus stop.", partOfSpeech = "noun"),
            VocabularyEntity(deckId = 5, word = "subway", meaning = "tau điện ngầm", phonetic = "/ˈsʌbweɪ/", example = "The subway is fast and cheap.", partOfSpeech = "noun"),
            VocabularyEntity(deckId = 5, word = "airplane", meaning = "máy bay", phonetic = "/ˈeəpleɪn/", example = "The airplane landed safely at the airport.", partOfSpeech = "noun"),

            // Deck 7: Office Life (A2)
            VocabularyEntity(deckId = 7, word = "meeting", meaning = "cuộc họp", phonetic = "/ˈmiːtɪŋ/", example = "We have a team meeting at 9 AM.", partOfSpeech = "noun"),
            VocabularyEntity(deckId = 7, word = "schedule", meaning = "lịch trình, thời khóa biểu", phonetic = "/ˈʃedjuːl/", example = "Check your schedule for free time.", partOfSpeech = "noun"),
            VocabularyEntity(deckId = 7, word = "deadline", meaning = "hạn chót", phonetic = "/ˈdedlaɪn/", example = "The project deadline is tomorrow.", partOfSpeech = "noun"),
            VocabularyEntity(deckId = 7, word = "colleague", meaning = "đồng nghiệp", phonetic = "/ˈkɒliːɡ/", example = "She works well with her colleagues.", partOfSpeech = "noun"),
            VocabularyEntity(deckId = 7, word = "report", meaning = "bản báo cáo", phonetic = "/rɪˈpɔːt/", example = "Submit the monthly report by Friday.", partOfSpeech = "noun"),
            VocabularyEntity(deckId = 7, word = "presentation", meaning = "bài thuyết trình", phonetic = "/ˌpreznˈteɪʃn/", example = "His presentation was very impressive.", partOfSpeech = "noun"),

            // Deck 8: Common Occupations (A2)
            VocabularyEntity(deckId = 8, word = "engineer", meaning = "kỹ sư", phonetic = "/ˌendʒɪˈnɪər/", example = "My brother is a software engineer.", partOfSpeech = "noun"),
            VocabularyEntity(deckId = 8, word = "doctor", meaning = "bác sĩ", phonetic = "/ˈdɒktər/", example = "The doctor examined the patient.", partOfSpeech = "noun"),
            VocabularyEntity(deckId = 8, word = "accountant", meaning = "kế toán viên", phonetic = "/əˈkaʊntənt/", example = "An accountant manages financial records.", partOfSpeech = "noun"),
            VocabularyEntity(deckId = 8, word = "architect", meaning = "kiến trúc sư", phonetic = "/ˈɑːkɪtekt/", example = "The architect designed a modern building.", partOfSpeech = "noun"),
            VocabularyEntity(deckId = 8, word = "chef", meaning = "đầu bếp", phonetic = "/ʃef/", example = "The chef prepared a delicious dish.", partOfSpeech = "noun"),

            // Deck 9: Shopping & Clothes (A2)
            VocabularyEntity(deckId = 9, word = "discount", meaning = "giảm giá", phonetic = "/ˈdɪskaʊnt/", example = "There is a 20% discount on shoes today.", partOfSpeech = "noun"),
            VocabularyEntity(deckId = 9, word = "receipt", meaning = "hóa đơn thanh toán", phonetic = "/rɪˈsiːt/", example = "Keep the receipt for return or exchange.", partOfSpeech = "noun"),
            VocabularyEntity(deckId = 9, word = "fitting room", meaning = "phòng thử đồ", phonetic = "/ˈfɪtɪŋ ruːm/", example = "Where is the fitting room?", partOfSpeech = "noun"),
            VocabularyEntity(deckId = 9, word = "jacket", meaning = "áo khoác", phonetic = "/ˈdʒækɪt/", example = "Put on your jacket, it's cold outside.", partOfSpeech = "noun"),

            // Deck 13: Body & Health (B1)
            VocabularyEntity(deckId = 13, word = "symptom", meaning = "triệu chứng", phonetic = "/ˈsɪmptəm/", example = "Fever is a common symptom of flu.", partOfSpeech = "noun"),
            VocabularyEntity(deckId = 13, word = "prescription", meaning = "đơn thuốc", phonetic = "/prɪˈskrɪpʃn/", example = "The doctor gave me a prescription for antibiotics.", partOfSpeech = "noun"),
            VocabularyEntity(deckId = 13, word = "recovery", meaning = "hồi phục", phonetic = "/rɪˈkʌvəri/", example = "We wish you a speedy recovery.", partOfSpeech = "noun"),
            VocabularyEntity(deckId = 13, word = "immune system", meaning = "hệ miễn dịch", phonetic = "/ɪˈmjuːn ˈsɪstəm/", example = "Vitamin C boosts your immune system.", partOfSpeech = "noun"),

            // Deck 917: Software & Internet (B2)
            VocabularyEntity(deckId = 917, word = "algorithm", meaning = "thuật toán", phonetic = "/ˈælɡərɪðəm/", example = "Search engines use complex algorithms.", partOfSpeech = "noun"),
            VocabularyEntity(deckId = 917, word = "database", meaning = "cơ sở dữ liệu", phonetic = "/ˈdeɪtəbeɪs/", example = "User info is stored securely in the database.", partOfSpeech = "noun"),
            VocabularyEntity(deckId = 917, word = "cybersecurity", meaning = "an ninh mạng", phonetic = "/ˌsaɪbərsɪˈkjʊərəti/", example = "Cybersecurity is crucial for online banking.", partOfSpeech = "noun"),
            VocabularyEntity(deckId = 917, word = "bandwidth", meaning = "băng thông", phonetic = "/ˈbændwɪdtθ/", example = "Video streaming requires high bandwidth.", partOfSpeech = "noun"),
            VocabularyEntity(deckId = 917, word = "artificial intelligence", meaning = "trí tuệ nhân tạo (AI)", phonetic = "/ˌɑːtɪˈfɪʃl ɪnˈtelɪdʒəns/", example = "Artificial intelligence is transforming industries.", partOfSpeech = "noun"),

            // Deck 1019: Banking & Money (B2)
            VocabularyEntity(deckId = 1019, word = "investment", meaning = "khoản đầu tư", phonetic = "/ɪnˈvestmənt/", example = "Real estate is a long-term investment.", partOfSpeech = "noun"),
            VocabularyEntity(deckId = 1019, word = "interest rate", meaning = "lãi suất", phonetic = "/ˈɪntrest reɪt/", example = "Central bank lowered the interest rate.", partOfSpeech = "noun"),
            VocabularyEntity(deckId = 1019, word = "transaction", meaning = "giao dịch", phonetic = "/trænˈzækʃn/", example = "The bank transaction was successful.", partOfSpeech = "noun"),
            VocabularyEntity(deckId = 1019, word = "revenue", meaning = "doanh thu", phonetic = "/ˈrevənjuː/", example = "Company revenue increased by 15% this year.", partOfSpeech = "noun"),

            // Deck 1121: Scientific Research (C1)
            VocabularyEntity(deckId = 1121, word = "hypothesis", meaning = "giả thuyết", phonetic = "/haɪˈpɒθəsɪs/", example = "Scientists conducted experiments to test the hypothesis.", partOfSpeech = "noun"),
            VocabularyEntity(deckId = 1121, word = "methodology", meaning = "phương pháp luận", phonetic = "/ˌmeθəˈdɒlədʒi/", example = "The research methodology must be rigorous.", partOfSpeech = "noun"),
            VocabularyEntity(deckId = 1121, word = "empirical", meaning = "dựa trên thực nghiệm", phonetic = "/ɪmˈpɪrɪkl/", example = "The conclusion is supported by empirical evidence.", partOfSpeech = "adjective"),
            VocabularyEntity(deckId = 1121, word = "paradigm", meaning = "mô hình, mẫu hình", phonetic = "/ˈpærədaɪm/", example = "Quantum physics introduced a new paradigm.", partOfSpeech = "noun"),

            // Deck 1222: Culture & Philosophy (C1)
            VocabularyEntity(deckId = 1222, word = "heritage", meaning = "di sản", phonetic = "/ˈherɪtɪdʒ/", example = "Preserving cultural heritage is essential.", partOfSpeech = "noun"),
            VocabularyEntity(deckId = 1222, word = "aesthetics", meaning = "mỹ học, tính thẩm mỹ", phonetic = "/iːsˈθetɪks/", example = "The design balances functionality and aesthetics.", partOfSpeech = "noun"),
            VocabularyEntity(deckId = 1222, word = "enlightenment", meaning = "sự khai sáng", phonetic = "/ɪnˈlaɪtnmənt/", example = "The Age of Enlightenment shaped modern philosophy.", partOfSpeech = "noun")
        )
        vocabularyDao.insertVocabularies(vocabularies)
    }
}
