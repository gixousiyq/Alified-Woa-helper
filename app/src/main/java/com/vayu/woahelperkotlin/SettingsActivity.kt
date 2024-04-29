package com.vayu.woahelperkotlin

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.ui.platform.LocalContext
import com.vayu.woahelperkotlin.screens.SettingsScreen
import java.util.Locale

class SettingsActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val sharedPrefs = applicationContext.getSharedPreferences("settings", Context.MODE_PRIVATE)
        val selectedLang = sharedPrefs.getString("selected_language", "default")
        if (selectedLang != "default") {
            val config = resources.configuration
            val locale = Locale(selectedLang)
            Locale.setDefault(locale)
            config.setLocale(locale)
            createConfigurationContext(config)
            resources.updateConfiguration(config, resources.displayMetrics)
        }
        setContent {
            SettingsScreen()
        }
    }
}