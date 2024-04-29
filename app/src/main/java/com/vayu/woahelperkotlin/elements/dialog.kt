package com.vayu.woahelperkotlin.elements

import android.content.SharedPreferences
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.vayu.woahelperkotlin.R
import com.vayu.woahelperkotlin.functions
import com.vayu.woahelperkotlin.ui.theme.WoaHelperKotlinTheme


object dialog {
    var show_backup_dialog = mutableStateOf(false)
    var show_mount_dialog = mutableStateOf(false)
    var show_modem_dialog = mutableStateOf(false)
    var show_uefi_dialog = mutableStateOf(false)
    var show_quickboot_dialog = mutableStateOf(false)
    var proceed_to_loading = mutableStateOf(false)
    var show_root_dialog = mutableStateOf(false)
    var show_language_dialog = mutableStateOf(false)
    var is_finished = false
}

@Composable
fun dialog(
    iconless: Boolean, iconScale: Float, icon: Int, question: String, buttons: List<String>, onClicks: List<() -> Unit>, onDismiss: () -> Unit,
    waitText: String, finishedText: String, buttonTextStyle: TextStyle, paddingInButtons : Int
) {
    WoaHelperKotlinTheme {
        Dialog(onDismissRequest = onDismiss) {
            Surface (
                shape = MaterialTheme.shapes.extraLarge,
                color = colorResource(R.color.cards),
            ) {
                Column (
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    if (!iconless) {
                        Image(
                            painter = painterResource(icon),
                            contentDescription = "Dialog Logo",
                            modifier = Modifier
                                .size(100.dp)
                                .scale(iconScale)
                        )
                    }
                    Text(
                        text = when {
                                    dialog.proceed_to_loading.value -> waitText
                                    dialog.is_finished -> finishedText
                                    else -> question
                                    },
                        color =  colorResource(R.color.white),
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center,
                        modifier = Modifier
                            .padding(top = 10.dp, bottom = 15.dp)
                            .fillMaxWidth()
                    )
                    Row(
                        modifier = Modifier
                            .padding(horizontal = paddingInButtons.dp)
                    ) {
                        when {
                            !dialog.proceed_to_loading.value && !dialog.is_finished -> {
                                buttons.forEachIndexed { index, it ->
                                    Button(
                                        onClick = {
                                            Thread (
                                                onClicks[index]
                                                ).start()
                                                  },
                                        shape = MaterialTheme.shapes.large,
                                        modifier = Modifier
                                            .weight(1f, true)
                                            .padding(
                                                end = 3.dp,
                                                start = 3.dp,
                                                bottom = 7.dp
                                            )
                                    ) {
                                        Text(
                                            style = buttonTextStyle,
                                            text = it
                                        )
                                    }
                                }
                            }
                            dialog.is_finished -> {
                                Button(
                                    onClick = onDismiss,
                                    shape = MaterialTheme.shapes.large,
                                    modifier = Modifier
                                        .padding(bottom = 7.dp)
                                ) {
                                    Text(text = stringResource(R.string.dismiss))
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun language_dialog(sharedPrefrences: SharedPreferences,preferencesEditor: SharedPreferences.Editor) {
    WoaHelperKotlinTheme {
        Dialog(onDismissRequest = { dialog.show_language_dialog.value = false }) {
            Surface (
                shape = MaterialTheme.shapes.large,
                color = colorResource(R.color.cards)
            ) {
                Column {
                    Text(
                        text = stringResource(R.string.language),
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.SemiBold,
                        textAlign = TextAlign.Center,
                        color = colorResource(R.color.white),
                        modifier = Modifier
                            .padding(top = 15.dp, start = 100.dp, end = 100.dp, bottom = 15.dp)
                    )
                    // The 3 lists must be in one order! eg languages[0] = "en" so options[0] needs
                    // to be = "English" !
                    val options = listOf(stringResource(R.string.default_lang), "English", "العربية")
                    val options_id = listOf(0,1,2)
                    val languages = listOf("default", "en", "ar")
                    var selectedOption by remember { mutableStateOf(sharedPrefrences.getInt("selected_language_id", 0)) }
                    options_id.forEach { option ->
                        Row (
                            modifier = Modifier.padding(start = 5.dp, bottom = 15.dp)
                        ) {
                            RadioButton(
                                selected = (selectedOption == option),
                                onClick = {
                                    selectedOption = option
                                    preferencesEditor.putInt("selected_language_id", option)
                                    preferencesEditor.putString("selected_language", languages[option])
                                    preferencesEditor.apply()
                                }
                            )
                            Text(
                                text = options[option],
                                modifier = Modifier.padding(top = 12.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}