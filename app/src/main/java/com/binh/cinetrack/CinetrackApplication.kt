package com.binh.cinetrack

import android.app.Application
import com.binh.cinetrack.data.local.AppDatabase

class CinetrackApplication : Application() {
    val database: AppDatabase by lazy { AppDatabase.getInstance(this) }
}