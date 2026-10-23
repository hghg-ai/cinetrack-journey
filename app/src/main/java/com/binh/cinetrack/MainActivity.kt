package com.binh.cinetrack

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.binh.cinetrack.data.preferences.AppTheme
import com.binh.cinetrack.data.preferences.ThemePreferences
import com.binh.cinetrack.ui.theme.CineTrackTheme
import com.binh.cinetrack.ui.theme.CinetrackApp
import com.binh.cinetrack.ui.theme.HomeScreen
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val context = LocalContext.current
            val themePreferences = remember { ThemePreferences(context) }
            val currentTheme by themePreferences.themeFlow
                .collectAsStateWithLifecycle(initialValue = AppTheme.SYSTEM)

                val isDarkTheme = when (currentTheme){
                    AppTheme.SYSTEM -> isSystemInDarkTheme()
                    AppTheme.LIGHT -> false
                    AppTheme.DARK -> true

                }

            CineTrackTheme(darkTheme = isDarkTheme) {
                CinetrackApp()
            }
        }
    }
}