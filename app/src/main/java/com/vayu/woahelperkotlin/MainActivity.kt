package com.vayu.woahelperkotlin

import android.content.Context
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.res.stringResource
import com.topjohnwu.superuser.ShellUtils
import com.vayu.woahelperkotlin.elements.dialog
import com.vayu.woahelperkotlin.screens.MainScreen
import com.vayu.woahelperkotlin.ui.theme.WoaHelperKotlinTheme
import java.util.Locale

object app {
    val version = "1.0"
}

class MainActivity : ComponentActivity() {
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
            WoaHelperKotlinTheme {
                MainScreen()
                // Root check
                val rootCheck = ShellUtils.fastCmd("su -c echo rooted")
                if (!(rootCheck == "rooted")) {
                    dialog.show_root_dialog.value = true
                }
                when {
                    dialog.show_root_dialog.value -> {
                        dialog(
                            iconless = true, iconScale = 0f, icon = 0, question = stringResource(R.string.root_not_detected),
                            buttons = listOf(), onClicks = listOf(),
                            onDismiss = { dialog.show_root_dialog.value = false }, waitText = "", finishedText = "",
                            buttonTextStyle = MaterialTheme.typography.titleSmall, paddingInButtons = 75
                        )
                    }
                }
            }
        }
    }
}