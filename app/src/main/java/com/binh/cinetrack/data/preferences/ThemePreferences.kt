package com.binh.cinetrack.data.preferences

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

// Khai báo file DataStore Preferences với tên "user_settings"
private val Context.dataStore by preferencesDataStore(name = "user_settings")
class ThemePreferences(private val context: Context) {
    companion object{
        // Khai báo Key kiểu String để lưu enum AppTheme dưới dạng chuỗi
        val THEME_KEY = stringPreferencesKey("app_theme")
    }
    // Đọc trạng thái Theme từ DataStore dưới dạng Flow
    val themeFlow: Flow<AppTheme> = context.dataStore.data.map { preferences ->
        val themeString = preferences[THEME_KEY] ?: AppTheme.SYSTEM.name
        try {
            AppTheme.valueOf(themeString)
        }catch (e: Exception){
            AppTheme.SYSTEM
        }
    }
     // Hàm lưu lựa chọn Theme mới vào DataStore
     suspend fun saveTheme(theme: AppTheme){
         context.dataStore.edit { preferences ->
             preferences[THEME_KEY] = theme.name
         }
     }
}