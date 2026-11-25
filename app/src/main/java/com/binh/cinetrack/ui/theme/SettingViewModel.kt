package com.binh.cinetrack.ui.theme

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.binh.cinetrack.data.preferences.AppTheme
import com.binh.cinetrack.data.preferences.ThemePreferences
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ViewModel quản lý logic trang Cài Đặt (Settings).
 * Đồng bộ lưu/đọc chế độ giao diện (Dark/Light Theme) và bật/tắt thông báo qua ThemePreferences (DataStore).
 */
@HiltViewModel
class SettingViewModel @Inject constructor(
    private val themePreferences: ThemePreferences
) : ViewModel() {

    // Trạng thái Theme hiện tại (SYSTEM, LIGHT, DARK)
    val currentTheme: StateFlow<AppTheme> = themePreferences.themeFlow
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = AppTheme.SYSTEM
        )

    // Trạng thái Bật/Tắt thông báo hằng ngày
    val isNotificationEnabled: StateFlow<Boolean> = themePreferences.notificationEnabledFlow
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = false
        )

    // Thay đổi và lưu Theme mới vào DataStore
    fun selectTheme(theme: AppTheme) {
        viewModelScope.launch {
            themePreferences.saveTheme(theme)
        }
    }

    // Thay đổi và lưu trạng thái bật/tắt thông báo vào DataStore
    fun toggleNotification(enabled: Boolean) {
        viewModelScope.launch {
            themePreferences.saveNotificationEnabled(enabled)
        }
    }
}
