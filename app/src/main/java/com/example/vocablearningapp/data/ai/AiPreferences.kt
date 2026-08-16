package com.example.vocablearningapp.data.ai

import android.content.Context
import android.content.SharedPreferences

class AiPreferences(context: Context) {
    private val prefs: SharedPreferences =
        context.getSharedPreferences("ai_tutor_prefs", Context.MODE_PRIVATE)

    companion object {
        private const val KEY_GEMINI_API_KEY = "gemini_api_key"
        private const val KEY_SELECTED_MODEL = "selected_model"
        const val DEFAULT_MODEL = "gemini-1.5-flash"
    }

    var apiKey: String
        get() = prefs.getString(KEY_GEMINI_API_KEY, "").orEmpty()
        set(value) = prefs.edit().putString(KEY_GEMINI_API_KEY, value.trim()).apply()

    var model: String
        get() = prefs.getString(KEY_SELECTED_MODEL, DEFAULT_MODEL).orEmpty()
        set(value) = prefs.edit().putString(KEY_SELECTED_MODEL, value.trim()).apply()

    val hasApiKey: Boolean
        get() = apiKey.isNotBlank()
}
