package com.vayu.woahelperkotlin.screens

import android.content.Context
import android.content.Intent
import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.vayu.woahelperkotlin.R
import com.vayu.woahelperkotlin.elements.toolbar
import com.vayu.woahelperkotlin.ui.theme.WoaHelperKotlinTheme
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.RadioButton
import androidx.compose.ui.draw.scale
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import com.vayu.woahelperkotlin.MainActivity
import com.vayu.woahelperkotlin.elements.dialog
import com.vayu.woahelperkotlin.elements.language_dialog
import java.util.Locale

@Composable
fun SettingsScreen() {
    val c = LocalContext.current
    val sharedPrefs = c.getSharedPreferences("settings", Context.MODE_PRIVATE)
    val editor = sharedPrefs.edit()

    val red = colorResource(R.color.red)
    val green = colorResource(R.color.green)

    var force_backup_check by remember {
        mutableStateOf(
            sharedPrefs.getBoolean(
                "force_backup_check",
                false
            )
        )
    }
    var force_backup_android by remember {
        mutableStateOf(
            sharedPrefs.getBoolean(
                "force_backup_android",
                false
            )
        )
    }
    var force_backup_windows by remember {
        mutableStateOf(
            sharedPrefs.getBoolean(
                "force_backup_windows",
                true
            )
        )
    }
    var detected_backup_check by remember {
        mutableStateOf(
            sharedPrefs.getBoolean(
                "detected_backup_check",
                false
            )
        )
    }
    var detected_backup_android by remember {
        mutableStateOf(
            sharedPrefs.getBoolean(
                "detected_backup_android",
                false
            )
        )
    }
    var detected_backup_windows by remember {
        mutableStateOf(
            sharedPrefs.getBoolean(
                "detected_backup_windows",
                false
            )
        )
    }
    var mount_to_mnt by remember {
        mutableStateOf(
            sharedPrefs.getBoolean(
                "mount_to_mnt",
                false
            )
        )
    }

    var force_android_btn_color by remember { mutableStateOf(red) }
    var force_windows_btn_color by remember { mutableStateOf(red) }
    var detected_android_btn_color by remember { mutableStateOf(red) }
    var detected_windows_btn_color by remember { mutableStateOf(red) }
    var mount_to_mnt_btn_color by remember { mutableStateOf(red) }

    force_android_btn_color = if (force_backup_android) green else red
    force_windows_btn_color = if (force_backup_windows) green else red
    detected_android_btn_color = if (detected_backup_android) green else red
    detected_windows_btn_color = if (detected_backup_windows) green else red
    mount_to_mnt_btn_color = if (mount_to_mnt) green else red

    fun updateButtons() {
        force_android_btn_color = if (force_backup_android) green else red
        force_windows_btn_color = if (force_backup_windows) green else red
        detected_android_btn_color = if (detected_backup_android) green else red
        detected_windows_btn_color = if (detected_backup_windows) green else red
        mount_to_mnt_btn_color = if (mount_to_mnt) green else red
    }

    WoaHelperKotlinTheme {
        Surface(
            modifier = Modifier.fillMaxSize()
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                toolbar(showIcons = false)
                val scrollState = rememberScrollState()
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .fillMaxWidth()
                        .verticalScroll(scrollState)
                ) {
                    // Forced backup button
                    Surface(
                        shape = MaterialTheme.shapes.large,
                        color = colorResource(R.color.settings_cards),
                        modifier = Modifier
                            .padding(top = 20.dp, start = 10.dp, end = 10.dp)
                            .width(400.dp)
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Row(
                                horizontalArrangement = Arrangement.Center
                            ) {
                                val localDensity = LocalDensity.current
                                var textHeight by remember {
                                    mutableStateOf(0.dp)
                                }
                                Text(
                                    text = c.getString(R.string.force_backup),
                                    style = MaterialTheme.typography.titleLarge,
                                    fontWeight = FontWeight.SemiBold,
                                    textAlign = TextAlign.Center,
                                    color = colorResource(R.color.white),
                                    modifier = Modifier
                                        .padding(vertical = 10.dp)
                                        .scale(0.90f)
                                        .width(300.dp)
                                        .onGloballyPositioned { coordinates ->
                                            // Set column height using the LayoutCoordinates
                                            textHeight =
                                                with(localDensity) { coordinates.size.height.toDp() }
                                        }
                                )
                                Column(
                                    verticalArrangement = Arrangement.Center,
                                    modifier = Modifier
                                        .height(textHeight)
                                        .padding(top = 10.dp)
                                ) {
                                    Switch(
                                        checked = force_backup_check, onCheckedChange = {
                                            force_backup_check = it
                                            editor.putBoolean("force_backup_check", it)
                                            editor.commit()
                                        }, modifier = Modifier
                                            .padding(top = 15.dp)
                                    )
                                }
                            }
                            AnimatedVisibility(
                                visible = force_backup_check,
                            ) {
                                Surface(
                                    color = colorResource(R.color.settings_cards)
                                ) {
                                    Column(
                                        horizontalAlignment = Alignment.CenterHorizontally,
                                        modifier = Modifier
                                            .fillMaxWidth()
                                    ) {
                                        Text(
                                            text = c.getString(R.string.save_in),
                                            style = MaterialTheme.typography.titleLarge,
                                            fontWeight = FontWeight.SemiBold,
                                            textAlign = TextAlign.Center,
                                            color = colorResource(R.color.cyan),
                                            modifier = Modifier
                                                .padding(top = 0.dp)
                                                .fillMaxWidth()
                                        )
                                        Row(
                                            horizontalArrangement = Arrangement.Center,
                                            modifier = Modifier
                                                .padding(top = 5.dp, bottom = 5.dp)
                                                .fillMaxWidth()
                                        ) {
                                            Button(
                                                onClick = {
                                                    editor.putBoolean(
                                                        "force_backup_android",
                                                        !force_backup_android
                                                    )
                                                    editor.apply()
                                                    force_backup_android = !force_backup_android
                                                    updateButtons()
                                                },
                                                modifier = Modifier
                                                    .padding(start = 0.dp, end = 10.dp)
                                                    .width(170.dp),
                                                colors = ButtonDefaults.buttonColors(
                                                    force_android_btn_color
                                                )
                                            ) {
                                                Text(
                                                    text = "Android"
                                                )
                                            }
                                            Button(
                                                onClick = {
                                                    editor.putBoolean(
                                                        "force_backup_windows",
                                                        !force_backup_windows
                                                    )
                                                    editor.apply()
                                                    force_backup_windows = !force_backup_windows
                                                    updateButtons()
                                                },
                                                modifier = Modifier
                                                    .width(170.dp),
                                                colors = ButtonDefaults.buttonColors(
                                                    force_windows_btn_color
                                                )
                                            ) {
                                                Text(
                                                    text = "Windows"
                                                )
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                    // Detected Backup Button
                    AnimatedVisibility(visible = !force_backup_check || !force_backup_android || !force_backup_windows) {
                        Surface(
                            shape = MaterialTheme.shapes.large,
                            color = colorResource(R.color.settings_cards),
                            modifier = Modifier
                                .padding(top = 10.dp, start = 10.dp, end = 10.dp)
                                .width(400.dp)
                        ) {
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.Center
                            ) {
                                Row(
                                    horizontalArrangement = Arrangement.Center
                                ) {
                                    val localDensity = LocalDensity.current
                                    var textHeight by remember {
                                        mutableStateOf(0.dp)
                                    }
                                    Text(
                                        text = c.getString(R.string.ifnot_detected_backup),
                                        style = MaterialTheme.typography.titleLarge,
                                        fontWeight = FontWeight.SemiBold,
                                        textAlign = TextAlign.Center,
                                        color = colorResource(R.color.white),
                                        modifier = Modifier
                                            .padding(vertical = 10.dp)
                                            .scale(0.90f)
                                            .width(300.dp)
                                            .onGloballyPositioned { coordinates ->
                                                // Set column height using the LayoutCoordinates
                                                textHeight =
                                                    with(localDensity) { coordinates.size.height.toDp() }
                                            }
                                    )
                                    Column(
                                        verticalArrangement = Arrangement.Center,
                                        modifier = Modifier.height(textHeight)
                                    ) {
                                        Switch(
                                            checked = detected_backup_check, onCheckedChange = {
                                                detected_backup_check = it
                                                editor.putBoolean("detected_backup_check", it)
                                                editor.commit()
                                            }, modifier = Modifier
                                                .padding(top = 30.dp)
                                        )
                                    }
                                }
                                AnimatedVisibility(
                                    visible = detected_backup_check,
                                ) {
                                    Surface(
                                        color = colorResource(R.color.settings_cards)
                                    ) {
                                        Column {
                                            Text(
                                                text = c.getString(R.string.save_in),
                                                style = MaterialTheme.typography.titleLarge,
                                                fontWeight = FontWeight.SemiBold,
                                                textAlign = TextAlign.Center,
                                                color = colorResource(R.color.cyan),
                                                modifier = Modifier
                                                    .padding(top = 0.dp)
                                                    .fillMaxWidth()
                                            )
                                            Row(
                                                horizontalArrangement = Arrangement.Center,
                                                modifier = Modifier
                                                    .padding(top = 5.dp, bottom = 5.dp)
                                                    .fillMaxWidth()
                                            ) {
                                                AnimatedVisibility(
                                                    visible = !force_backup_android || !force_backup_check
                                                ) {
                                                    Button(
                                                        onClick = {
                                                            editor.putBoolean(
                                                                "detected_backup_android",
                                                                !detected_backup_android
                                                            )
                                                            editor.apply()
                                                            detected_backup_android =
                                                                !detected_backup_android
                                                            updateButtons()
                                                        },
                                                        modifier = Modifier
                                                            .padding(start = 0.dp, end = 10.dp)
                                                            .width(170.dp),
                                                        colors = ButtonDefaults.buttonColors(
                                                            detected_android_btn_color
                                                        )
                                                    ) {
                                                        Text(
                                                            text = "Android"
                                                        )
                                                    }
                                                }
                                                AnimatedVisibility(
                                                    visible = !force_backup_windows || !force_backup_check,
                                                ) {
                                                    Button(
                                                        onClick = {
                                                            editor.putBoolean(
                                                                "detected_backup_windows",
                                                                !detected_backup_windows
                                                            )
                                                            editor.apply()
                                                            detected_backup_windows =
                                                                !detected_backup_windows
                                                            updateButtons()
                                                        },
                                                        modifier = Modifier
                                                            .width(170.dp),
                                                        colors = ButtonDefaults.buttonColors(
                                                            detected_windows_btn_color
                                                        )
                                                    ) {
                                                        Text(
                                                            text = "Windows"
                                                        )
                                                    }

                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                    // Select language button
                    Surface(
                        shape = MaterialTheme.shapes.large,
                        onClick = {
                            dialog.show_language_dialog.value = true
                        },
                        color = colorResource(R.color.settings_cards),
                        modifier = Modifier
                            .padding(top = 10.dp, start = 10.dp, end = 10.dp)
                            .width(400.dp)
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = c.getString(R.string.language),
                                style = MaterialTheme.typography.titleLarge,
                                fontWeight = FontWeight.SemiBold,
                                textAlign = TextAlign.Center,
                                color = colorResource(R.color.white),
                                modifier = Modifier
                                    .padding(vertical = 20.dp)
                                    .scale(0.90f)
                                    .width(350.dp)
                            )
                        }
                    }

                    Surface(
                        shape = MaterialTheme.shapes.large,
                        color = colorResource(R.color.settings_cards),
                        modifier = Modifier
                            .padding(top = 10.dp, start = 10.dp, end = 10.dp)
                            .width(400.dp)
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Row(
                                horizontalArrangement = Arrangement.Center
                            ) {
                                val localDensity = LocalDensity.current
                                var textHeight by remember {
                                    mutableStateOf(0.dp)
                                }
                                Text(
                                    text = c.getString(R.string.mount_to_mnt),
                                    style = MaterialTheme.typography.titleLarge,
                                    fontWeight = FontWeight.SemiBold,
                                    textAlign = TextAlign.Center,
                                    color = colorResource(R.color.white),
                                    modifier = Modifier
                                        .padding(vertical = 10.dp)
                                        .scale(0.90f)
                                        .width(300.dp)
                                        .onGloballyPositioned { coordinates ->
                                            // Set column height using the LayoutCoordinates
                                            textHeight =
                                                with(localDensity) { coordinates.size.height.toDp() }
                                        }
                                )
                                Column(
                                    verticalArrangement = Arrangement.Center,
                                    modifier = Modifier
                                        .height(textHeight)
                                        .padding(top = 10.dp)
                                ) {
                                    Switch(
                                        checked = mount_to_mnt, onCheckedChange = {
                                            mount_to_mnt = it
                                            editor.putBoolean("mount_to_mnt", it)
                                            editor.commit()
                                        }, modifier = Modifier
                                            .padding(top = 15.dp)
                                    )
                                }
                            }
                        }
                    }
                }
            }

            when {
                dialog.show_language_dialog.value -> {
                    language_dialog(sharedPrefs, editor)
                }
            }

            // Floating save button code
            Column(
                horizontalAlignment = Alignment.End,
                verticalArrangement = Arrangement.Bottom,
                modifier = Modifier
                    .fillMaxSize()
            ) {
                Surface(
                    color = colorResource(R.color.white),
                    shape = MaterialTheme.shapes.extraLarge,
                    modifier = Modifier
                        .padding(end = 20.dp, bottom = 40.dp)
                        .size(75.dp)
                        .clickable {
                            editor.apply()
                            Toast
                                .makeText(
                                    c,
                                    c.getString(R.string.settings_saved),
                                    Toast.LENGTH_SHORT
                                )
                                .show()
                            c.startActivity(Intent(c, MainActivity::class.java))
                        }
                ) {
                    Image(
                        painter = painterResource(R.drawable.ic_mnt),
                        contentDescription = "Save settings",
                        modifier = Modifier
                            .scale(0.60f)
                    )
                }
            }
        }
    }
}

