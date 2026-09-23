package com.binh.cinetrack

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.binh.cinetrack.ui.theme.CineTrackTheme
import com.binh.cinetrack.ui.theme.HomeScreen

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CineTrackTheme {
                HomeScreen()
            }
        }
    }
}