# VocabLearningApp - Developer & Agent Context Guide

> **Mục đích tài liệu:** Tài liệu này cung cấp cái nhìn toàn diện về kiến trúc, công nghệ, cấu trúc thư mục, luồng dữ liệu và lộ trình phát triển của dự án **VocabLearningApp** dành cho các AI Agent và lập trình viên trong các phiên làm việc tiếp theo.

---

## 1. Tổng quan dự án (Project Overview)
**VocabLearningApp** là ứng dụng học từ vựng tiếng Anh trên nền tảng Android, áp dụng thuật toán lặp lại ngắt quãng hiện đại **FSRS-6 (Free Spaced Repetition Scheduler)** và khung tham chiếu chuẩn ngôn ngữ châu Âu **CEFR (A1 - C2)**. Ứng dụng cung cấp trải nghiệm học tập nhẹ nhàng, tập trung vào việc ghi nhớ sâu, phát âm chuẩn (IPA + TTS) và theo dõi tiến độ học tập hàng ngày.

- **Nền tảng:** Android (Native)
- **Min SDK:** 26 (Android 8.0 Oreo) | **Target/Compile SDK:** 35 (Android 15)
- **Ngôn ngữ:** Kotlin
- **UI Framework:** Jetpack Compose (Material Design 3)
- **Kiến trúc:** MVVM (Model-View-ViewModel) kết hợp Clean Architecture căn bản (Data - Domain - UI)

---

## 2. Công nghệ & Thư viện sử dụng (Tech Stack)

| Thành phần | Công nghệ / Thư viện | Ghi chú |
|---|---|---|
| **UI** | Jetpack Compose + Material 3 | Giao diện khai báo, Material Icons Extended |
| **Navigation** | Navigation Compose (`androidx.navigation:navigation-compose`) | Single-Activity, điều hướng qua Route |
| **State Management** | Android ViewModel + Compose `mutableStateOf` / `mutableStateListOf` | Quản lý state tập trung tại `AppViewModel` |
| **TTS (Text To Speech)** | `android.speech.tts.TextToSpeech` | Phát âm từ vựng và câu ví dụ (US English) |
| **Thuật toán Spaced Repetition** | FSRS-6 (`FsrsScheduler.kt`) | Triển khai thuần Kotlin với 21 tham số FSRS-6, hỗ trợ fuzzing và retrievability |
| **Lưu trữ dữ liệu** | `Room Database` (SQLite) + `SharedPreferences` (Streak) + Assets JSON (`cefr_dictionary.json`) | Dữ liệu các Set và tiến độ FSRS lưu bền vững tại Room DB |
| **Hệ thống Build** | Gradle Kotlin DSL (`build.gradle.kts`) + KSP (`com.google.devtools.ksp`) | Version Catalog (`libs.versions.toml`) |

---

## 3. Cấu trúc thư mục (Directory Structure)

```text
app/src/main/
├── assets/
│   └── cefr_dictionary.json       # Cơ sở dữ liệu từ điển CEFR (A1-C2, từ loại, IPA, nghĩa, ví dụ)
├── java/com/example/vocablearningapp/
│   ├── MainActivity.kt            # Single Activity khởi tạo theme, NavController và AppViewModel
│   ├── data/                      # Tầng dữ liệu (Data Layer)
│   │   ├── DictionaryRepository.kt# Tra cứu từ điển, tìm kiếm gợi ý từ (searchWords), sinh bộ từ Daily
│   │   ├── MockData.kt            # Dữ liệu mẫu khởi tạo ban đầu cho Room DB
│   │   ├── StreakManager.kt       # Quản lý chuỗi ngày học liên tục (Streak) qua SharedPreferences
│   │   ├── ai/                    # AI Tutor & LLM Agent Layer
│   │   │   ├── AiModels.kt        # Data classes: AiChatMessage, AiGeneratedSet, AiActionPayload
│   │   │   ├── AiPreferences.kt   # Quản lý cấu hình Gemini API Key trong SharedPreferences
│   │   │   └── GeminiApiClient.kt # Client giao tiếp Google Gemini API (Structured JSON) + Offline Mock Engine
│   │   ├── local/                 # Room Database (Local Persistence)
│   │   │   ├── AppDatabase.kt     # RoomDatabase (tables: vocabulary_sets, vocabulary_items)
│   │   │   ├── dao/
│   │   │   │   └── VocabularyDao.kt # Truy vấn và cập nhật Set, Word, FSRS state
│   │   │   └── entity/
│   │   │       ├── VocabularySetEntity.kt
│   │   │       ├── VocabularyItemEntity.kt
│   │   │       └── SetWithWords.kt# Quan hệ 1-N giữa Set và Word, mapper sang Domain Model
│   │   └── repository/
│   │       └── VocabularyRepository.kt # Repository trung gian quản lý dữ liệu Room DB & Seeding
│   ├── domain/                    # Tầng nghiệp vụ (Domain Layer)
│   │   ├── model/
│   │   │   └── VocabularyModels.kt# Entity: VocabularyItem, VocabularySet, FsrsState, FsrsRating, PartOfSpeech, StudyMode
│   │   ├── srs/
│   │   │   ├── FsrsScheduler.kt   # Bộ lập lịch FSRS-6 (tính stability, difficulty, interval, due date)
│   │   │   └── FsrsRatingEvaluator.kt # Đánh giá xếp hạng FSRS tự động dựa trên thời gian phản hồi & độ chính xác
│   │   └── util/
│   │       ├── ExampleSentenceGenerator.kt # Bộ sinh câu ví dụ ngữ cảnh tự nhiên
│   │       ├── IpaGenerator.kt    # Bộ sinh phiên âm IPA tự động và bàn phím ký tự IPA
│   │       └── MeaningParser.kt   # Bộ bóc tách đa nghĩa & đa từ loại (n., v., adj.)
│   └── ui/                        # Tầng giao diện (UI Layer)
│       ├── component/             # Component tái sử dụng
│       │   ├── AppScaffold.kt     # Bottom Navigation Bar và Scaffold chính
│       │   ├── Buttons.kt         # PrimaryButton, SecondaryButton, FsrsRatingButton, TextAction
│       │   ├── CommonComponents.kt# SectionHeader, VocabProgressBar, VocabularySetCard, VocabularyRow, StatCard, EmptyState...
│       │   ├── Flashcard.kt       # Thẻ Flashcard 2 mặt (lật thẻ, hiển thị IPA, TTS audio)
│       │   ├── SessionResultSummary.kt # Màn hình tổng kết kết quả sau mỗi phiên học (hỗ trợ Skip/Learn daily)
│       │   └── SpeechButton.kt    # Nút phát âm Text-to-Speech
│       ├── navigation/
│       │   ├── NavGraph.kt        # Khai báo tuyến đường (NavHost) và kết nối ViewModel
│       │   └── Screen.kt          # Định nghĩa danh sách các Route (Home, Learn, Review, Progress, Practice, SetEditor, AiChat...)
│       ├── screen/
│       │   ├── ai/
│       │   │   └── AiChatScreen.kt# Màn hình Gia sư AI (VocabAI Coach): Chat, tạo bộ từ 1-chạm vào Room DB
│       │   ├── flashcard/FlashcardScreen.kt   # Màn hình học Flashcard tự đánh giá FSRS
│       │   ├── home/HomeScreen.kt             # Trang chủ: Streak, AI Tutor Banner, Daily Words, Danh sách Set gần đây
│       │   ├── learn/LearnScreen.kt           # Tab Học: Bộ từ đang học, danh sách Set
│       │   ├── learn/LearnModeScreen.kt       # Chế độ trắc nghiệm 4 đáp án (Multiple Choice)
│       │   ├── match/MatchModeScreen.kt       # Chế độ ghép cặp Từ - Nghĩa (Matching Pairs)
│       │   ├── practice/                      # Các chế độ luyện tập nâng cao (Active Recall & Syntax)
│       │   │   ├── FillBlankModeScreen.kt     # Điền từ vào chỗ trống trong câu ví dụ + Hint + FSRS
│       │   │   └── SentenceScrambleScreen.kt  # Sắp xếp từ xáo trộn thành câu hoàn chỉnh + FSRS
│       │   ├── progress/ProgressScreen.kt     # Tab Thống kê: Biểu đồ FSRS state, Streak, tổng số từ
│       │   ├── quiz/QuizModeScreen.kt         # Chế độ Đúng/Sai (True/False Quiz)
│       │   ├── review/ReviewScreen.kt         # Tab Ôn tập: Hàng đợi ôn tập theo FSRS due date
│       │   └── set/
│       │       ├── AllSetsScreen.kt           # Xem tất cả các bộ từ
│       │       ├── SetDetailScreen.kt         # Chi tiết bộ từ, chọn 6 chế độ học (Study Modes)
│       │       └── SetEditorScreen.kt         # Tạo/Chỉnh sửa bộ từ, hỗ trợ Auto-suggest từ điển, Auto IPA, gán đa nghĩa/ví dụ
│       └── theme/
│           ├── Color.kt           # Bảng màu chủ đạo (Accent Forest Green, Ink, Canvas, Surface, FSRS States)
│           ├── Theme.kt           # Cấu hình MaterialTheme
│           └── Type.kt            # Cấu hình Typography
```

---

## 4. Các tính năng cốt lõi hiện có (Implemented Features)

1. **Gia sư AI Trực tuyến & Tạo bộ từ 1-chạm (VocabAI Coach LLM)**:
   - Tích hợp Google Gemini 1.5 Flash thông qua Structured JSON Action (`CREATE_VOCAB_SET`).
   - Tự động sinh từ vựng theo chủ đề (Job Interview, Travel, Technology...), đầy đủ IPA, nghĩa tiếng Việt và câu ví dụ.
   - Thẻ xem trước tương tác (Interactive Set Card) cho phép bấm **`[💾 Save to My Sets]`** lưu thẳng vào Room Database.
   - Hỗ trợ **Dual-Engine**: Live Gemini API qua API Key hoặc Offline Smart Mock Engine khi không có mạng.
   - Tài liệu đặc tả: [`llm_ai_tutor_spec.md`](file:///Users/macbookair/Documents/Huster/Source-Code/VocabLearningApp/llm_ai_tutor_spec.md).
2. **Lưu trữ dữ liệu bền vững (Room Database)**:
   - Toàn bộ các bộ từ vựng tạo mới/chỉnh sửa và lịch sử FSRS được lưu trữ trong SQLite cục bộ trên máy người dùng thông qua Room DB.
   - Tự động seed dữ liệu mẫu trong lần khởi chạy đầu tiên.
3. **Gợi ý từ vựng theo thời gian thực (Real-time Word Suggestions)**:
   - Trong màn hình Soạn thảo bộ từ (`SetEditorScreen`), khi người dùng gõ từ vựng, hệ thống tự động tra cứu kho từ điển CEFR và hiển thị danh sách từ gợi ý phù hợp.
   - Khi chọn từ gợi ý, hệ thống tự động điền: Từ, Phiên âm IPA, Từ loại, tất cả các nghĩa tiếng Việt và câu ví dụ tương ứng.
4. **Thuật toán Spaced Repetition FSRS-6 & Smart Evaluator**:
   - Triển khai 21 tham số FSRS-6, tính toán `stability`, `difficulty`, `retrievability`, `dueAtMillis`.
   - Xem chi tiết tại tài liệu đặc tả: [`fsrs_system_spec.md`](file:///Users/macbookair/Documents/Huster/Source-Code/VocabLearningApp/fsrs_system_spec.md).
   - Tự động chuyển đổi hành vi trong từng Game (Độ chính xác, Thời gian phản xạ, Số lần gợi ý Hint, Undo) thành 4 mức FSRS Rating (`Again`, `Hard`, `Good`, `Easy`).
5. **Hệ thống 6 Chế độ Học tập Toàn diện (6 Study Modes)**:
   - **Flashcards**: Lật thẻ 2 mặt, phát âm câu và từ bằng TTS, người học tự đánh giá theo 4 nút FSRS.
   - **Learn (Multiple Choice)**: Trắc nghiệm 4 lựa chọn nghĩa tiếng Việt, chấm điểm tức thì và đưa vào FSRS.
   - **Quiz (True/False)**: Phản xạ nhanh kiểm tra tính đúng/sai giữa từ và nghĩa.
   - **Match (Ghép cặp)**: Trò chơi ghép 4 từ tiếng Anh tương ứng 4 nghĩa tiếng Việt.
   - **Fill in the Blank (Điền từ khuyết)**: Ẩn từ vựng trong câu ví dụ, người học gõ phím hoặc mở gợi ý ký tự.
   - **Sentence Scramble (Sắp xếp câu)**: Xáo trộn các mảnh ghép từ trong câu ví dụ, người học lắp ghép thành câu hoàn chỉnh theo đúng ngữ pháp.
6. **Theo dõi Streak & Thống kê tiến độ**:
   - Tính chuỗi ngày học hiện tại (`currentStreak`), kỷ lục (`bestStreak`), và hiển thị các ngày hoạt động trong tuần (Thứ 2 - Chủ nhật).
   - Biểu đồ phân bổ trạng thái thẻ FSRS và tỉ lệ chính xác.
7. **Từ vựng hàng ngày (Daily Words)**:
   - Tự động lấy danh sách 10 từ theo cấp độ người dùng đang học từ kho từ điển `cefr_dictionary.json`.
   - Hỗ trợ thao tác `Study` (đưa vào chu trình học) hoặc `Skip` (bỏ qua từ đã biết).

---

## 5. Luồng dữ liệu & State (State & Data Flow)

- `AppViewModel` sử dụng `VocabularyRepository` để quan sát Flow `getAllSetsFlow()` từ Room Database.
- Khi người dùng tạo/sửa bộ từ (`saveSet`) hoặc đánh giá thẻ (`rateItem`):
  1. Room DB được cập nhật ngay lập tức bất đồng bộ (Asynchronous qua Coroutine / IO Dispatcher).
  2. `FsrsScheduler.review()` tính toán ra `FsrsSchedule` mới và lưu trạng thái vào Room.
  3. `StreakManager.recordActivityToday()` ghi nhận phiên học hôm nay vào `SharedPreferences`.
  4. Giao diện nhận phản hồi tức thì và duy trì toàn bộ dữ liệu khi khởi động lại ứng dụng.

---

## 6. Lộ trình phát triển đề xuất tiếp theo (Next Steps)

- [ ] **Luyện phát âm qua giọng nói (Speech-to-Text / Speech Recognition)**: Sử dụng Android `SpeechRecognizer` để thu âm và chấm điểm độ tương đồng với từ vựng.
- [ ] **Tra cứu từ điển nhanh (Dictionary Lookup Tab / Search)**: Thêm thanh tìm kiếm từ vựng từ `cefr_dictionary.json` và nút 1 chạm "Lưu vào Set".
- [ ] **Nhắc nhở ôn tập thông minh (Notification via WorkManager)**: Lên lịch thông báo khi có từ đến hạn ôn tập FSRS trong ngày.
- [ ] **Kiểm tra trình độ đầu vào (CEFR Placement Test)**: Bài kiểm tra 10-15 câu trắc nghiệm để gợi ý cấp độ khởi đầu phù hợp cho người học.
- [ ] **Sổ tay từ khó / Hay sai (Weak Words / Mistake Bank)**: Danh sách tổng hợp các từ có `lapseCount > 0` hoặc rating nhiều lần là `Again`.
- [ ] **Export / Import dữ liệu**: Nhập/xuất bộ từ vựng qua file CSV, JSON hoặc Anki package.

