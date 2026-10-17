package com.binh.cinetrack.ui.theme

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.binh.cinetrack.data.preferences.AppTheme
import com.binh.cinetrack.data.preferences.ThemePreferences
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class SettingViewModel(
    private val themePreferences: ThemePreferences
) : ViewModel() {
    val currentTheme: StateFlow<AppTheme> = themePreferences.themeFlow
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = AppTheme.SYSTEM
        )
    fun selectTheme(theme: AppTheme){
        viewModelScope.launch {
            themePreferences.saveTheme(theme)
        }
    }
}