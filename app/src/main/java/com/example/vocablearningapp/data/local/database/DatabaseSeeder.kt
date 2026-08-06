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
        // Seed Levels
        val levels = listOf(
            LevelEntity("A1", "A1", "Cấp độ A1 - Cơ bản", "Từ thông dụng, câu ngắn, tình huống hàng ngày", 1),
            LevelEntity("A2", "A2", "Cấp độ A2 - Sơ trung cấp", "Mở rộng giao tiếp cá nhân và sinh hoạt", 2),
            LevelEntity("B1", "B1", "Cấp độ B1 - Trung cấp", "Từ theo ngữ cảnh học tập và công việc", 3),
            LevelEntity("B2", "B2", "Cấp độ B2 - Trung cao cấp", "Từ trừu tượng hơn, collocation và sắc thái nghĩa", 4),
            LevelEntity("C1", "C1", "Cấp độ C1 - Nâng cao", "Từ học thuật, chuyên ngành và cách dùng tinh tế", 5)
        )
        levelDao.insertLevels(levels)

        // Seed Topics
        val topics = listOf(
            TopicEntity(id = 1, levelId = "A1", title = "Daily Life", description = "Từ vựng cơ bản về cuộc sống hàng ngày"),
            TopicEntity(id = 2, levelId = "A1", title = "School", description = "Từ vựng thuộc môi trường trường học")
        )
        topicDao.insertTopics(topics)

        // Seed Decks
        val decks = listOf(
            DeckEntity(id = 1, topicId = 1, title = "Morning Routine", description = "Các từ vựng về hoạt động buổi sáng", sourceType = "SYSTEM"),
            DeckEntity(id = 2, topicId = 1, title = "Food and Drinks", description = "Từ vựng về món ăn và đồ uống thông dụng", sourceType = "SYSTEM"),
            DeckEntity(id = 3, topicId = 2, title = "Classroom", description = "Đồ vật và thiết bị trong lớp học", sourceType = "SYSTEM"),
            DeckEntity(id = 4, topicId = 2, title = "Study Activities", description = "Các hoạt động và kỹ năng học tập", sourceType = "SYSTEM")
        )
        deckDao.insertDecks(decks)

        // Seed Vocabularies
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

            // Deck 2: Food and Drinks
            VocabularyEntity(deckId = 2, word = "water", meaning = "nước", phonetic = "/ˈwɔːtər/", example = "Drink plenty of water every day.", partOfSpeech = "noun"),
            VocabularyEntity(deckId = 2, word = "bread", meaning = "bánh mì", phonetic = "/bred/", example = "I had bread and butter for breakfast.", partOfSpeech = "noun"),
            VocabularyEntity(deckId = 2, word = "milk", meaning = "sữa", phonetic = "/mɪlk/", example = "Fresh milk is rich in calcium.", partOfSpeech = "noun"),
            VocabularyEntity(deckId = 2, word = "rice", meaning = "cơm, gạo", phonetic = "/raɪs/", example = "Rice is a popular food in Asia.", partOfSpeech = "noun"),
            VocabularyEntity(deckId = 2, word = "apple", meaning = "quả táo", phonetic = "/ˈæpl/", example = "An apple a day keeps the doctor away.", partOfSpeech = "noun"),
            VocabularyEntity(deckId = 2, word = "chicken", meaning = "thịt gà", phonetic = "/ˈtʃɪkɪn/", example = "We had grilled chicken for dinner.", partOfSpeech = "noun"),
            VocabularyEntity(deckId = 2, word = "tea", meaning = "trà", phonetic = "/tiː/", example = "Would you like a cup of green tea?", partOfSpeech = "noun"),
            VocabularyEntity(deckId = 2, word = "fruit", meaning = "trái cây", phonetic = "/fruːt/", example = "Fresh fruit contains many vitamins.", partOfSpeech = "noun"),
            VocabularyEntity(deckId = 2, word = "soup", meaning = "món súp, canh", phonetic = "/suːp/", example = "The vegetable soup tastes delicious.", partOfSpeech = "noun"),
            VocabularyEntity(deckId = 2, word = "juice", meaning = "nước ép", phonetic = "/dʒuːs/", example = "Orange juice is high in Vitamin C.", partOfSpeech = "noun"),

            // Deck 3: Classroom
            VocabularyEntity(deckId = 3, word = "teacher", meaning = "giáo viên", phonetic = "/ˈtiːtʃər/", example = "The teacher explains the lesson clearly.", partOfSpeech = "noun"),
            VocabularyEntity(deckId = 3, word = "student", meaning = "học sinh, sinh viên", phonetic = "/ˈstjuːdnt/", example = "Every student must complete their homework.", partOfSpeech = "noun"),
            VocabularyEntity(deckId = 3, word = "book", meaning = "sách", phonetic = "/bʊk/", example = "Please open your book to page 10.", partOfSpeech = "noun"),
            VocabularyEntity(deckId = 3, word = "desk", meaning = "bàn học", phonetic = "/desk/", example = "There is a laptop on the desk.", partOfSpeech = "noun"),
            VocabularyEntity(deckId = 3, word = "chair", meaning = "cái ghế", phonetic = "/tʃeər/", example = "Sit down on the chair, please.", partOfSpeech = "noun"),
            VocabularyEntity(deckId = 3, word = "board", meaning = "bảng", phonetic = "/bɔːd/", example = "The teacher writes the sentence on the board.", partOfSpeech = "noun"),
            VocabularyEntity(deckId = 3, word = "pen", meaning = "bút mực", phonetic = "/pen/", example = "Can I borrow your blue pen?", partOfSpeech = "noun"),
            VocabularyEntity(deckId = 3, word = "notebook", meaning = "cuốn vở", phonetic = "/ˈnəʊtbʊk/", example = "Write down the main points in your notebook.", partOfSpeech = "noun"),
            VocabularyEntity(deckId = 3, word = "backpack", meaning = "ba lô", phonetic = "/ˈbækpæk/", example = "He packed his books into his backpack.", partOfSpeech = "noun"),
            VocabularyEntity(deckId = 3, word = "classroom", meaning = "phòng học", phonetic = "/ˈklɑːsruːm/", example = "The classroom is bright and clean.", partOfSpeech = "noun"),

            // Deck 4: Study Activities
            VocabularyEntity(deckId = 4, word = "read", meaning = "đọc", phonetic = "/riːd/", example = "I read books in the library every afternoon.", partOfSpeech = "verb"),
            VocabularyEntity(deckId = 4, word = "write", meaning = "viết", phonetic = "/raɪt/", example = "Students write essays about their dreams.", partOfSpeech = "verb"),
            VocabularyEntity(deckId = 4, word = "listen", meaning = "lắng nghe", phonetic = "/ˈlɪsn/", example = "Listen carefully to the instructions.", partOfSpeech = "verb"),
            VocabularyEntity(deckId = 4, word = "speak", meaning = "nói", phonetic = "/spiːk/", example = "Practice speaking English with friends.", partOfSpeech = "verb"),
            VocabularyEntity(deckId = 4, word = "learn", meaning = "học", phonetic = "/lɜːn/", example = "We learn new vocabulary every day.", partOfSpeech = "verb"),
            VocabularyEntity(deckId = 4, word = "exam", meaning = "kỳ thi", phonetic = "/ɪɡˈzæm/", example = "She is studying hard for the final exam.", partOfSpeech = "noun"),
            VocabularyEntity(deckId = 4, word = "question", meaning = "câu hỏi", phonetic = "/ˈkwestʃən/", example = "Raise your hand if you have a question.", partOfSpeech = "noun"),
            VocabularyEntity(deckId = 4, word = "answer", meaning = "câu trả lời", phonetic = "/ˈɑːnsər/", example = "He gave the correct answer to the teacher.", partOfSpeech = "noun"),
            VocabularyEntity(deckId = 4, word = "homework", meaning = "bài tập về nhà", phonetic = "/ˈhəʊmwɜːk/", example = "Finish your homework before going to bed.", partOfSpeech = "noun"),
            VocabularyEntity(deckId = 4, word = "review", meaning = "ôn tập", phonetic = "/rɪˈvjuː/", example = "Review the words before taking the test.", partOfSpeech = "verb")
        )
        vocabularyDao.insertVocabularies(vocabularies)
    }
}
