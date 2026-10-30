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

@HiltViewModel
class SettingViewModel @Inject constructor(
    private val themePreferences: ThemePreferences
) : ViewModel() {
    val currentTheme: StateFlow<AppTheme> = themePreferences.themeFlow
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = AppTheme.SYSTEM
        )
    val isNotificationEnabled: StateFlow<Boolean> = themePreferences.notificationEnabledFlow
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = false
        )
    fun selectTheme(theme: AppTheme){
        viewModelScope.launch {
            themePreferences.saveTheme(theme)
        }
    }
    fun toggleNotification(enabled: Boolean){
        viewModelScope.launch {
            themePreferences.saveNotificationEnabled(enabled)
        }
    }
}