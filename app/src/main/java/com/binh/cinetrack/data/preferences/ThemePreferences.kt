package com.binh.cinetrack.data.preferences

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
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
        val NOTIFICATION_KEY = booleanPreferencesKey("is_notification_enabled")
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
    val notificationEnabledFlow: Flow<Boolean> = context.dataStore.data.map {
        preferences -> preferences[NOTIFICATION_KEY] ?: false
    }
     // Hàm lưu lựa chọn Theme mới vào DataStore
     suspend fun saveTheme(theme: AppTheme){
         context.dataStore.edit { preferences ->
             preferences[THEME_KEY] = theme.name
         }
     }
    suspend fun saveNotificationEnabled(enabled: Boolean){
        context.dataStore.edit { preferences ->
            preferences[NOTIFICATION_KEY] = enabled
        }
    }
}