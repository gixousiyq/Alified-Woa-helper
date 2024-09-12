package com.vayu.woahelperkotlin.screens

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.topjohnwu.superuser.ShellUtils
import com.vayu.woahelperkotlin.R
import com.vayu.woahelperkotlin.elements.InfoCard
import com.vayu.woahelperkotlin.elements.button
import com.vayu.woahelperkotlin.elements.dialog
import com.vayu.woahelperkotlin.elements.toolbar
import com.vayu.woahelperkotlin.functions
import com.vayu.woahelperkotlin.ui.theme.WoaHelperKotlinTheme


@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun MainScreen() {
    WoaHelperKotlinTheme {
        Surface(
            color = MaterialTheme.colorScheme.background,
            modifier = Modifier
                .fillMaxSize()
        ) {
            val c = LocalContext.current
            Column{
                toolbar(true)
                val scrollState = rememberScrollState()
                Column (
                    // This line allows for scrolling in the app
                    modifier = Modifier.verticalScroll(scrollState),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    InfoCard()
                    Column {
                        // Backup Boot
                        button(
                            Icon = R.drawable.ic_disk,
                            Title = stringResource(R.string.backupBoot_title),
                            Subtitle = stringResource(R.string.backupBoot_subtitle),
                            {
                                dialog.show_backup_dialog.value = true
                            },
                            1.25f
                        )
                        // Mount Windows
                        button(
                            Icon = R.drawable.ic_mnt, Title = functions.mounted + " Windows",
                            Subtitle = stringResource(R.string.mount_subtitle), {
                                dialog.show_mount_dialog.value = true
                            }, 0.85f
                        )
                        // Dump Modem
                        if (functions.supportsModem) {
                            button(
                                Icon = R.drawable.ic_modem,
                                Title = stringResource(R.string.modem_title),
                                Subtitle = stringResource(R.string.modem_subtitle),
                                {
                                    dialog.show_modem_dialog.value = true
                                },
                                1.70f
                            )
                        }
                        val uefi_check = functions.checkUEFI()
                        // Flash UEFI
                        button(
                            Icon = R.drawable.ic_uefi,
                            Title = when {
                                uefi_check == "Multiple Images Detected Error" -> stringResource(R.string.multiple_images_title)
                                uefi_check == "" -> stringResource(R.string.uefi_not_found_title)
                                else -> stringResource(R.string.uefi_title)
                            },
                            Subtitle = when {
                                uefi_check == "Multiple Images Detected Error" -> stringResource(R.string.multiple_images_subtitle)
                                uefi_check == "" -> stringResource(
                                    R.string.uefi_not_found_subtitle,
                                    when (functions.DeterminePicture(LocalContext.current)) {
                                        "vayu" -> "POCO X3 PRO (VAYU)"
                                        "nabu" -> "XIAOMI PAD 5 (NABU)"
                                        "raphael" -> "XIAOMI 9T PRO (RAPHAEL)"
                                        else -> "UNKNOWN"
                                    }
                                )

                                else -> stringResource(R.string.uefi_subtitle)
                            }, {
                                if (uefi_check == "Everything is good") {
                                    dialog.show_uefi_dialog.value = true
                                }
                            }, 1.40f
                        )
                        // QuickBoot
                        if (uefi_check == "Everything is good") {
                            button(
                                Icon = R.drawable.ic_launcher_foreground,
                                Title = stringResource(R.string.quickboot_title),
                                Subtitle = stringResource(R.string.quickboot_subtitle),
                                {
                                    dialog.show_quickboot_dialog.value = true
                                },
                                1.65f
                            )
                        }
                    }
                    val context = LocalContext.current
                    // Dialogs
                    when {
                        dialog.show_backup_dialog.value -> {
                            dialog(
                                iconless = false,
                                iconScale = 1f,
                                icon = R.drawable.ic_disk,
                                question = c.getString(R.string.backup_question),
                                buttons = listOf(
                                    "Windows",
                                    "Android",
                                    c.getString(R.string.dismiss)
                                ),
                                onClicks = listOf({
                                    dialog.proceed_to_loading.value = true
                                    functions.backupBoot(destination = "windows", true, true, context)
                                }, {
                                    dialog.proceed_to_loading.value = true
                                    functions.backupBoot(destination = "android", true, true, context)
                                }, {
                                    dialog.is_finished = false
                                    dialog.show_backup_dialog.value = false
                                }),
                                onDismiss = {
                                    functions.onDismiss()
                                },
                                stringResource(R.string.please_wait),
                                stringResource(R.string.backup_done),
                                MaterialTheme.typography.bodySmall,
                                paddingInButtons = 0
                            )
                        }
                        dialog.show_mount_dialog.value -> {
                            dialog(
                                iconless = false,
                                iconScale = 0.85f,
                                icon = R.drawable.ic_mnt,
                                question = c.getString(R.string.mount_question, functions.mounted),
                                buttons = listOf(
                                    c.getString(R.string.yes),
                                    c.getString(R.string.no)
                                ),
                                onClicks = listOf({
                                    dialog.proceed_to_loading.value = true
                                    if (functions.mounted == "Mount") functions.mount(true, context)
                                    else functions.unmount(true)
                                }, {
                                    dialog.is_finished = false
                                    dialog.show_mount_dialog.value = false
                                }),
                                onDismiss = {
                                    functions.onDismiss()
                                },
                                waitText = c.getString(R.string.please_wait),
                                finishedText = c.getString(R.string.mount_done, functions.mounted),
                                MaterialTheme.typography.bodyLarge,
                                paddingInButtons = 12
                            )
                        }

                        dialog.show_modem_dialog.value -> {
                            dialog(
                                iconless = false,
                                iconScale = 1.70f,
                                icon = R.drawable.ic_modem,
                                question = c.getString(R.string.modem_question),
                                buttons = listOf(
                                    c.getString(R.string.yes),
                                    c.getString(R.string.no)
                                ),
                                onClicks = listOf({
                                    dialog.proceed_to_loading.value = true
                                    functions.dump_modem(true, context)
                                }, {
                                    dialog.is_finished = false
                                    dialog.show_modem_dialog.value = false
                                }),
                                onDismiss = {
                                    functions.onDismiss()
                                },
                                waitText = c.getString(R.string.please_wait),
                                finishedText = c.getString(R.string.modem_done),
                                MaterialTheme.typography.bodyLarge,
                                paddingInButtons = 12
                            )
                        }

                        dialog.show_uefi_dialog.value -> {
                            dialog(
                                iconless = false,
                                iconScale = 1f,
                                icon = R.drawable.ic_uefi,
                                question = c.getString(R.string.uefi_question),
                                buttons = listOf(
                                    c.getString(R.string.yes),
                                    c.getString(R.string.no)
                                ),
                                onClicks = listOf({
                                    dialog.proceed_to_loading.value = true
                                    functions.flash(true)
                                }, {
                                    dialog.is_finished = false
                                    dialog.show_uefi_dialog.value = false
                                }),
                                onDismiss = {
                                    functions.onDismiss()
                                },
                                waitText = c.getString(R.string.please_wait),
                                finishedText = c.getString(R.string.uefi_done),
                                buttonTextStyle = MaterialTheme.typography.bodyLarge,
                                paddingInButtons = 12
                            )
                        }

                        dialog.show_quickboot_dialog.value -> {
                            dialog(
                                iconless = false,
                                iconScale = 1.25f,
                                icon = R.drawable.ic_launcher_foreground,
                                question = c.getString(R.string.quickboot_question),
                                buttons = listOf(
                                    c.getString(R.string.yes),
                                    c.getString(R.string.no)
                                ),
                                onClicks = listOf({
                                    dialog.proceed_to_loading.value = true
                                    functions.quickboot(c)
                                }, {
                                    dialog.is_finished = false
                                    dialog.show_quickboot_dialog.value = false
                                }),
                                onDismiss = {
                                    functions.onDismiss()
                                },
                                waitText = c.getString(R.string.please_wait),
                                finishedText = c.getString(R.string.quickboot_done),
                                buttonTextStyle = MaterialTheme.typography.bodyLarge,
                                paddingInButtons = 12
                            )
                        }
                    }
                }
            }
        }
    }
}


