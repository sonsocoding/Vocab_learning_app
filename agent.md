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
| **Lưu trữ dữ liệu** | `SharedPreferences` (Streak) + Assets JSON (`cefr_dictionary.json`) | Hiện tại dữ liệu học tập và các Set đang ở dạng In-Memory trong ViewModel |
| **Hệ thống Build** | Gradle Kotlin DSL (`build.gradle.kts`) | Version Catalog (`libs.versions.toml`) |

---

## 3. Cấu trúc thư mục (Directory Structure)

```text
app/src/main/
├── assets/
│   └── cefr_dictionary.json       # Cơ sở dữ liệu từ điển CEFR (A1-C2, từ loại, IPA, nghĩa, ví dụ)
├── java/com/example/vocablearningapp/
│   ├── MainActivity.kt            # Single Activity khởi tạo theme, NavController và AppViewModel
│   ├── data/                      # Tầng dữ liệu (Data Layer)
│   │   ├── DictionaryRepository.kt# Đọc JSON từ assets, tra từ, sinh bộ từ Daily theo level
│   │   ├── MockData.kt            # Dữ liệu mẫu khởi tạo cho các VocabularySet và DailyWords
│   │   └── StreakManager.kt       # Quản lý chuỗi ngày học liên tục (Streak) qua SharedPreferences
│   ├── domain/                    # Tầng nghiệp vụ (Domain Layer)
│   │   ├── model/
│   │   │   └── VocabularyModels.kt# Entity: VocabularyItem, VocabularySet, FsrsState, FsrsRating, PartOfSpeech, StudyMode
│   │   ├── srs/
│   │   │   ├── FsrsScheduler.kt   # Bộ lập lịch FSRS-6 (tính stability, difficulty, interval, due date)
│   │   │   └── FsrsRatingEvaluator.kt # Đánh giá xếp hạng FSRS tự động dựa trên thời gian phản hồi & độ chính xác
│   │   └── util/
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
│       │   └── Screen.kt          # Định nghĩa danh sách các Route (Home, Learn, Review, Progress, Practice, SetEditor...)
│       ├── screen/
│       │   ├── flashcard/FlashcardScreen.kt   # Màn hình học Flashcard tự đánh giá FSRS
│       │   ├── home/HomeScreen.kt             # Trang chủ: Streak, Daily Words, Danh sách Set gần đây
│       │   ├── learn/LearnScreen.kt           # Tab Học: Bộ từ đang học, danh sách Set
│       │   ├── learn/LearnModeScreen.kt       # Chế độ trắc nghiệm 4 đáp án (Multiple Choice)
│       │   ├── match/MatchModeScreen.kt       # Chế độ ghép cặp Từ - Nghĩa (Matching Pairs)
│       │   ├── progress/ProgressScreen.kt     # Tab Thống kê: Biểu đồ FSRS state, Streak, tổng số từ
│       │   ├── quiz/QuizModeScreen.kt         # Chế độ Đúng/Sai (True/False Quiz)
│       │   ├── review/ReviewScreen.kt         # Tab Ôn tập: Hàng đợi ôn tập theo FSRS due date
│       │   └── set/
│       │       ├── AllSetsScreen.kt           # Xem tất cả các bộ từ
│       │       ├── SetDetailScreen.kt         # Chi tiết bộ từ, chọn chế độ học (Study Modes)
│       │       └── SetEditorScreen.kt         # Tạo/Chỉnh sửa bộ từ, hỗ trợ Auto IPA, gán đa nghĩa/ví dụ
│       └── theme/
│           ├── Color.kt           # Bảng màu chủ đạo (Accent Forest Green, Ink, Canvas, Surface, FSRS States)
│           ├── Theme.kt           # Cấu hình MaterialTheme
│           └── Type.kt            # Cấu hình Typography
```

---

## 4. Các tính năng cốt lõi hiện có (Implemented Features)

1. **Thuật toán Spaced Repetition FSRS-6**:
   - 4 trạng thái thẻ: `New`, `Learning`, `Review`, `Relearning`.
   - 4 mức đánh giá khi ôn tập: `Again (1)`, `Hard (2)`, `Good (3)`, `Easy (4)`.
   - Tính toán chính xác độ ổn định (`stability`), độ khó (`difficulty`), khoảng cách ngày ôn (`scheduledDays`) và thời điểm tới hạn (`dueAtMillis`).
2. **Theo dõi Streak & Thống kê tiến độ**:
   - Tính chuỗi ngày học hiện tại (`currentStreak`), kỷ lục (`bestStreak`), và hiển thị các ngày hoạt động trong tuần (Thứ 2 - Chủ nhật).
   - Biểu đồ phân bổ trạng thái thẻ FSRS và tỉ lệ chính xác.
3. **Từ vựng hàng ngày (Daily Words)**:
   - Tự động lấy danh sách 10 từ theo cấp độ người dùng đang học từ kho từ điển `cefr_dictionary.json`.
   - Hỗ trợ thao tác `Study` (đưa vào chu trình học) hoặc `Skip` (bỏ qua từ đã biết).
4. **Đa dạng chế độ học tập (Study Modes)**:
   - **Flashcards**: Lật thẻ 2 mặt, phát âm câu và từ bằng TTS, người học tự đánh giá theo 4 nút FSRS.
   - **Learn (Multiple Choice)**: Trắc nghiệm 4 lựa chọn nghĩa tiếng Việt, chấm điểm tức thì và đưa vào FSRS.
   - **Quiz (True/False)**: Phản xạ nhanh kiểm tra tính đúng/sai giữa từ và nghĩa.
   - **Match (Ghép cặp)**: Trò chơi ghép 4 từ tiếng Anh tương ứng 4 nghĩa tiếng Việt.
5. **Trình soạn thảo bộ từ nâng cao (Set Editor)**:
   - Thêm/sửa từ vựng, tự động sinh phiên âm IPA bằng rule-based kết hợp tra cứu từ điển (`Auto IPA`).
   - Bàn phím ký hiệu ngữ âm IPA nhanh.
   - Bóc tách đa nghĩa (`MeaningParser`) theo từ loại (`noun`, `verb`, `adj`) kèm ví dụ cho từng nghĩa.

---

## 5. Luồng dữ liệu & State (State & Data Flow)

- `AppViewModel` đóng vai trò trung tâm chứa `AppUiState`:
  - `vocabularySets`: Danh sách các bộ từ hiện có.
  - `dailyWordsByLevel`: Bản đồ các từ gợi ý theo cấp độ A1 -> C2.
  - `streakState`: Trạng thái chuỗi ngày học được cập nhật qua `StreakManager`.
- Khi người dùng hoàn thành một lượt học/ôn tập, hàm `rateItem(itemId, rating)` được gọi:
  1. `FsrsScheduler.review()` tính toán ra `FsrsSchedule` mới.
  2. Trạng thái thẻ của từ trong danh sách được cập nhật (`fsrsState`, `stability`, `difficulty`, `dueAtMillis`).
  3. `StreakManager.recordActivityToday()` ghi nhận phiên học hôm nay vào `SharedPreferences`.

---

## 6. Lộ trình phát triển đề xuất (Roadmap & Next Steps)

- [ ] **Lưu trữ dữ liệu lâu dài (Persistence)**: Tích hợp **Room Database** để lưu trữ bộ từ, danh sách từ vựng và lịch sử ôn tập (tránh mất dữ liệu khi đóng app).
- [ ] **Tra cứu từ điển nhanh (Dictionary Lookup Tab / Search)**: Thêm thanh tìm kiếm từ vựng từ `cefr_dictionary.json` và nút 1 chạm "Lưu vào Set".
- [ ] **Chế độ Luyện viết & Nghe chính tả (Typing Test / Dictation)**:
  - `FILL_IN_BLANK`: Gõ lại từ bị ẩn trong câu ví dụ.
  - `LISTENING_PRACTICE`: Nghe âm thanh từ TTS và gõ lại đúng chính tả.
- [ ] **Luyện phát âm qua giọng nói (Speech-to-Text / Speech Recognition)**: Sử dụng Android `SpeechRecognizer` để thu âm và chấm điểm độ tương đồng với từ vựng.
- [ ] **Nhắc nhở ôn tập thông minh (Notification via WorkManager)**: Lên lịch thông báo khi có từ đến hạn ôn tập FSRS trong ngày.
- [ ] **Kiểm tra trình độ đầu vào (CEFR Placement Test)**: Bài kiểm tra 10-15 câu trắc nghiệm để gợi ý cấp độ khởi đầu phù hợp cho người học.
- [ ] **Sổ tay từ khó / Hay sai (Weak Words / Mistake Bank)**: Danh sách tổng hợp các từ có `lapseCount > 0` hoặc rating nhiều lần là `Again`.
- [ ] **Export / Import dữ liệu**: Nhập/xuất bộ từ vựng qua file CSV, JSON hoặc Anki package.
